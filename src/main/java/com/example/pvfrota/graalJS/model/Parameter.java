package com.example.pvfrota.graalJS.model;

import com.example.pvfrota.graalJS.enumeration.JavaTypeEnum;
import com.example.pvfrota.graalJS.enumeration.ParameterTypeEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 22/08/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Parameter {
    @Id
    private String name;
    private String description;
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "PARAMETER_TYPE")
    private ParameterTypeEnum parameterTypeEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "JAVA_TYPE")
    private JavaTypeEnum javaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOGIC_NAME")
    private Logic logic;

    public Parameter(String name, String description, ParameterTypeEnum parameterTypeEnum, JavaTypeEnum javaType, String value) {
        this.name = name;
        this.description = description;
        this.parameterTypeEnum = parameterTypeEnum;
        this.javaType = javaType;
        this.value = value;
    }

    public Parameter(String name, JavaTypeEnum javaType, String value) {
        this.name = name;
        this.javaType = javaType;
        this.value = value;
    }

    public Object getTypedValue() {
        return javaType.parse(value);
    }
}
