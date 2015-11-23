package ru.spbau.mit;

import java.util.Random;

public class SumTwoNumbersGame implements Game {
    private GameServer server;
    private int guessedA, guessedB;
    private Random random = new Random(42);

    public SumTwoNumbersGame(GameServer server) {
        this.server = server;
        guessedA = random.nextInt(1000);
        guessedB = random.nextInt(1000);
    }

    @Override
    public void onPlayerConnected(String id) {
        server.sendTo(id, String.format("%d %d", guessedA, guessedB));
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        try {
            int answer = Integer.parseInt(msg);
            if (guessedA + guessedB == answer) {
                server.sendTo(id, "Right");
                server.broadcast(id + " won");

                guessedA = random.nextInt(1000);
                guessedB = random.nextInt(1000);
                server.broadcast(String.format("%d %d", guessedA, guessedB));
            } else {
                server.sendTo(id, "Wrong");
            }
        } catch (NumberFormatException e) {
        }
    }
}
