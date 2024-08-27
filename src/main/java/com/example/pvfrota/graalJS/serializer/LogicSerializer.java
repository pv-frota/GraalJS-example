package com.example.pvfrota.graalJS.serializer;

import com.example.pvfrota.graalJS.model.Logic;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author Pedro Victor (pedro.victor@wpe4bank.com)
 * @since 27/08/2024
 */
public class LogicSerializer extends StdSerializer<Logic> {
    protected LogicSerializer() {
        super(Logic.class);
    }

    @Override
    public void serialize(Logic logic, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString(logic.getName());
    }
}
