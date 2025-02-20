package Server.World;
import Server.Robots.Position;
import Server.Robots.Robot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class World {
    private Position TOP_LEFT ;
    private Position BOTTOM_RIGHT ;
    private final int WorldVisibily;
    private final int RepairTime;
    private final int ReloadTime;
    private final int MaxShield;
    private final int MaxShots;
    private final int Players;
    public List<Obstacles> obstacles = new ArrayList<>();
    public List<Robot> robotList = new ArrayList<>();
    private JsonObject con;

    public World(){
        /**
         * Constructs a World instance. If a configuration file exists, it loads the configuration;
         * otherwise, it creates a new configuration file.
         */
        String filePath = System.getProperty("user.home") + "/.RobotWorldsConfig/WorldConfig.txt";
        Path path = Paths.get(filePath);
        Config c = new Config();

        if (Files.exists(path)) {
            String a;
            while (true) {
                Scanner s = new Scanner(System.in);
                System.out.println("Config file exists would you like to reconfigure? y/n");
                a = s.nextLine();
                if (a.equalsIgnoreCase("yes") || a.equalsIgnoreCase("y") || a.equalsIgnoreCase("no") || a.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Respond with yes/y/no/n");
                }
            }
            if (a.equalsIgnoreCase("y")) {
                c.createConfigF();
                c.createFile();
                JsonObject configJSON = c.config();
                c.writeToFile(configJSON);

                Position top = c.readWorldTop(configJSON);
                this.TOP_LEFT = top;
                this.BOTTOM_RIGHT = new Position(top.getX() * -1, top.getY() * -1);

                this.WorldVisibily = c.readWorldVise(configJSON);
                this.RepairTime = c.readWorldRepairTime(configJSON);
                this.ReloadTime = c.readWorldReloadTime(configJSON);
                this.MaxShield = c.readWorldShield(configJSON);
                this.MaxShots = c.readWorldShots(configJSON);
                this.Players = c.readWorldPlayers(configJSON);
            } else {
                try {
                    String line = Files.readString(path);
                    this.con = JsonParser.parseString(line).getAsJsonObject();

                } catch (IOException e) {
                    System.err.println("An error occurred while reading the file: " + e.getMessage());
                }

                Position top = c.readWorldTop(con);
                this.TOP_LEFT = top;
                this.BOTTOM_RIGHT = new Position(top.getX() * -1, top.getY() * -1);

                this.WorldVisibily = c.readWorldVise(con);
                this.RepairTime = c.readWorldRepairTime(con);
                this.ReloadTime = c.readWorldReloadTime(con);
                this.MaxShield = c.readWorldShield(con);
                this.MaxShots = c.readWorldShots(con);
                this.Players = c.readWorldPlayers(con);
            }
        } else {

            c.createConfigF();
            c.createFile();
            JsonObject configJSON = c.config();
            c.writeToFile(configJSON);

            Position top = c.readWorldTop(configJSON);
            this.TOP_LEFT = top;
            this.BOTTOM_RIGHT = new Position(top.getX() * -1,top.getY() * -1);

            this.WorldVisibily = c.readWorldVise(configJSON);
            this.RepairTime = c.readWorldRepairTime(configJSON);
            this.ReloadTime = c.readWorldReloadTime(configJSON);
            this.MaxShield = c.readWorldShield(configJSON);
            this.MaxShots = c.readWorldShots(configJSON);
            this.Players = c.readWorldPlayers(configJSON);
        }

        System.out.println("Started world");
        genObs();
    }

    public void genObs(){
        /**
         * Generates obstacles in the world based on the world size.
         * It creates lakes, mountains, and pits at random positions within the world bounds.
         */
        int xSize = this.getBOTTOM_RIGHT().getX() * 2;
        int ySize = this.getTOP_LEFT().getY() * 2;
        int totalSize = xSize + ySize;

        int lakes = totalSize / 100 + 2;
        int lc = 0;
        int mountains = totalSize / 100 * 2;
        int mc = 0;
        int pits = totalSize / 100;
        int pc = 0;

        while (lc < lakes){
            Random r1 = new Random();
            int x = r1.nextInt(TOP_LEFT.getX(),BOTTOM_RIGHT.getX());
            Random r2 = new Random();
            int y = r2.nextInt(BOTTOM_RIGHT.getY(),TOP_LEFT.getY());
            LakesObstacles l = new LakesObstacles(x,y);
            int obCount = 0;
            for (Obstacles i:obstacles){
                Position obsPosBottom = new Position(l.getBottomLeftX(),l.getBottomLeftY());
                Position obsPosTop = new Position(l.getBottomLeftX() + l.getSize(),l.getBottomLeftY() + l.getSize());
                if (i.blocksPosition(obsPosBottom)) obCount += 1;
                if (i.blocksPosition(obsPosTop)) obCount += 1;
            }
            if (obCount == 0) {
                this.obstacles.add(l);
            lc += 1;}
        }

        while (mc < mountains){
            Random r1 = new Random();
            int x = r1.nextInt(TOP_LEFT.getX(),BOTTOM_RIGHT.getX());
            Random r2 = new Random();
            int y = r2.nextInt(BOTTOM_RIGHT.getY(),TOP_LEFT.getY());
            MountainObstacle l = new MountainObstacle(x,y);
            int obCount = 0;
            for (Obstacles i:obstacles){
                Position obsPosBottom = new Position(l.getBottomLeftX(),l.getBottomLeftY());
                Position obsPosTop = new Position(l.getBottomLeftX() + l.getSize(),l.getBottomLeftY() + l.getSize());
                if (i.blocksPosition(obsPosBottom)) obCount += 1;
                if (i.blocksPosition(obsPosTop)) obCount += 1;
            }
            if (obCount == 0) {
                this.obstacles.add(l);
                mc += 1;}
        }

        while (pc < pits){
            Random r1 = new Random();
            int x = r1.nextInt(TOP_LEFT.getX(),BOTTOM_RIGHT.getX());
            Random r2 = new Random();
            int y = r2.nextInt(BOTTOM_RIGHT.getY(),TOP_LEFT.getY());
            PitsObstacle l = new PitsObstacle(x,y);
            int obCount = 0;
            for (Obstacles i:obstacles){
                Position obsPosBottom = new Position(l.getBottomLeftX(),l.getBottomLeftY());
                Position obsPosTop = new Position(l.getBottomLeftX() + l.getSize(),l.getBottomLeftY() + l.getSize());
                if (i.blocksPosition(obsPosBottom)) obCount += 1;
                if (i.blocksPosition(obsPosTop)) obCount += 1;
            }
            if (obCount == 0) {
                this.obstacles.add(l);
                pc += 1;}
        }
    }
    public List<Obstacles> getObstacles() {
        return this.obstacles;
    }

    public List<Robot> getRobotList() {
        return robotList;
    }

    public void setTOP_LEFT(Position newTop){
        this.TOP_LEFT = newTop;
    }
    public void setBOTTOM_RIGHT(Position newBottom){
        this.BOTTOM_RIGHT = newBottom;
    }

    public int getWorldVisibily() {
        return WorldVisibily;
    }

    public int getRepairTime() {
        return RepairTime;
    }

    public int getReloadTime() {
        return ReloadTime;
    }

    public int getMaxShield() {
        return MaxShield;
    }

    public int getMaxShots() {
        return MaxShots;
    }

    public int getPlayers() {
        return Players;
    }

    public Position getTOP_LEFT() {
        return TOP_LEFT;
    }

    public Position getBOTTOM_RIGHT() {
        return BOTTOM_RIGHT;
    }
}

