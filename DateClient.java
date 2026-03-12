import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MessengerClient {

    private static final String SERVER_IP = "172.16.42.102"; // Server IP
    private static final int SERVER_PORT = 6013;              // Server Port

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader serverInput =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter serverOutput =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Scanner userInput = new Scanner(System.in)
        ) {

            System.out.println("Connected to Messenger Server");

            // Thread to receive messages from server
            Thread receiveThread = new Thread(() -> {

                try {

                    String message;

                    while ((message = serverInput.readLine()) != null) {
                        System.out.println(message);
                    }

                } catch (IOException e) {

                    System.out.println("Server connection closed.");

                }

            });

            receiveThread.start();

            // Sending messages to server
            while (true) {

                System.out.println("\nCommands:");
                System.out.println("LIST - Get client list");
                System.out.println("BROADCAST <message>");
                System.out.println("MSG <clientName> <message>");
                System.out.println("EXIT - Quit");

                String message = userInput.nextLine();

                serverOutput.write(message);
                serverOutput.newLine();
                serverOutput.flush();

                if (message.equalsIgnoreCase("EXIT")) {
                    break;
                }
            }

        } catch (IOException e) {

            System.out.println("Unable to connect to server: " + e.getMessage());

        }
    }
}
