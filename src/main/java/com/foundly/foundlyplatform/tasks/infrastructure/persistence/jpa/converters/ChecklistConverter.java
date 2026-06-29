package com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ChecklistConverter implements AttributeConverter<List<Task.ChecklistItem>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Task.ChecklistItem> checklist) {
        if (checklist == null || checklist.isEmpty()) return "[]";
        try {
            return mapper.writeValueAsString(checklist);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<Task.ChecklistItem> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return mapper.readValue(json, new TypeReference<List<Task.ChecklistItem>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
