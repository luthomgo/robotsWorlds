import Server.Robots.Launch;
import Server.Robots.Robot;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

public class LaunchTest {

    private Launch launch;
    private JsonObject received;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;



    @Test
    public void testLaunchResponse() {
        if (launch != null) {
            JsonObject response = launch.LaunchResponse();
            assertNotNull(response);
            assertEquals("testRobot", response.get("name").getAsString());
            assertEquals("tank", response.get("kind").getAsString());
            assertEquals(5, response.get("shield").getAsInt());
            assertEquals(1, response.get("shots").getAsInt());
            assertEquals(5, response.get("vis").getAsInt());
        }
    }

    @Test
    public void testGetRobot() {
        if (launch != null) {
            Robot robot = launch.getRobot();
            assertNotNull(robot);
            assertEquals("testRobot", robot.getName());
            assertEquals("tank", robot.getkind());
            assertEquals(5, robot.getShield());
            assertEquals(1, robot.getShots());
            assertEquals(5, robot.getVisibility());
        }
    }

    @Test
    public void testLaunchWithoutArguments() {
        if (launch != null) {
            JsonObject response = launch.LaunchResponse();
            assertNotNull(response);
            assertEquals("testRobot", response.get("name").getAsString());
            assertEquals("Normy", response.get("kind").getAsString());
            assertEquals(6, response.get("shield").getAsInt());
            assertEquals(6, response.get("shots").getAsInt());
            assertEquals(6, response.get("vis").getAsInt());
        }
    }

    @Test
    public void testLaunchWithSniperArguments() {
    if (launch != null) {
        JsonArray args = new JsonArray();
        args.add("sniper");
        received.addProperty("arguments",args.getAsString());
        launch = new Launch(received, socket, dos, dis);
        JsonObject response = launch.LaunchResponse();
        assertNotNull(response);
        assertEquals("testRobot", response.get("name").getAsString());
        assertEquals("sniper", response.get("kind").getAsString());
        assertEquals(1, response.get("shield").getAsInt());
        assertEquals(5, response.get("shots").getAsInt());
        assertEquals(10, response.get("vis").getAsInt());
        }
    }

    @Test
    public void testLaunchWithBrad1Arguments() {
        if (launch != null) {
            JsonArray args = new JsonArray();
            args.add("brad1");
            received.addProperty("arguments", args.getAsString());
            launch = new Launch(received, socket, dos, dis);
            JsonObject response = launch.LaunchResponse();
            assertNotNull(response);
            assertEquals("testRobot", response.get("name").getAsString());
            assertEquals("brad1", response.get("kind").getAsString());
            assertEquals(4, response.get("shield").getAsInt());
            assertEquals(2, response.get("shots").getAsInt());
            assertEquals(5, response.get("vis").getAsInt());
        }
    }
}