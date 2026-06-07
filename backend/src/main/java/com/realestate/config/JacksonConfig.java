package com.realestate.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER) {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText().trim();
                if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
                    return null;
                }
                return super.deserialize(parser, context);
            }
        });

        javaTimeModule.addDeserializer(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText();
                if (text == null || text.trim().isEmpty() || "null".equalsIgnoreCase(text)) {
                    return null;
                }
                return text.trim();
            }
        });

        javaTimeModule.addDeserializer(Integer.class, new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText().trim();
                if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
                    return null;
                }
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });

        javaTimeModule.addDeserializer(Long.class, new JsonDeserializer<Long>() {
            @Override
            public Long deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText().trim();
                if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
                    return null;
                }
                try {
                    return Long.parseLong(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });

        javaTimeModule.addDeserializer(BigDecimal.class, new JsonDeserializer<BigDecimal>() {
            @Override
            public BigDecimal deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText().trim();
                if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
                    return null;
                }
                try {
                    return new BigDecimal(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });

        javaTimeModule.addDeserializer(int.class, new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText().trim();
                if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
                    return 0;
                }
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });

        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
