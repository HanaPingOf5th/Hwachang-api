package com.hwachang.hwachangapi.utils.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import jakarta.persistence.AttributeConverter;

public class ApplicationformConverter implements AttributeConverter<ApplicationForm, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(ApplicationForm attribute) {
        return "";
    }

    @Override
    public ApplicationForm convertToEntityAttribute(String dbData) {
        return null;
    }
}
