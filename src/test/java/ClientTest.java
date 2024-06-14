import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import Client.Request;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

public class ClientTest {
    @Test
    public void testCreateRequestWithNoArguments() {
        Request request = new Request("Robot1", "state");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("state", json.get("command").getAsString());
        assertTrue(json.get("arguments").getAsJsonArray().isEmpty());
    }

    @Test
    public void testCreateRequestWithSingleArgument() {
        Request request = new Request("Robot1", "forward 10");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("forward", json.get("command").getAsString());

        JsonArray arguments = json.getAsJsonArray("arguments");
        assertEquals(1, arguments.size());
        assertEquals("10", arguments.get(0).getAsString());
    }

    @Test
    public void testCreateRequestWithMultipleArguments() {
        Request request = new Request("Robot1", "move 10 20");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("move", json.get("command").getAsString());

        JsonArray arguments = json.getAsJsonArray("arguments");
        assertEquals(2, arguments.size());
        assertEquals("10", arguments.get(0).getAsString());
        assertEquals("20", arguments.get(1).getAsString());
    }

    @Test
    public void testCreateRequestWithEmptyCommand() {
        Request request = new Request("Robot1", "");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("", json.get("command").getAsString());
        assertTrue(json.get("arguments").getAsJsonArray().isEmpty());
    }

    @Test
    public void testCreateRequestWithComplexArguments() {
        Request request = new Request("Robot1", "turn right 90");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("turn", json.get("command").getAsString());

        JsonArray arguments = json.getAsJsonArray("arguments");
        assertEquals(2, arguments.size());
        assertEquals("right", arguments.get(0).getAsString());
        assertEquals("90", arguments.get(1).getAsString());
    }

    @Test
    public void testCreateRequestWithUpperCaseCommand() {
        Request request = new Request("Robot1", "LOOK");
        JsonObject json = request.createRequest();

        assertEquals("Robot1", json.get("robot").getAsString());
        assertEquals("look", json.get("command").getAsString());
        assertTrue(json.get("arguments").getAsJsonArray().isEmpty());
    }
}