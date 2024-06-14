import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import Server.Robots.Position;
import Server.World.Config;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

public class WorldTest {

    @Test
    public void testWorldTop() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("World x", 30);
        response.addProperty("World y", 30);
        config.writeToFile(response);

        Position top = config.readWorldTop(response);

        assertNotNull(top);
        assertEquals(-30, top.getX());
        assertEquals(30, top.getY());
    }

    @Test
    public void testWorldShield() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldShield", 5);
        config.writeToFile(response);

        int shield = config.readWorldShield(response);
        assertEquals(5, shield);
    }

    @Test
    public void testWorldShots() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldShots", 5);
        config.writeToFile(response);

        int shots = config.readWorldShots(response);
        assertEquals(5, shots);
    }

    @Test
    public void testWorldPlayers() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldPlayers", 2);
        config.writeToFile(response);

        int players = config.readWorldPlayers(response);
        assertEquals(2, players);
    }

    @Test
    public void testWorldVisibility() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldVis", 2);
        config.writeToFile(response);

        int vis = config.readWorldVise(response);
        assertEquals(2, vis);
    }

    @Test
    public void testWorldReload() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldReload", 5);
        config.writeToFile(response);

        int reload = config.readWorldReloadTime(response);
        assertEquals(5, reload);
    }
    @Test
    public void testWorldRepair() {
        Config config = new Config();
        config.createFile();
        JsonObject response = new JsonObject();
        response.addProperty("WorldRepair", 5);
        config.writeToFile(response);

        int repair = config.readWorldRepairTime(response);
        assertEquals(5, repair);
    }
}