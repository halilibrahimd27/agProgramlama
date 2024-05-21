package tcpclient;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClientGUI extends JFrame {

    private JTextArea textArea;
    private PrintWriter out;
    private Scanner in;

    public TCPClientGUI() {
        setTitle("TCP İstemci");
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

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 1234);
            appendToTextArea("Sunucuya bağlanıldı.");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());

            new Thread(this::receiveMessages).start(); // Sunucudan gelen mesajları dinlemek için thread başlat
        } catch (IOException e) {
            appendToTextArea("Sunucuya bağlanılamadı: " + e.getMessage());
        }
    }

    private void receiveMessages() {
        while (true) {
            if (in.hasNextLine()) {
                String serverMessage = in.nextLine();
                appendToTextArea("Sunucu: " + serverMessage);
            }
        }
    }

    private void sendMessage(String message) {
        out.println(message);
        appendToTextArea("İstemci: " + message);
    }

    private void appendToTextArea(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TCPClientGUI clientGUI = new TCPClientGUI();
            clientGUI.setVisible(true);
        });
    }
}
