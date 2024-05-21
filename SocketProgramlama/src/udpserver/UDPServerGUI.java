package udpserver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UDPServerGUI extends JFrame {
    private JTextArea chatArea;
    private DatagramSocket socket;

    public UDPServerGUI(int port) {
        super("UDP Server");

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
        	displayError(getFormattedTimestamp() + " Soket Bağlantı Hatası: " + ex.getMessage());
            return;
        }

        chatArea = new JTextArea(15, 30);
        chatArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);

        receiveMessages();
    }

    private void receiveMessages() {
        while (true) {
            try {
                byte[] buffer = new byte[256];
                DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(inPacket);

                String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());

                InetAddress clientAddress = inPacket.getAddress();
                int clientPort = inPacket.getPort();

                chatArea.append(clientAddress + ":" + clientPort + "> Mesaj alındı: " + receivedMessage + "\n");

                String replyMessage = receivedMessage.toUpperCase();
                DatagramPacket outPacket = new DatagramPacket(replyMessage.getBytes(), replyMessage.length(), clientAddress, clientPort);
                socket.send(outPacket);

            } catch (IOException ex) {
                displayError(getFormattedTimestamp() + " Hata: " + ex.getMessage());
            }
        }
    }

    
    private void displayError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private String getFormattedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + now.format(formatter) + "]";
    }

    public static void main(String[] args) {
        String portStr = JOptionPane.showInputDialog("Bağlantı kurulacak port numarasını giriniz:");
        int port = Integer.parseInt(portStr);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UDPServerGUI(port);
            }
        });
    }
}
