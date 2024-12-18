package com.hwachang.hwachangapi.utils.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.UUID;

@Converter
public class StringListConverter implements AttributeConverter<List<UUID>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List attribute) {
        try{
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UUID> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, UUID.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패 " + dbData, e);
        }
    }
}
