package net.mistrio.briscola.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.mistrio.briscola.testutils.DefaultTrumpStateFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrumpStateConverterTest {

    @Test
    void converterWorks() {
        var trumpStateConverter = new TrumpStateConverter();

        var defaultGameState = new DefaultTrumpStateFactory().createTrumpState();
        var json = trumpStateConverter.convertToDatabaseColumn(defaultGameState);
        var gameState = trumpStateConverter.convertToEntityAttribute(json);

        assertEquals(defaultGameState, gameState);
    }
}
