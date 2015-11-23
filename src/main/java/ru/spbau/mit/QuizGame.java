package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuizGame implements Game {
    private GameServer server;
    private int maxLettersToOpen, delayUntilNextLetter;
    private ArrayList<String> questions = new ArrayList<>(), answers = new ArrayList<>();
    private int curQuestion = 0;
    private int lettersOpened = 0;

    public QuizGame(GameServer server) {
        this.server = server;
    }

    @Override
    public void onPlayerConnected(String id) {
    }

    private Runnable gameMaster = new Runnable() {
        @Override
        public synchronized void run() {
            while (true) {
                try {
                    wait(delayUntilNextLetter);
                } catch (InterruptedException e) {
                    return;
                }

                if (lettersOpened < maxLettersToOpen) {
                    ++lettersOpened;
                    server.broadcast("Current prefix is " + answers.get(curQuestion).substring(0, lettersOpened));
                } else {
                    server.broadcast("Nobody guessed, the word was " + answers.get(curQuestion));
                    curQuestion = (curQuestion + 1) % questions.size();
                    lettersOpened = 0;
                }
            }
        }
    };

    private Thread gameMasterThread;

    private void newRound() {
        if (gameMasterThread != null) {
            gameMasterThread.interrupt();
        }
        curQuestion = (curQuestion + 1) % questions.size();
        lettersOpened = 0;
        server.broadcast(String.format("New round started: %s (%d letters)",
                questions.get(curQuestion), answers.get(curQuestion).length()));
        gameMasterThread = new Thread(gameMaster);
        gameMasterThread.start();
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        if (msg.equals("!start")) {
            newRound();
        } else if (msg.equals("!stop")) {
            server.broadcast("Game has been stopped by " + id);
        } else if (msg.equals(answers.get(curQuestion))) {
            server.broadcast("The winner is " + id);
            newRound();
        } else {
            server.sendTo(id, "Wrong try");
        }
    }

    public void setMaxLettersToOpen(int maxLettersToOpen) {
        this.maxLettersToOpen = maxLettersToOpen;
    }

    public void setDelayUntilNextLetter(int delayUntilNextLetter) {
        this.delayUntilNextLetter = delayUntilNextLetter;
    }

    public void setDictionaryFilename(String dictionaryFilename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(dictionaryFilename));
        for (String line : lines) {
            String[] splitted = line.split(";");
            questions.add(splitted[0]);
            answers.add(splitted[1]);
        }
        curQuestion = questions.size() - 1;
    }
}
