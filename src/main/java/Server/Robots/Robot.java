package Server.Robots;

import Server.Commands.Command;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class Robot {
    private Position centre = new Position(0,0);
    private String name;
    private String kind;
    private int shield;
    private int shots;
    private Position position;
    private int visibility = 10;
    private int reload = 5;
    private int repair = 5;
    private Direction direction = Direction.NORTH;
    private String status = "NORMAL";


    public Robot(String name, String kind, int shield, int shots) {
        this.shield = shield;
        this.kind = kind;
        this.name = name;
        this.shots = shots;
        this.position = centre;
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


}
