
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class LookTest {
    @Test
    public void testVisibilityForObstacles() {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);


        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY();

        int visibility = target.getVisibility();

        Position look_north = new Position(tX,tY + visibility);
        Position look_south = new Position(tX,tY - visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);


        assertEquals(tX, look_north.getX());
        assertEquals(tY + visibility, look_north.getY());

        assertEquals(tX, look_south.getX());
        assertEquals(tY - visibility, look_south.getY());

        assertEquals(tX -  visibility, look_west.getX());
        assertEquals(tY, look_west.getY());

        assertEquals(tX + visibility, look_east.getX());
        assertEquals(tY, look_east.getY());

    }
    @Test
    public void testVisibilityForRobots(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        Position currRobot = target.getPosition();
        int robotX = currRobot.getX();
        int robotY = currRobot.getY();

        int visibility = target.getVisibility();

        Position robot_north = new Position(robotX,robotY + visibility);
        Position robot_south = new Position(robotX,robotY - visibility);
        Position robot_west = new Position(robotX - visibility,robotY);
        Position robot_east = new Position(robotX + visibility,robotY);


        assertEquals(robotX, robot_north.getX());
        assertEquals(robotY + visibility, robot_north.getY());

        assertEquals(robotX, robot_south.getX());
        assertEquals(robotY - visibility, robot_south.getY());

        assertEquals(robotX -  visibility, robot_west.getX());
        assertEquals(robotY, robot_west.getY());

        assertEquals(robotX + visibility, robot_east.getX());
        assertEquals(robotY, robot_east.getY());


    }

    @Test
    public void NorthObstacles(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        Position look_north = new Position(target.getPosition().getX(), target.getPosition().getY()+ target.getVisibility());
        Position targetPosition = target.getPosition();
        JsonObject obstacleStats = new JsonObject();
        JsonArray objects = new JsonArray();

        for (Obstacles i : testObs){
            if (look_north.getY() >= i.getBottomLeftY() && targetPosition.getY() <= i.getBottomLeftY() && targetPosition.getX() >= i.getBottomLeftX() && targetPosition.getX() <= i.getBottomLeftX()+ i.getSize()) {
                obstacleStats.addProperty("direction", "NORTH");
                obstacleStats.addProperty("type", i.getType());
                int distance = targetPosition.getY() - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
                assertEquals("NORTH", obstacleStats.get("direction").getAsString());
                assertEquals("Mountain", obstacleStats.get("type").getAsString());
                assertEquals(20, obstacleStats.get("distance").getAsInt());
            }
        }

    }
    @Test
    public void testBlockPosition(){
        Obstacles obstacles = new MountainObstacle(5,5);


        assertTrue(obstacles.blocksPosition(new Position(7,7)));
        assertFalse(obstacles.blocksPosition(new Position(15,15)));
        assertTrue(obstacles.blocksPosition(new Position(5,5)));
        assertFalse(obstacles.blocksPosition(new Position(15, 5)));
    }

    @Test
    public void testBlockPath(){
        Obstacles obstacles = new MountainObstacle(5,5);

        assertTrue(obstacles.blocksPath(new Position(3,7), new Position(7,7)));
        assertFalse(obstacles.blocksPath(new Position(7,7), new Position(12,7)));
        assertTrue(obstacles.blocksPath(new Position(7,3), new Position(7,7)));
        assertFalse(obstacles.blocksPath(new Position(3,15), new Position(12,15)));
        assertFalse(obstacles.blocksPath(new Position(15,3), new Position(15,12)));
    }

}
