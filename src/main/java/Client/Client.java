package Client;

import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException
    {
        try {
            System.out.println("Please enter your ip Address:");
            Scanner scanner = new Scanner(System.in);
            String ipAddress = scanner.nextLine();
            if (!isValidIpAddress(ipAddress)){
                throw new IOException("Invalid ip address format");
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

                if(name.equalsIgnoreCase("exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                String response = dis.readUTF();
                if (response.contains("Hello")){
                    System.out.println(response);
                    break;
                }else {System.out.println(response);}

            }
            while (true) {
                System.out.println(dis.readUTF());
                String command = scn.nextLine();
                if(command.equalsIgnoreCase("exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

                Request request = new Request(name, command);
                JsonObject toSend = request.createRequest();
                dos.writeUTF(toSend.toString());
                String response = dis.readUTF();
                System.out.println(response);
                if (response.contains("ok")) break;
            }
            while (true){
                System.out.println(dis.readUTF());
                String newCommand = scn.nextLine();
                if(newCommand.equalsIgnoreCase("exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

                Request newRequest = new Request(name,newCommand);
                JsonObject newToSend = newRequest.createRequest();
                dos.writeUTF(newToSend.toString());
                System.out.println(dis.readUTF());

            }
            scn.close();
            dos.close();
            dis.close();
        }catch (IOException ignored){}
    }

    private static boolean isValidIpAddress(String ipAddress){
        String[] ipNum = ipAddress.split("\\.");
        if (ipNum.length != 4){
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
        }catch (NumberFormatException e){
            return false;
        }
    }
}
