package net.mistrio.briscola.service.trump;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpCardRank;
import net.mistrio.briscola.model.TrumpCardSuite;
import net.mistrio.briscola.service.TrumpPlayCard;
import net.mistrio.briscola.testutils.DefaultTrumpStateFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class TrumpPlayCardTest {

    @Test
    void updateStateOnPlayCard() {
        var defaultGameState = new DefaultTrumpStateFactory().createTrumpState();
        var trumpPlayCard = new TrumpPlayCard();
        var playerHand = defaultGameState.getPlayerHands().get(100);
        var playedCard = new TrumpCard(TrumpCardRank.TWO, TrumpCardSuite.COINS);

        trumpPlayCard.playCard(defaultGameState, 100, playedCard);

        assertEquals(Optional.of(playedCard), playerHand.getPlayedCard());
    }

    @Test
    void disallowPlayNotOwnedCard() {
        var defaultGameState = new DefaultTrumpStateFactory().createTrumpState();
        var trumpPlayCard = new TrumpPlayCard();
        var playedCard = new TrumpCard(TrumpCardRank.ACE, TrumpCardSuite.COINS);

        assertThrows(
                IllegalStateException.class,
                () -> trumpPlayCard.playCard(defaultGameState, 100, playedCard));
    }

    @Test
    void resolveStateOnPlayCard() {
        var defaultGameState = new DefaultTrumpStateFactory().createTrumpState();
        var trumpPlayCard = new TrumpPlayCard();
        var playedCard1 = new TrumpCard(TrumpCardRank.TWO, TrumpCardSuite.COINS);
        var playedCard2 = new TrumpCard(TrumpCardRank.SIX, TrumpCardSuite.COINS);

        trumpPlayCard.playCard(defaultGameState, 100, playedCard1);
        trumpPlayCard.playCard(defaultGameState, 101, playedCard2);

        var playerHand1 = defaultGameState.getPlayerHands().get(100);
        var playerHand2 = defaultGameState.getPlayerHands().get(101);
        var playerStash1 = defaultGameState.getPlayerStashes().get(100);
        var playerStash2 = defaultGameState.getPlayerStashes().get(101);

        assertEquals(Optional.empty(), playerHand1.getPlayedCard());
        assertEquals(Optional.empty(), playerHand2.getPlayedCard());

        assertEquals(0, playerStash1.getCards().size());
        assertEquals(2, playerStash2.getCards().size());

        var knightCoins = new TrumpCard(TrumpCardRank.KNIGHT, TrumpCardSuite.COINS);
        var kingCoins = new TrumpCard(TrumpCardRank.KING, TrumpCardSuite.COINS);
        assertTrue(playerHand1.getCards().contains(kingCoins));
        assertTrue(playerHand2.getCards().contains(knightCoins));
    }

    @Test
    void resolveStateOnPlayLastCards() {
        var defaultGameState = new DefaultTrumpStateFactory().createTrumpState();
        var trumpPlayCard = new TrumpPlayCard();

        defaultGameState.getDeck().draw(34);

        var playedCard1 = new TrumpCard(TrumpCardRank.TWO, TrumpCardSuite.COINS);
        var playedCard2 = new TrumpCard(TrumpCardRank.SIX, TrumpCardSuite.COINS);
        var playerHand1 = defaultGameState.getPlayerHands().get(100);
        var playerHand2 = defaultGameState.getPlayerHands().get(101);

        trumpPlayCard.playCard(defaultGameState, 100, playedCard1);
        trumpPlayCard.playCard(defaultGameState, 101, playedCard2);

        assertEquals(2, playerHand1.getCards().size());
        assertEquals(2, playerHand2.getCards().size());
    }
}
