package net.mistrio.briscola.exception;

public class GameNotFoundException extends IllegalStateException {
    public GameNotFoundException() {
        super("Game not found");
    }
}
