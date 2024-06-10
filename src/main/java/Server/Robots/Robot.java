package Server.Robots;

import Server.Commands.Command;
import Server.Server;
import Server.World.Obstacles;
import Server.World.RobotObstacle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import static Server.ServerCommands.ServerCommand.ANSI_RED;
import static Server.ServerCommands.ServerCommand.ANSI_RESET;
import java.util.Random;

public class Robot {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final String name;
    private final String kind;
    public int shield;
    private int shots;
    private Position position;
    private final int visibility ;
    private int reload ;
    private int repair ;
    private Direction direction = Direction.NORTH;
    private String status = "NORMAL";
    private int iShield;
    public int iShot;
    private final Socket client;
    private boolean isReloading = false;
    private boolean isRepairing = false;
    private Position TOP_LEFT ;
    private Position BOTTOM_RIGHT ;
    private  int MaxShield;
    private  int MaxShots;
    private  int WorldVisibily;
    private final List<Obstacles> obstacles;
    private final List<Robot> robotList;

    public Robot(String name, String kind, int shield, int shots, int vis, Socket s,DataOutputStream dos, DataInputStream dis) {
        this.kind = kind;
        this.name = name;

        this.MaxShield = Server.world.getMaxShield();
        this.MaxShots = Server.world.getMaxShots();
        this.WorldVisibily = Server.world.getWorldVisibily();

        if (shots > MaxShots){this.shots = MaxShots;this.iShot = MaxShots;}
        else {this.shots = shots;this.iShot = shots;}

        if (shield > MaxShield) {this.shield = MaxShield;this.iShield = MaxShield;}
        else {this.shield = shield;this.iShield = shield;}

        if (vis > WorldVisibily){this.visibility = WorldVisibily;}
        else this.visibility = vis;

        this.reload = Server.world.getReloadTime();
        this.repair = Server.world.getRepairTime();

        this.obstacles = Server.world.getObstacles();
        this.robotList = Server.world.getRobotList();

        this.TOP_LEFT = Server.world.getTOP_LEFT();
        this.BOTTOM_RIGHT = Server.world.getBOTTOM_RIGHT();

        this.position = genPos();

        this.client = s;
        this.dis = dis;
        this.dos = dos;
    }

    public Robot(String name, String kind, int visibility, List<Obstacles> obstacles, List<Robot> robotList, int shield, int shots, Position position, int reload, int repair, Direction direction, int iShield, int iShot, Position TOP_LEFT, Position BOTTOM_RIGHT, int maxShield, int maxShots, int worldVisibility) {
        this.name = name;
        this.kind = kind;
        this.visibility = visibility;
        this.obstacles = obstacles;
        this.robotList = robotList;
        this.shield = shield;
        this.shots = shots;
        this.position = position;
        this.reload = reload;
        this.repair = repair;
        this.direction = direction;
        this.iShield = iShield;
        this.iShot = iShot;
        this.TOP_LEFT = TOP_LEFT;
        this.BOTTOM_RIGHT = BOTTOM_RIGHT;
        MaxShield = maxShield;
        MaxShots = maxShots;
        WorldVisibily = worldVisibility;
        this.client = null;
        this.dos = null;
        this.dis = null;
    }

    public JsonObject handleCommand(Command command) {
        return command.execute(this);
    }

    public String getName(){
        return this.name;
    }
    public Position getPosition(){
        return this.position;
    }
    public int getVisibility(){
        return this.visibility;
    }
    public Direction getDirection(){
        return this.direction;
    }
    public int getShots() {
        return shots;
    }
    public int getReload() {
        return reload;
    }
    public int getIShield(){return this.iShield;}

    public Position getTOP_LEFT() {
        return TOP_LEFT;
    }

    public Position getBOTTOM_RIGHT() {
        return BOTTOM_RIGHT;
    }

    public List<Obstacles> getObstacles() {
        return obstacles;
    }

    public List<Robot> getRobotList() {
        return robotList;
    }

    public int getShield(){
        return this.shield;
    }

    public Position genPos(){
        while (true){
            Random random1 = new Random();
            int randomY = random1.nextInt(this.BOTTOM_RIGHT.getY(),this.TOP_LEFT.getY());
            Random random2 = new Random();
            int randomX = random2.nextInt(this.TOP_LEFT.getX(),this.BOTTOM_RIGHT.getX());
            Position robotPos = new Position(randomX,randomY);
            int blockCount = 0;
            for (Obstacles ob:this.obstacles){
                if (ob.blocksPosition(robotPos)){
                    blockCount += 1;
                }
            }
            for (Robot rob:this.robotList){
                if (rob.getName().equals(this.name)) continue;;
                Obstacles robOb = new RobotObstacle(rob.getPosition().getX(),rob.getPosition().getY());
                if (robOb.blocksPosition(robotPos)){
                    blockCount += 1;
                }
            }
            if (blockCount == 0) {return robotPos;}
        }
    }

