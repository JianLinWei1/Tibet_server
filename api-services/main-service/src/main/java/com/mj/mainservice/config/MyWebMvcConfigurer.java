package com.mj.mainservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.io.IOException;
import java.util.List;

/**
 * 将接受的“” 转为null
 * @auther JianLinWei
 * @date 2020-11-24 21:03
 */
@Configuration
public class MyWebMvcConfigurer  implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringWithoutSpaceDeserializer(String.class));
        mapper.registerModule(module);

        converter.setObjectMapper(mapper);

        return converter;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.deserializerByType(String.class, new StringDeserializer() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        String deserialize = super.deserialize(p, ctxt);
                        if (deserialize != null && deserialize.equals("")) {
                            return null;
                        }
                        return deserialize;
                    }
                });
            }
        };
    }




}
