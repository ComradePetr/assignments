package ru.spbau.mit;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServerImpl implements GameServer {
    private final Game game;

    public GameServerImpl(String gameClassName, Properties properties) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> gameClass = Class.forName(gameClassName);
        game = (Game) gameClass.getConstructor(GameServer.class).newInstance(this);
        for (String key : properties.stringPropertyNames()) {
            String setterName = "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1),
                    value = properties.getProperty(key);
            try {
                int valueInt = Integer.parseInt(value);
                gameClass.getMethod(setterName, int.class).invoke(game, valueInt);
            } catch (NumberFormatException e) {
                gameClass.getMethod(setterName, String.class).invoke(game, value);
            }
        }
    }

    private CopyOnWriteArrayList<LinkedBlockingQueue<String>> messagesQueue = new CopyOnWriteArrayList<>();

    @Override
    public void accept(final Connection connection) {
        final int id = messagesQueue.size();
        final String ids = Integer.toString(id);
        messagesQueue.add(new LinkedBlockingQueue<String>());

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = messagesQueue.get(id).take();
                        //System.out.printf("Send to %s -- '%s'\n", ids, message);
                        connection.send(message);
                    } catch (InterruptedException e) {
                        throw new RuntimeException();
                    }
                }
            }
        });
        sender.start();

        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String message = null;
                    try {
                        synchronized (connection) {
                            if (connection.isClosed()) {
                                return;
                            }
                            message = connection.receive(1);
                        }
                    } catch (InterruptedException | IllegalStateException e) {
                        return;
                    }

                    if (message != null) {
                        //System.out.printf("Server got from %s: %s\n", ids, message);
                        game.onPlayerSentMsg(ids, message);
                    }
                }
            }
        });
        receiver.start();

        sendTo(id, ids);
        game.onPlayerConnected(ids);
    }

    @Override
    public void broadcast(String message) {
        for (int i = 0; i < messagesQueue.size(); i++) {
            sendTo(i, message);
        }
    }

    @Override
    public void sendTo(String id, String message) {
        sendTo(Integer.parseInt(id), message);
    }

    public void sendTo(final int id, final String message) {
        try {
            messagesQueue.get(id).put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
