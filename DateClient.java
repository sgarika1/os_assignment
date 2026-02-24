import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DateClientModified {

    private static final String HOST = "172.16.58.53"; // change if needed
    private static final int SERVER_PORT = 6013;

    public static void main(String[] args) {

        try (
            Socket clientSocket = new Socket(HOST, SERVER_PORT);
            BufferedReader serverInput =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter serverOutput =
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Scanner userInput = new Scanner(System.in)
        ) {

            System.out.println("Connected to server...");

            // Thread to receive messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = serverInput.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Server connection closed.");
                }
            });

            receiveThread.setDaemon(true);
            receiveThread.start();

            // Sending messages to server
            boolean running = true;
            while (running) {
                String message = userInput.nextLine();

                serverOutput.write(message);
                serverOutput.newLine();
                serverOutput.flush();

                if (message.equalsIgnoreCase("exit") ||
                    message.equalsIgnoreCase("bye")) {
                    running = false;
                }
            }

        } catch (IOException ex) {
            System.out.println("Unable to connect: " + ex.getMessage());
        }
    }
}
