package com.example.pvfrota.graalJS.enumeration;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 22/08/2024
 */
@Log4j2
public enum JavaTypeEnum {
    STRING(String.class),
    BIG_DECIMAL(BigDecimal.class),
    BIG_INTEGER(BigInteger.class),
    BOOLEAN(Boolean.class),
    SHORT(Short.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DATE(LocalDate.class),
    DATETIME(LocalDateTime.class),
    ARRAY(Collection.class);

    private final Class<?> typeClass;

    JavaTypeEnum(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public Object parse(Object value) {
        if(!this.equals(STRING)) {
            try {
                Constructor<?> constructor = typeClass.getConstructor(String.class);
                return constructor.newInstance(String.valueOf(value));
            } catch (Exception e) {
                log.info("Construtor não encontrado, tentando método valueOf...");
            }

            try {
                Method valueOf = typeClass.getMethod("valueOf", String.class);
                return valueOf.invoke(null, String.valueOf(value));
            } catch (Exception e) {
                log.info("Método valueOf não encontrado, tentando método parse...");
            }

            try {
                Method parse = typeClass.getMethod("parse", String.class);
                return parse.invoke(null, String.valueOf(value));
            } catch (Exception e) {
                log.info("Método parse não encontrado, retornando valor string...");
            }
        }

        return value;
    }
}