    public void minusShield(){
        if(this.shield > 0){
            this.shield -= 1;
        }else {
            checkIfDead();        }
    }

    public boolean minusShot(){
        if(this.shots > 0){
            this.shots -= 1;
            return true;
        }
        return false;
    }
    public void updateDirection(boolean turnRight) {
        if (turnRight) {
            switch (this.direction) {
                case NORTH -> this.direction = Direction.EAST;
                case EAST -> this.direction = Direction.SOUTH;
                case SOUTH -> this.direction = Direction.WEST;
                case WEST -> this.direction = Direction.NORTH;
            }
        } else {
            switch (this.direction) {
                case NORTH -> this.direction = Direction.WEST;
                case WEST -> this.direction = Direction.SOUTH;
                case SOUTH -> this.direction = Direction.EAST;
                case EAST -> this.direction = Direction.NORTH;
            }
        }
    }



    public JsonObject data(){
        JsonObject data = new JsonObject();

        JsonArray positionL = new JsonArray();
        positionL.add(this.position.getX());
        positionL.add(this.position.getY());
        data.add("position",positionL);
        data.addProperty("visibility",this.visibility);
        data.addProperty("reload",this.reload);
        data.addProperty("repair",this.repair);
        data.addProperty("shields",this.shield);

        return data;
    }

    public void setPosition(Position centre) {
        this.position= centre;
    }

    public JsonObject state(){
        JsonObject state = new JsonObject();
        JsonArray positionL = new JsonArray();
        positionL.add(this.position.getX());
        positionL.add(this.position.getY());
        state.add("position",positionL);
        state.addProperty("direction",this.direction.toString());
        state.addProperty("shields",this.shield);
        state.addProperty("shots",this.shots);
        state.addProperty("status",this.status);
        return state;
    }

    public JsonObject toJSON(){
        JsonObject data = data();
        JsonObject state = state();
        JsonObject response = new JsonObject();
        response.addProperty("result","OK");
        response.add("data",data);
        response.add("state",state);
        return response;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", shield=" + shield +
                ", shots=" + shots +
                '}';
    }

    public boolean updatePosition(int nrSteps) {
        List<Obstacles> temp = new ArrayList<>();
        temp = this.obstacles;

        int newX = this.position.getX();
        int newY = this.position.getY();

        switch (this.direction) {
            case NORTH:
                newY = newY + nrSteps;
                break;
            case EAST:
                newX = newX + nrSteps;
                break;
            case SOUTH:
                newY = newY - nrSteps;
                break;
            case WEST:
                newX = newX - nrSteps;
                break;
        }

        Position newPosition = new Position(newX, newY);
        for (Robot i: this.robotList){
            if (i.getName().equals(this.name))continue;
            Position iPos = i.getPosition();
            Obstacles ob = new RobotObstacle(iPos.getX(), iPos.getY());
            temp.add(ob);
        }

        for (Obstacles robotObs : temp){
            if(!robotObs.getType().equals("robot")){continue;}
            if (robotObs.blocksPath(this.position,newPosition)) {
                temp.clear();
                return false;
            }
        }

        if (Position.Isin(newPosition.getX(), newPosition.getY(),getTOP_LEFT(),getBOTTOM_RIGHT())) {
            for (Obstacles obstacle : this.obstacles) {
                if (obstacle.blocksPath(this.position,newPosition)) {
                    String type = obstacle.getType();
                    if (type.equals("pit")) {
                        pitDeath();}
                    else return false;
                }
            }

            this.position = newPosition;
            return true;
        } else {
            return false;
        }
    }

    public void pitDeath(){
        Server.world.removeRobot(this);
        System.out.println(ANSI_RED+"Robot "+this.name+" has died");
            try {
                DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
                dos.writeUTF(ANSI_RED+"You have fallen into a pit" + ANSI_RESET);
                dos.writeUTF(ANSI_RED+"You have died.\nTry again ;)" + ANSI_RESET);
                this.client.close();
                this.dos.close();
                this.dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void checkIfDead() {
        if (this.shield <= 0 ) {
            Server.world.removeRobot(this);
            System.out.println(ANSI_RED+"Robot "+this.name+" has died" + ANSI_RESET);
                try {
                    DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
                    dos.writeUTF(ANSI_RED+"You're shields have hit 0" + ANSI_RESET);
                    dos.writeUTF(ANSI_RED+"You have died.\nTry again ;)" + ANSI_RESET);
                    this.client.close();
                    this.dos.close();
                    this.dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void repairShields(){
        this.shield = this.iShield;
    }

    public int getRepair() {
        return this.repair;
    }

    public void setRepair(int repair){
        this.repair = repair;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }

    public void setShots(int iShot) {
        this.shots = this.iShot;
    }

    public boolean isRepairing(){
        return this.isRepairing;
    }
    public void setRepairing(boolean repairing){
        this.isRepairing = repairing;
    }

    public boolean isReloading() {

        return this.isReloading;
    }

    public void setReload(boolean reloading){
        this.isReloading = reloading;
    }
}
