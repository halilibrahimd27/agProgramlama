package tcpserver;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServerGUI extends JFrame {

    private JTextArea textArea;
    private PrintWriter out;
    private Scanner in;

    public TCPServerGUI() {
        setTitle("TCP Sunucu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JTextField messageField = new JTextField(20);
        JButton sendButton = new JButton("Gönder");
        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            sendMessage(message);
            messageField.setText("");
        });
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        add(bottomPanel, BorderLayout.SOUTH);

        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            appendToTextArea("Sunucu başlatıldı. Bağlantı bekleniyor...");

            Socket clientSocket = serverSocket.accept();
            appendToTextArea("İstemci bağlandı: " + clientSocket);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());

            new Thread(this::receiveMessages).start(); // İstemciden gelen mesajları dinlemek için thread başlat
        } catch (IOException e) {
            appendToTextArea("Bağlantı hatası: " + e.getMessage());
        }
    }

    private void receiveMessages() {
        while (true) {
            if (in.hasNextLine()) {
                String clientMessage = in.nextLine();
                appendToTextArea("İstemci: " + clientMessage);
            }
        }
    }

    private void sendMessage(String message) {
        out.println(message);
        appendToTextArea("Sunucu: " + message);
    }

    private void appendToTextArea(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TCPServerGUI serverGUI = new TCPServerGUI();
            serverGUI.setVisible(true);
        });
    }
}
