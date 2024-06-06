package Server.World;
import Server.Robots.Position;
import Server.Robots.Robot;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class World {
    private Position TOP_LEFT ;
    private Position BOTTOM_RIGHT ;
    private int WorldVisibily;
    private int RepairTime;
    private int ReloadTime;
    private int MaxShield;
    public List<Obstacles> obstacles = new ArrayList<>();
    public List<Robot> robotList = new ArrayList<>();

    public World(){
        Config c = new Config();
        c.createConfigF();
        c.createFile();
        JsonObject configJSON = c.config();
        c.writeToFile(configJSON);

        Position top = c.readWorldTop(configJSON);
        this.TOP_LEFT = top;

        Position bottom = new Position(top.getX() * -1,top.getY() * -1);
        this.BOTTOM_RIGHT = bottom;

        this.WorldVisibily = c.readWorldVise(configJSON);
        this.RepairTime = c.readWorldRepairTime(configJSON);
        this.ReloadTime= c.readWorldReloadTime(configJSON);
        this.MaxShield = c.readWorldShield(configJSON);

        System.out.println("Started world");
        genObs();
    }

    public void genObs(){
//        this.obstacles.add(new MountainObstacle(5,25));
//        this.obstacles.add(new MountainObstacle(5,7));
//        this.obstacles.add(new MountainObstacle(5,6));
//        this.obstacles.add(new MountainObstacle(2,5));
//        this.obstacles.add(new MountainObstacle(8,5));
    }
    public List<Obstacles> getObstacles() {
        return this.obstacles;
    }

    public void showObstacles() {
        obstacles.forEach(o -> System.out.println(o.toString()));
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

    public Position getTOP_LEFT() {
        return TOP_LEFT;
    }

    public Position getBOTTOM_RIGHT() {
        return BOTTOM_RIGHT;
    }
}

