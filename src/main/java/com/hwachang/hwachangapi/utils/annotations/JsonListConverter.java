package com.hwachang.hwachangapi.utils.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.Map;

public class JsonListConverter implements AttributeConverter<List<Map<String, Object>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String, Object>> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute); // JSON 문자열로 직렬화
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 직렬화 실패", e);
        }
    }

    @Override
    public List<Map<String, Object>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, List.class); // JSON 문자열을 다시 List로 역직렬화
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 역직렬화 실패", e);
        }
    }
}