import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // Set to hold all active client threads
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start(); // Accept client and start new handler thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle each client connection
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Setup input and output streams for the client
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientWriters) {
                    clientWriters.add(out); // Add the writer to the set of active clients
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(message); // Send the message to all connected clients
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    synchronized (clientWriters) {
                        clientWriters.remove(out); // Remove the client from the set of active clients
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
