package Server.World;

import Server.Robots.Position;
import Server.Server;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {
    private String homeP;
    private String configP;
    private File worldFile;
    private int WorldX;
    private int WorldY;
    private JsonObject configJSON;
    private boolean configCreated = false;

    public Config(){
        this.homeP = System.getProperty("user.home");
        this.configP = setConfigP();
    }
    public File getWorldFile(){
        return this.worldFile;
    }
    public String getHomeP(){
        return this.homeP;
    }

    public String setConfigP(){
            String configName = ".RobotWorldsConfig";
        String configPath = getHomeP() + "/" + configName;
            this.configP = configPath;
            return configPath;
    }

    public void createConfigF(){
        File dir = new File(this.configP);
        boolean worldConfig = dir.mkdir();
        if (worldConfig) {System.out.println("created config file at " + configP);configCreated =true;}
        else {System.out.println("Failed to create config it already exists");configCreated=true;}
    }

    public void createFile(){
        try {
            File myObj = new File(configP + "/" + "WorldConfig.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            this.worldFile = myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToFile(JsonObject config){
        try {
            FileWriter writer = new FileWriter(this.worldFile);
            writer.append(config.toString());
            writer.close();
        } catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public JsonObject config(){
        JsonObject config = new JsonObject();
        Scanner s = new Scanner(System.in);
        int worldX;
        int worldY;
        int worldVis;
        int worldReloadTime;
        int worldRepairTime;
        int worldShield;
        while (true){
            System.out.println("Enter world x size");
            worldX = Integer.parseInt(s.nextLine());
            if (worldX >= 30 && worldX <= 500)break;
            else System.out.println("X cant be smaller than 30 or bigger than 500");
        }
        while (true) {
            System.out.println("Enter world y size");
            worldY = Integer.parseInt(s.nextLine());
            if (worldY >= 30 && worldY <= 500)break;
            else System.out.println("Y cant be smaller than 30 or bigger than 500");
        }
        while (true) {
            System.out.println("Enter Robot visibility range");
            worldVis = Integer.parseInt(s.nextLine());
            if (worldVis >= 0 && worldVis <= 20)break;
            else System.out.println("World visibility cant be smaller than 0 or bigger than 20");
        }
        while (true) {
            System.out.println("Enter world repair time");
            worldRepairTime = Integer.parseInt(s.nextLine());
            if (worldRepairTime >= 3 && worldRepairTime <= 10)break;
            else System.out.println("World repair time cant be smaller than 3 or bigger than 10");
        }
        while (true) {
            System.out.println("Enter world reload time");
            worldReloadTime = Integer.parseInt(s.nextLine());
            if (worldReloadTime >= 3 && worldReloadTime <= 10)break;
            else System.out.println("World reload time cant be smaller than 3 or bigger than 10");
        }
        while (true) {
            System.out.println("Enter world shield strength");
            worldShield = Integer.parseInt(s.nextLine());
            if (worldShield >= 0 && worldShield <= 10)break;
            else System.out.println("World shield strength cant be smaller than 0 or bigger than 10");
        }
        config.addProperty("World x",worldX);
        config.addProperty("World y",worldY);
        config.addProperty("WorldVis",worldVis);
        config.addProperty("WorldReload",worldReloadTime);
        config.addProperty("WorldRepair",worldRepairTime);
        config.addProperty("WorldShield",worldShield);

        return config;
    }

    public Position readWorldTop(JsonObject configJSON){
        Position top = null;
        try {
            Scanner myReader = new Scanner(worldFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                JsonObject configData = JsonParser.parseString(data).getAsJsonObject();
                int x = configData.get("World x").getAsInt();
                int y = configData.get("World y").getAsInt();
                top = new Position(x * -1,y);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return top;
    }
    public int readWorldShield(JsonObject configJSON){
        int shield = 0;
        try {
            Scanner reader = new Scanner(worldFile);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                JsonObject configData = JsonParser.parseString(data).getAsJsonObject();
                shield = configData.get("WorldShield").getAsInt();
            }
            reader.close();
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return shield;
    }
    public int readWorldRepairTime(JsonObject configJSON){
        int repairTime = 0;
        try {
            Scanner reader = new Scanner(worldFile);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                JsonObject configData = JsonParser.parseString(data).getAsJsonObject();
                repairTime = configData.get("WorldRepair").getAsInt();
            }
            reader.close();
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return repairTime;
    }

    public int readWorldReloadTime(JsonObject configJSON){
        int reload = 0;
        try {
            Scanner reader = new Scanner(worldFile);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                JsonObject configData = JsonParser.parseString(data).getAsJsonObject();
                reload = configData.get("WorldReload").getAsInt();
            }
            reader.close();
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return reload;
    }
    public int readWorldVise(JsonObject configJSON){
        int vis = 0;
        try {
            Scanner reader = new Scanner(worldFile);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                JsonObject configData = JsonParser.parseString(data).getAsJsonObject();
                vis = configData.get("WorldVis").getAsInt();
            }
            reader.close();
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return vis;
    }

    public JsonObject getConfigJSON() {
        return configJSON;
    }

    public boolean isConfigCreated() {
        return configCreated;
    }
}
