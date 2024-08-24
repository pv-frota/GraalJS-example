package com.example.pvfrota.graalJS.model;

import com.example.pvfrota.graalJS.enumeration.JavaTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.graalvm.polyglot.Value;

import java.util.List;

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
public class Logic {
    @Id
    private String name;
    private String description;

    @Lob
    private String script;

    @Enumerated(EnumType.STRING)
    private JavaTypeEnum type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "logic")
    private List<Parameter> parameters;

    public Object getTypedValue(Value value) {
        return type.parse(value);
    }
}
