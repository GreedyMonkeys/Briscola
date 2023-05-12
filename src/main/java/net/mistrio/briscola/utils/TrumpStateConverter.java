package net.mistrio.briscola.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

import lombok.extern.slf4j.Slf4j;

import net.mistrio.briscola.model.TrumpGameState;

import java.io.IOException;

@Slf4j
public class TrumpStateConverter implements AttributeConverter<TrumpGameState, String> {
    private final ObjectMapper mapper;

    public TrumpStateConverter() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Override
    public String convertToDatabaseColumn(TrumpGameState gameState) {
        try {
            return mapper.writeValueAsString(gameState);
        } catch (final IOException e) {
            log.error("JSON writing error", e);
            return null;
        }
    }

    @Override
    public TrumpGameState convertToEntityAttribute(String gameStateJson) {
        try {
            return mapper.readValue(gameStateJson, new TypeReference<TrumpGameState>() {});
        } catch (final IOException e) {
            log.error("JSON reading error", e);
            return null;
        }
    }
}
