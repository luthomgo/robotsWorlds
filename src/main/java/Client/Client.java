package Client;


import Server.Server;
import Server.Robots.Robot;
import Server.World.World;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import static Server.ServerCommands.ServerCommand.ANSI_RED;

public class Client {
    // ANSI color codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws IOException {
        try {
            System.out.println("Please enter your IP Address:");
            Scanner scanner = new Scanner(System.in);
            String ipAddress = scanner.nextLine();
            if (!isValidIpAddress(ipAddress)){
                throw new IOException("Invalid IP address format");
            }

            InetAddress ip = InetAddress.getByName(ipAddress);
            Socket s = new Socket(ip, 5055);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            Scanner scn = new Scanner(System.in);
            String name;
            while (true) {
                System.out.println(dis.readUTF());

                name = scn.nextLine();
                dos.writeUTF(name);

                if(name.equalsIgnoreCase("exit")) {
                    System.out.println("Closing this connection: " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                String response = dis.readUTF();
                if (response.contains("Hello")){
                    System.out.println(response);
                    break;
                } else {
                    System.out.println(formatResponse(response));
                }
            }

            while (true) {
                System.out.println(dis.readUTF());
                String command = scn.nextLine();
                Request request = new Request(name, command);
                JsonObject toSend = request.createRequest();
                dos.writeUTF(toSend.toString());
                String response = dis.readUTF();
                System.out.println(formatResponse(response));

                if(command.equalsIgnoreCase("exit")) {
                    System.out.println("Closing this connection: " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;

                }
                if (response.contains("ok")) break;
            }

            while (true) {
                System.out.println(dis.readUTF());
                String newCommand = scn.nextLine();

                Request newRequest = new Request(name, newCommand);
                JsonObject newToSend = newRequest.createRequest();
                dos.writeUTF(newToSend.toString());

                if(newCommand.equalsIgnoreCase("exit")) {
                    System.out.println("Closing this connection: " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                System.out.println(formatResponse(dis.readUTF()));
            }

            scn.close();
            dos.close();
            dis.close();
        } catch (IOException ignored) {}
    }

    private static boolean isValidIpAddress(String ipAddress) {
        String[] ipNum = ipAddress.split("\\.");
        if (ipNum.length != 4) {
            return false;
        }
        try {
            for (String num : ipNum) {
                int ip = Integer.parseInt(num);
                if (ip < 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String formatResponse(String response) {
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        StringBuilder formattedResponse = new StringBuilder();

        if (jsonResponse.has("result")) {
            formattedResponse.append(ANSI_BLUE).append("Result: ").append(ANSI_RESET).append(jsonResponse.get("result").getAsString()).append("\n");
        }
        if (jsonResponse.has("data")) {
            JsonObject data = jsonResponse.getAsJsonObject("data");
            if (data.has("help")) {
                formattedResponse.append(formatHelpData(data.get("help").getAsString()));
            } else {
                formattedResponse.append(ANSI_GREEN).append("Data: ").append(ANSI_RESET).append("\n");
                data.entrySet().forEach(entry -> {
                    formattedResponse.append(ANSI_YELLOW).append("  ").append(entry.getKey()).append(": ").append(ANSI_RESET).append(entry.getValue().toString()).append("\n");
                });
            }
        }
        if (jsonResponse.has("state")) {
            JsonObject state = jsonResponse.getAsJsonObject("state");
            formattedResponse.append(ANSI_GREEN).append("State: ").append(ANSI_RESET).append("\n");
            state.entrySet().forEach(entry -> {
                formattedResponse.append(ANSI_YELLOW).append("  ").append(entry.getKey()).append(": ").append(ANSI_RESET).append(entry.getValue().toString()).append("\n");
            });
        }

        return formattedResponse.toString();
    }
    private static String formatFireResponse(JsonObject jsonResponse) {
        StringBuilder formattedFireResponse = new StringBuilder();

        formattedFireResponse.append(ANSI_CYAN).append("Fire Command Response:").append(ANSI_RESET).append("\n");
        JsonObject state = jsonResponse.getAsJsonObject("state");
        formattedFireResponse.append(ANSI_GREEN).append("State: ").append(ANSI_RESET).append("\n");
        state.entrySet().forEach(entry -> {
            formattedFireResponse.append(ANSI_YELLOW).append("  ").append(entry.getKey()).append(": ").append(ANSI_RESET).append(entry.getValue().toString()).append("\n");
        });

        String result = jsonResponse.get("result").getAsString();
        if (result.equals("miss")) {
            formattedFireResponse.append(ANSI_RED).append("Missed the target!").append(ANSI_RESET).append("\n");
        } else {
            formattedFireResponse.append(ANSI_GREEN).append("Hit the target!").append(ANSI_RESET).append("\n");
        }

        return formattedFireResponse.toString();
    }

    private static String formatHelpData(String helpText) {
        String[] commands = helpText.split("\n");
        StringBuilder formattedHelp = new StringBuilder();
        formattedHelp.append(ANSI_CYAN).append("Available Commands:").append(ANSI_RESET).append("\n");
        for (String command : commands) {
            formattedHelp.append(ANSI_GREEN).append("  ").append(command).append(ANSI_RESET).append("\n");
        }
        return formattedHelp.toString();
    }
}
