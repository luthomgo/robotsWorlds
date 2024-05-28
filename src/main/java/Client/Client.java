package Client;

import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException
    {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            Socket s = new Socket(ip, 5055);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            Scanner scn = new Scanner(System.in);
            String name;
            while (true) {
                System.out.println(dis.readUTF());

                name = scn.nextLine();
                dos.writeUTF(name);
                String response = dis.readUTF();
                if (response.contains("Hello")){
                System.out.println(response);
                break;
                }else {System.out.println(response);}

            }
            while (true) {
                System.out.println(dis.readUTF());
                String command = scn.nextLine();

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

                if(newCommand.equals("Exit"))
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
}