package com.hwachang.hwachangapi.utils.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Converter
public class UniversalListConverter implements AttributeConverter<List<?>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<?> attribute) {
        try {
            // List<?>를 JSON 문자열로 변환
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 실패: " + attribute, e);
        }
    }

    @Override
    public List<?> convertToEntityAttribute(String dbData) {
        try {
            // 우선 JSON을 List<Object>로 역직렬화
            List<Object> tempList = objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));

            // UUID로 변환 가능한 경우 List<UUID>, 아니면 List<String>으로 반환
            if (tempList.stream().allMatch(item -> isUuid(item.toString()))) {
                return tempList.stream().map(item -> UUID.fromString(item.toString())).collect(Collectors.toList());
            }
            return tempList.stream().map(Object::toString).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패: " + dbData, e);
        }
    }

    // UUID 형식 확인 메서드
    private boolean isUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
