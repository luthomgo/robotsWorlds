package Server.World;
import Server.Robots.Position;
import Server.Robots.Robot;

import java.util.ArrayList;
import java.util.List;

public class World {
    protected final Position TOP_LEFT = new Position(-100,200);
    protected final Position BOTTOM_RIGHT = new Position(100,-200);
    public List<SqaureObstacle> obstacles = new ArrayList<>();
    public List<Robot> robotList = new ArrayList<>();

    public World(){
        System.out.println("Started world");
        genObs();
    }

    public void genObs(){
        this.obstacles.add(new SqaureObstacle(5,5));
//        this.obstacles.add(new SqaureObstacle(1,5));
//        this.obstacles.add(new SqaureObstacle(15,2));
//        this.obstacles.add(new SqaureObstacle(0,5));
    }
    public List<SqaureObstacle> getObstacles() {
        return this.obstacles;
    }

    public void showObstacles() {
        obstacles.forEach(o -> System.out.println(o.toString()));
    }

    public List<Robot> getRobotList() {
        return robotList;
    }
}

