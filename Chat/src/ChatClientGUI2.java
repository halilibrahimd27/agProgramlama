import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI2 {
    private String hostname;
    private int port;
    private String userName;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;

    public ChatClientGUI2(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        textField = new JTextField();
        frame.add(textField, BorderLayout.SOUTH);

        sendButton = new JButton("Send");
        frame.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    private void sendMessage() {
        String text = textField.getText();
        if (!text.trim().isEmpty()) {
            writer.println(userName + ": " + text);
            textArea.append("Me: " + text + "\n");  // Mesajı kendi ekranına ekle
            textField.setText("");
        }
    }

    public void execute() {
        try {
            socket = new Socket(hostname, port);

            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            new ReadThread().start();

            userName = JOptionPane.showInputDialog(frame, "Enter your name:");
            while (userName == null || userName.trim().isEmpty() || userName.length() < 5) {
                userName = JOptionPane.showInputDialog(frame, "Name must be at least 5 characters. Enter your name:");
            }
            writer.println(userName);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error connecting to the server: " + ex.getMessage());
        }
    }

    private class ReadThread extends Thread {
        public void run() {
            while (true) {
                try {
                    String response = reader.readLine();
                    if (response == null) {
                        break;
                    }
                    textArea.append(response + "\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String hostname = JOptionPane.showInputDialog("Enter server IP:");
            int port = Integer.parseInt(JOptionPane.showInputDialog("Enter server port:"));
            ChatClientGUI client = new ChatClientGUI(hostname, port);
            client.execute();
        });
    }
}
