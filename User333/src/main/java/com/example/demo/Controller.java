package com.example.demo;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Component
public class Controller {

    @Autowired
    private final PubsubOutboundGateway gateway;

    @Value("${topics.sport.enabled}")
    private String sportEnabled;

    @Value("${topics.news.enabled}")
    private String newsEnabled;

    @Value("${topics.cooking.enabled}")
    private String cookingEnabled;

    static String publishedMessage = "";

    private long tokenReceivedAt = 0;
    private String inputText = "";

    public Controller(PubsubOutboundGateway gateway) {
        this.gateway = gateway;
    }

    public void beginCommunication() throws IOException {
        var sender = createSenderSocket();
        var receiver = createReceiverSocket();

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            receiver.read(buffer);
            buffer.flip();
            if (buffer.get(0) != 0) {
                System.out.println("User 3 received token from user 2");
                tokenReceivedAt = System.currentTimeMillis();
                createInputDialog();
            }
            if (tokenReceivedAt != 0 && System.currentTimeMillis() - tokenReceivedAt > 10000) {
                System.out.println("We need to send token to user 1 ");
                this.determineTopicAndSendMessage();
                ByteBuffer tokenBuffer = ByteBuffer.wrap("Alex".getBytes());
                sender.write(tokenBuffer);
                tokenReceivedAt = 0;
            } else {
                ByteBuffer emptyBuffer = ByteBuffer.allocate(1024);
                sender.write(emptyBuffer);
            }
            buffer.clear();
        }
    }

    private void determineTopicAndSendMessage() {
        if (!StringUtil.isNullOrEmpty(inputText)) {
            var stringList = inputText.split(":");
            publishedMessage = stringList[1];
            switch (stringList[0]) {
                case "Sport":
                    if (sportEnabled.equals("true")) {
                        gateway.sendToSportTopic(publishedMessage);
                    } else {
                        System.out.println("This User doesn't have access to Sport Topic");
                    }
                    break;
                case "Cooking":
                    if (cookingEnabled.equals("true")) {
                        gateway.sendToCookingTopic(publishedMessage);
                    } else {
                        System.out.println("This User doesn't have access to Cooking Topic");
                    }
                    break;
                case "News":
                    if (newsEnabled.equals("true")) {
                        gateway.sendToNewsTopic(publishedMessage);
                    } else {
                        System.out.println("This User doesn't have access to News Topic");
                    }
                    break;
                default:
            }
        }
    }

    private void createInputDialog() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("User3");
            frame.setSize(700, 300);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            JTextPane textPane = new JTextPane();
            JScrollPane scrollPane = new JScrollPane(textPane);
            frame.getContentPane().add(scrollPane);

            frame.setVisible(true);
            Timer timer = new Timer(8000, e -> {
                frame.setVisible(false);
                inputText = textPane.getText();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private SocketChannel createReceiverSocket() throws IOException {
        ServerSocketChannel serverSocket;
        SocketChannel client;
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress("localhost", 1002));
        System.out.println("Waiting for user 2 to connect");
        client = serverSocket.accept();
        System.out.println("User 2 connected");

        return client;
    }

    private SocketChannel createSenderSocket() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 1000);
        socketChannel.connect(socketAddr);
        return socketChannel;
    }

}
