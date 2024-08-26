package com.example.pvfrota.graalJS.service;

import com.example.pvfrota.graalJS.enumeration.ParameterTypeEnum;
import com.example.pvfrota.graalJS.model.Logic;
import com.example.pvfrota.graalJS.model.Parameter;
import com.example.pvfrota.graalJS.record.DynamicParameterValue;
import com.example.pvfrota.graalJS.repository.LogicRepository;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 22/08/2024
 */
@Log4j2
@Service
public class GraalService {
    private final String LANGUAGE = "js";
    private final LogicService logicService;

    public GraalService(LogicService logicService) {
        this.logicService = logicService;
    }

    public Object runScript(String logicName, DynamicParameterValue... dynamicParameterValues) {
        try {
            Logic logic = logicService.findById(logicName)
                    .orElseThrow(() -> new Exception("Não foi possível encontrar a lógica: " + logicName));
            if(dynamicParameterValues.length > 0) handleDynamicParameterValues(logic, dynamicParameterValues);
            return execute(logic);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private Object execute(Logic logic) throws Exception {
        try {
            validateScript(logic.getParameters(), logic.getScript());

            try (Context context = Context.create()) {
                context.eval(LANGUAGE, logic.getScript());
                logic.getParameters().forEach(p -> setAsValue(context, p));

                Value function = context.getBindings(LANGUAGE).getMember("main");

                log.info("Executando lógica: {}", logic.getName());
                Value result = function.execute();
                return logic.getTypedValue(result);
            }
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao executar a lógica: " + e);
        }
    }

    private void handleDynamicParameterValues(Logic logic, DynamicParameterValue[] dynamicParameterValues) throws Exception {
        List<Parameter> dynamicParameters = logic.getParameters().stream()
                .filter(p -> p.getParameterType().equals(ParameterTypeEnum.DYNAMIC))
                .toList();
        try {
            List<DynamicParameterValue> parameterValues = Arrays.asList(dynamicParameterValues);
            for (Parameter parameter: dynamicParameters){
                DynamicParameterValue parameterValue = parameterValues.stream()
                        .filter(pv -> pv.name().equals(parameter.getName()))
                        .findFirst()
                        .orElseThrow(() -> new Exception("Parâmetro " + parameter.getName() + " não encontrado!"));
                parameter.setValue(parameterValue.value());
            }
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao tratar os parâmetros dinâmicos: " + e);
        }
    }

    private void validateScript(List<Parameter> parameters, String script) throws Exception {
        int count = 0;
        String regex = "let\\s(\\w+)";

        String cleanedScript = script.replaceAll("\\{[^{}]*}", "");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanedScript);
        List<String> variables = new ArrayList<>();

        while (matcher.find()) {
            count++;
            variables.add(matcher.group().replace("let", "").strip());
        }

        if(count > 0) {
            if (parameters.size() < count) throw new Exception("Quantidade de parâmetros deve ser a mesma quantidade do script!");
        }

        for (String v : variables) {
            boolean exists = parameters.stream().anyMatch(p -> p.getName().equals(v));
            if (!exists) throw new Exception("Parâmetro " + v + " não encontrado!");
        }
    }

    private void setAsValue(Context context, Parameter p) {
        Value value = context.asValue(p.getTypedValue());
        context.getBindings(LANGUAGE).putMember(p.getName(), value);
    }
}
