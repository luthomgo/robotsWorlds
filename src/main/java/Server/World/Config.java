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
    /**
     * Retrieves the world configuration file.
     *
     * @return The world configuration file as a {@code File} object.
     */
    private final String homeP;
    private String configP;
    private File worldFile;
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
        if (worldConfig) {configCreated =true;}
        else {configCreated=true;}
    }

    public void createFile(){
        File myObj = new File(configP + "/" + "WorldConfig.txt");
        this.worldFile = myObj;
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
        int worldPlayers;
        int worldShots;

        while (true){
            System.out.println("Enter world x size");
            try {
                worldX = Integer.parseInt(s.nextLine());
                if (worldX >= 30 && worldX <= 500) break;
                else System.out.println("X cant be smaller than 30 or bigger than 500");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter world y size");
            try{
                worldY = Integer.parseInt(s.nextLine());
                if (worldY >= 30 && worldY <= 500)break;
                else System.out.println("Y cant be smaller than 30 or bigger than 500");
            }catch (NumberFormatException e) {System.out.println("X cant be smaller than 30 or bigger than 500");}
        }
        while (true) {
            System.out.println("Enter Robot visibility range");
            try{
                worldVis = Integer.parseInt(s.nextLine());
                if (worldVis >= 0 && worldVis <= 20)break;
                else System.out.println("World visibility cant be smaller than 0 or bigger than 20");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter world repair time");
            try{
                worldRepairTime = Integer.parseInt(s.nextLine());
                if (worldRepairTime >= 3 && worldRepairTime <= 10)break;
                else System.out.println("World repair time cant be smaller than 3 or bigger than 10");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter world reload time");
            try{
                worldReloadTime = Integer.parseInt(s.nextLine());
                if (worldReloadTime >= 3 && worldReloadTime <= 10)break;
                else System.out.println("World reload time cant be smaller than 3 or bigger than 10");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter world shield strength");
            try{
                worldShield = Integer.parseInt(s.nextLine());
                if (worldShield >= 0 && worldShield <= 10)break;
                else System.out.println("World shield strength cant be smaller than 0 or bigger than 10");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter world amount of shots");
            try{
                 worldShots = Integer.parseInt(s.nextLine());
                if (worldShots >= 1 && worldShots <= 5)break;
                else System.out.println("World shots cant be smaller than 1 or bigger than 5");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        while (true) {
            System.out.println("Enter amount of players");
            try{
                worldPlayers = Integer.parseInt(s.nextLine());
                if (worldPlayers >= 0 && worldPlayers <= 6)break;
                else System.out.println("amount of players must be more that 0 and less than 6");
            }catch (NumberFormatException e) {System.out.println("enter a number");}
        }
        config.addProperty("World x",worldX);
        config.addProperty("World y",worldY);
        config.addProperty("WorldVis",worldVis);
        config.addProperty("WorldReload",worldReloadTime);
        config.addProperty("WorldRepair",worldRepairTime);
        config.addProperty("WorldShield",worldShield);
        config.addProperty("WorldShots",worldShots);
        config.addProperty("WorldPlayers",worldPlayers);
        return config;
    }

    public Position readWorldTop(JsonObject configJSON){
        Position top = null;
        int x = configJSON.get("World x").getAsInt();
        int y = configJSON.get("World y").getAsInt();
        top = new Position(x * -1,y);
        return top;
    }
    public int readWorldShield(JsonObject configJSON){
        int shield = 0;
        shield = configJSON.get("WorldShield").getAsInt();
        return shield;
    }
    public int readWorldShots(JsonObject configJSON){
        int shots = 0;
        shots= configJSON.get("WorldShots").getAsInt();
        return shots;
    }
    public int readWorldPlayers(JsonObject configJSON){
        int players = 0;
        players = configJSON.get("WorldPlayers").getAsInt();
        return players;
    }
    public int readWorldRepairTime(JsonObject configJSON){
        int repairTime = 0;
        repairTime = configJSON.get("WorldRepair").getAsInt();
        return repairTime;
    }

    public int readWorldReloadTime(JsonObject configJSON){
        int reload = 0;
        reload = configJSON.get("WorldReload").getAsInt();
        return reload;
    }
    public int readWorldVise(JsonObject configJSON){
        int vis = 0;
        vis = configJSON.get("WorldVis").getAsInt();
        return vis;
    }
}
