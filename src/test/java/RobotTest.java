import static  org.junit.Assert.*;

import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.Obstacles;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RobotTest {
    List<Obstacles> testObs = new ArrayList<>();
    List<Robot> testRobs = new ArrayList<>();

    Position initialPosition = new Position(5, 10);
    Position topL = new Position(-50, 50);
    Position botR = new Position(50, -50);
    Robot testRobot = new Robot("robot", "sniper", 10, testObs, testRobs, 5, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

    @Test
    public void testName(){
        String name = testRobot.getName();
        assertEquals(name, "robot");
    }

    @Test
    public void testDirection(){
        Direction direction = testRobot.getDirection();
        assertEquals(direction, Direction.NORTH);
    }

    @Test
    public void testPosition(){
        Position position = testRobot.getPosition();
        assertEquals(position, initialPosition);
    }

    @Test
    public void testShots(){
        int shots = testRobot.getShots();
        assertEquals(shots, 5);
    }

    @Test
    public void testShield(){
        int shield = testRobot.getShield();
        assertEquals(shield, 5);
    }

    @Test
    public void testObstacles(){
        List<Obstacles> obstacles = testRobot.getObstacles();
        assertEquals(obstacles, testObs);
    }

    @Test
    public void testRobotList(){
        List<Robot> robotList = testRobot.getRobotList();
        assertEquals(robotList, testRobs);
    }

    @Test
    public void testGetTopLeft(){
        Position topLeft = testRobot.getTOP_LEFT();
        assertEquals(topLeft, topL);
    }

    @Test
    public void testGetBottomRight(){
        Position bottomRight = testRobot.getBOTTOM_RIGHT();
        assertEquals(bottomRight, botR);
    }

    @Test
    public void testGetInitialPosition(){
        Position initialPosition = testRobot.getPosition();
        assertEquals(initialPosition, this.initialPosition);
    }

    @Test
    public void testGetInitialDirection(){
        Direction initialDirection = testRobot.getDirection();
        assertEquals(initialDirection, Direction.NORTH);
    }

    @Test
    public void testGetInitialShots(){
        int initialShots = testRobot.getShots();
        assertEquals(initialShots, 5);
    }

    @Test
    public void testGetInitialShield(){
        int initialShield = testRobot.getShield();
        assertEquals(initialShield, 5);
    }


    @Test
    public void testGetInitialObstacles(){
        List<Obstacles> initialObstacles = testRobot.getObstacles();
        assertEquals(initialObstacles, testObs);
    }

    @Test
    public void testGetInitialRobotList(){
        List<Robot> initialRobotList = testRobot.getRobotList();
        assertEquals(initialRobotList, testRobs);
    }

    @Test
    public void testGetInitialTopLeft(){
        Position initialTopLeft = testRobot.getTOP_LEFT();
        assertEquals(initialTopLeft, topL);
    }

    @Test
    public void testGetInitialBottomRight(){
        Position initialBottomRight = testRobot.getBOTTOM_RIGHT();
        assertEquals(initialBottomRight, botR);
    }
}
