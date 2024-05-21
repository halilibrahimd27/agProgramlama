import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServerGUI {
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();
    private JFrame frame;
    private JTextArea textArea;
    private JTextField portField;
    private JButton startButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServerGUI::new);
    }

    public ChatServerGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        portField = new JTextField();
        portField.setText("12345");  // Varsayılan port
        panel.add(new JLabel("Port: "), BorderLayout.WEST);
        panel.add(portField, BorderLayout.CENTER);

        startButton = new JButton("Start Server");
        startButton.addActionListener(e -> startServer());
        panel.add(startButton, BorderLayout.EAST);

        frame.add(panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public void startServer() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            textArea.append("Invalid port number\n");
            return;
        }

        startButton.setEnabled(false);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                textArea.append("Server is listening on port " + port + "\n");

                while (true) {
                    Socket socket = serverSocket.accept();
                    textArea.append("New client connected\n");

                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                textArea.append("Error starting server: " + e.getMessage() + "\n");
                startButton.setEnabled(true);
            }
        }).start();
    }

    public void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != excludeUser) {
                clientHandler.sendMessage(message);
            }
        }
        textArea.append(message + "\n");
    }

    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;
        private ChatServerGUI server;
        private String userName;

        public ClientHandler(Socket socket, ChatServerGUI server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);

                printUsers();

                userName = reader.readLine();
                server.broadcast(userName + " has joined", this);

                String serverMessage;
                do {
                    serverMessage = reader.readLine();
                    server.broadcast("[" + userName + "]: " + serverMessage, this);

                } while (serverMessage != null && !serverMessage.equalsIgnoreCase("bye"));

                server.removeClient(this);
                socket.close();

                server.broadcast(userName + " has left.", this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void printUsers() {
            if (clientHandlers.isEmpty()) {
                writer.println("No other users connected");
            } else {
                writer.println("Connected users: " + clientHandlers.size());
            }
        }

        void sendMessage(String message) {
            writer.println(message);
        }
    }
}
