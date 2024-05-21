package udpclient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UDPClientGUI extends JFrame {

    private DatagramSocket socket;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private JTextField nameField;
    private JTextArea messageArea;

    public UDPClientGUI() {
        super("UDP Client");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("Server Address:"));
        serverAddressField = new JTextField("localhost", 10);
        panel.add(serverAddressField);

        panel.add(new JLabel("Server Port:"));
        serverPortField = new JTextField("1234", 10);
        panel.add(serverPortField);

        panel.add(new JLabel("Your Name:"));
        nameField = new JTextField(20);
        panel.add(nameField);

        panel.add(new JLabel("Message:"));
        messageArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane);

        JButton sendButton = new JButton("Send Message");
        sendButton.addActionListener(e -> sendMessage());
        panel.add(sendButton);

        add(panel, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void sendMessage() {
        String serverAddressStr = serverAddressField.getText();
        int serverPort = Integer.parseInt(serverPortField.getText());
        String name = nameField.getText();
        String message = messageArea.getText();

        if (name.isEmpty() || message.isEmpty()) {
            showMessage("Please enter your name and message.");
            return;
        }

        try {
            InetAddress serverAddress = InetAddress.getByName(serverAddressStr);
            socket = new DatagramSocket();

            String fullMessage = name + ": " + message;

            byte[] sendData = fullMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);

            showMessage("Sent: " + fullMessage);

            byte[] receiveData = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            showMessage("Received: " + receivedMessage);

        } catch (IOException ex) {
            showMessage("Error: " + ex.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTimestamp = "[" + now.format(formatter) + "]";
            messageArea.append(formattedTimestamp + " " + message + "\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UDPClientGUI::new);
    }
}
