package com.example.pvfrota.graalJS.service;

import com.example.pvfrota.graalJS.model.Logic;
import com.example.pvfrota.graalJS.model.Parameter;
import com.example.pvfrota.graalJS.repository.LogicRepository;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
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
    private final String FUNCTION = "main";
    private final LogicRepository logicRepository;

    public GraalService(LogicRepository logicRepository) {
        this.logicRepository = logicRepository;
    }

    public Object runScript(String logicName, Parameter... dynamicParameters) {
        try {
            Logic logic = logicRepository.getReferenceById(logicName);
            return execute(logic, dynamicParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object execute(Logic logic, Parameter... dynamicParameters) {
        try {
            validateScript(dynamicParameters, logic.getParameters(), logic.getScript());

            try (Context context = Context.create()) {
                context.eval(LANGUAGE, logic.getScript());
                logic.getParameters().forEach(p -> setAsValue(context, p));
                if(dynamicParameters.length > 0) Arrays.stream(dynamicParameters).forEach(p -> setAsValue(context, p));

                Value function = context.getBindings(LANGUAGE).getMember(FUNCTION);

                log.info("Executando lógica: {}", logic.getName());
                Value result = function.execute();
                return logic.getTypedValue(result);
            }
        } catch (Exception e) {
            log.error("Ocorreu um erro ao executar a lógica: {}", e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao executar a lógica: " + e);
        }
    }

    private void validateScript(Parameter[] dynamics, List<Parameter> presets, String script) throws Exception {
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
            int i = dynamics.length + presets.size();
            if (i < count) throw new Exception("Quantidade de parâmetros deve ser a mesma quantidade do script!");
        }

        for (String v : variables) {
            Predicate<Parameter> parameterPredicate = p -> p.getName().equals(v);
            boolean existsInPresets = presets.stream().anyMatch(parameterPredicate);
            boolean existsInDynamics = Arrays.stream(dynamics).anyMatch(parameterPredicate);
            if (!existsInPresets && !existsInDynamics) throw new Exception("Parâmetro " + v + " não encontrado!");
        }
    }

    private void setAsValue(Context context, Parameter p) {
        Value value = context.asValue(p.getTypedValue());
        context.getBindings(LANGUAGE).putMember(p.getName(), value);
    }
}
