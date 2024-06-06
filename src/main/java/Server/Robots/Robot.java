package Server.Robots;

import Client.Client;
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

public class Robot {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Position centre = new Position(5,5);
    private String name;
    private String kind;
    private int shield;
    private int shots;
    private Position position;
    private int fireDistance = 20;
    private int visibility ;
    private int reload ;
    private int repair ;
    private Direction direction = Direction.NORTH;
    private String status = "NORMAL";
    private int iShield;
    public int iShot;
    private int maxShots;
    private Socket client;
    private boolean isReloading = false;
    private boolean isRepairing = false;

    public int getReload() {
        return reload;
    }
    public void minusShield(){
        if(this.shield > 0){
            this.shield -= 1;
        }else {
            System.out.println("dies");
            //abdul dies
        }
    }

    public boolean minusShot(){
        if(this.shots > 0){
            this.shots -= 1;
            return true;
        }
        return false;
    }


    public Robot(String name, String kind, int shield, int shots, int vis, Socket s,DataOutputStream dos, DataInputStream dis) {
        this.kind = kind;
        this.name = name;
        this.shots = shots;this.iShot = shots;
        if (shield > Server.world.getMaxShield()) {this.shield = Server.world.getMaxShield();this.iShield = Server.world.getMaxShield();}
        else this.shield = shield;this.iShield = shield;
        this.reload = Server.world.getReloadTime();
        this.repair = Server.world.getRepairTime();
        if (vis > Server.world.getWorldVisibily()) this.visibility = Server.world.getWorldVisibily();
        else this.visibility = vis;
        this.position = centre;
        this.client = s;
        this.dis = dis;
        this.dos = dos;
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

    public int getMaxShots() {
        return maxShots;
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
        temp = Server.world.getObstacles();

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
        checkIfDead();
        for (Robot i: Server.world.robotList){
            if (i.getName().equals(this.name))continue;
            Position ipos = i.getPosition();
            Obstacles ob = new RobotObstacle(ipos.getX(), ipos.getY());
            temp.add(ob);
        }

        for (Obstacles robotObs : temp){
            if(!robotObs.getType().equals("robot")){continue;}
            if (robotObs.blocksPath(this.position,newPosition)) {
                temp.clear();
                return false;
            }
        }

        if (Position.Isin(newPosition.getX(), newPosition.getY())) {
            for (Obstacles obstacle : Server.world.obstacles) {
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
        System.out.println(ANSI_RED+"Robot "+this.name+" has died"+ANSI_RESET);
            try {
                DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
                dos.writeUTF(ANSI_RED+"You have fallen into a pit"+ANSI_RESET);
                dos.writeUTF(ANSI_RED+"You have died.\nTry again ;)"+ANSI_RESET);
                this.client.close();
                this.dos.close();
                this.dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    public void checkIfDead() {
        if (this.shield == 0 ) {
            Server.world.removeRobot(this);
            System.out.println(ANSI_RED+"Robot "+this.name+" has died"+ANSI_RESET);
                try {
                    DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
                    dos.writeUTF(ANSI_RED+"You're shields have hit 0"+ANSI_RESET);
                    dos.writeUTF(ANSI_RED+"You have died.\nTry again ;)"+ANSI_RESET);
                    this.client.close();
                    this.dos.close();
                    this.dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    public void repairShields(){
        this.shield = Server.world.getMaxShield();
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

    public void setiShot(int iShot) {
        this.iShot = iShot;
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
