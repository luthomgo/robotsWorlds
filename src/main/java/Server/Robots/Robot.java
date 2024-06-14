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
        /**
         * Constructs a Robot object with the given parameters.
         *
         * @param name the name of the robot
         * @param kind the type of robot
         * @param shield the initial shield value
         * @param shots the initial number of shots
         * @param vis the visibility range of the robot
         * @param s the socket connection to the client
         * @param dos the DataOutputStream to communicate with the client
         * @param dis the DataInputStream to communicate with the client
         */

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
        /**
         * Constructs a Robot object with the given parameters, used for state restoration.
         *
         * @param name the name of the robot
         * @param kind the type of robot
         * @param visibility the visibility range of the robot
         * @param obstacles the list of obstacles in the world
         * @param robotList the list of robots in the world
         * @param shield the shield value of the robot
         * @param shots the number of shots the robot has
         * @param position the position of the robot
         * @param reload the reload time for the robot
         * @param repair the repair time for the robot
         * @param direction the direction the robot is facing
         * @param iShield the initial shield value of the robot
         * @param iShot the initial shot value of the robot
         * @param TOP_LEFT the top-left position of the world
         * @param BOTTOM_RIGHT the bottom-right position of the world
         * @param maxShield the maximum shield value
         * @param maxShots the maximum shot value
         * @param worldVisibility the maximum visibility in the world
         */

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
    public int getIShot() {
        return this.iShot;
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

    public void removeRobot(Robot robot) {
        this.robotList.remove(robot);
    }
    public Position genPos(){
    /**
     * Generates a random position within the bounds of the world that is not blocked by any obstacles or other robots.
     *
     * @return a Position object representing the new position for the robot.
     */

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
    /**
     * Decreases the shield value of the robot by 1. If the shield value reaches 0 or less, checks if the robot is dead.
     */

        if(this.shield > 0){
            this.shield -= 1;
        }else {
            checkIfDead();        }
    }

    public boolean minusShot(){
    /**
     * Decreases the number of shots the robot has by 1 if the robot has shots remaining.
     *
     * @return true if a shot was successfully deducted, false if no shots remain.
     */

        if(this.shots > 0){
            this.shots -= 1;
            return true;
        }
        return false;
    }
    public void updateDirection(boolean turnRight) {
    /**
     * Updates the direction of the robot. Turns the robot right if turnRight is true, otherwise turns the robot left.
     *
     * @param turnRight a boolean indicating the direction to turn the robot. True for right, false for left.
     */

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
    /**
     * Creates a JSON object containing the data of the robot, including its position,
     * visibility, reload time, repair time, and shields.
     *
     * @return a JsonObject containing the robot's data.
     */

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
        /**
         * Sets the position of the robot to the specified position.
         *
         * @param centre the new position for the robot.
         */

        this.position= centre;
    }

    public JsonObject state(){
    /**
    * Creates a JSON object containing the state of the robot, including its position,
    * direction, shields, shots, and status.
    *
    * @return a JsonObject containing the robot's state.
    */

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
    /**
     * Creates a JSON object representing the robot's overall information, including
     * the result, data, and state of the robot.
     *
     * @return a JsonObject containing the robot's overall information.
     */

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
    /**
     * Returns a string representation of the robot, including its name, kind,
     * shield, and shots.
     *
     * @return a string representation of the robot.
     */

        return "Robot{" +
                "name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", shield=" + shield +
                ", shots=" + shots +
                '}';
    }

    public boolean updatePosition(int nrSteps) {
        /**
         * Updates the position of the robot by the specified number of steps.
         * The direction in which the robot moves depends on its current direction.
         * Checks for obstacles and other robots in the path, and only updates the position
         * if the path is clear and within bounds.
         *
         * @param nrSteps the number of steps to move.
         * @return true if the position was successfully updated, false otherwise.
         */

        List<Obstacles> temp = new ArrayList<>();

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

        for (Obstacles i:this.obstacles){
            temp.add(i);
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
        /**
         * Handles the scenario where the robot falls into a pit and dies.
         * Removes the robot from the world and notifies the client.
         */

        removeRobot(this);
        System.out.println(ANSI_RED+"Robot "+this.name+" has died" + ANSI_RESET);
        try {
            DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
            JsonObject response = new JsonObject();
            response.addProperty("result","dead");
            JsonObject data = new JsonObject();
            data.addProperty("message","You have fallen into a pit");
            response.add("data",data);
            dos.writeUTF(response.toString());
            this.client.close();
            this.dos.close();
            this.dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkIfDead() {
        /**
         * Checks if the robot's shield is zero or less, indicating it has died.
         * Removes the robot from the world and notifies the client if the robot is dead.
         */

        if (this.shield <= 0 ) {
            removeRobot(this);
            System.out.println(ANSI_RED+"Robot "+this.name+" has died" + ANSI_RESET);
            try {
                DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
                JsonObject response = new JsonObject();
                response.addProperty("result","dead");
                JsonObject data = new JsonObject();
                data.addProperty("message","You have died");
                response.add("data",data);
                dos.writeUTF(response.toString());
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
