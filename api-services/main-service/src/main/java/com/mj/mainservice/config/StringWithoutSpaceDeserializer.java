package com.mj.mainservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @auther JianLinWei
 * @date 2020-12-05 1:05
 */
public class StringWithoutSpaceDeserializer  extends StdDeserializer<String> {
    private static final long serialVersionUID = -6972065572263950443L;

    public StringWithoutSpaceDeserializer(Class<String> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getText() != null ? p.getText().trim() : null;
    }
}
