import Server.World.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReloadTest {

    private Config config;
    private static final String CONFIG_DIRECTORY = System.getProperty("user.home") + "/.RobotWorldsConfig";
    private static final String CONFIG_FILE_PATH = CONFIG_DIRECTORY + "/WorldConfig.txt";

    @BeforeEach
    public void setUp() {
        config = new Config();
        deleteConfigDirectory();
    }

    @AfterEach
    public void tearDown() {
        deleteConfigDirectory();
    }

    @Test
    @Order(1)
    public void testCreateConfigF() {
        config.createConfigF();
        File dir = new File(CONFIG_DIRECTORY);
        assertTrue(dir.exists() && dir.isDirectory(), "Config directory should be created");
    }

    @Test
    @Order(2)
    public void testCreateFile() {
        config.createConfigF();
        config.createFile();
        File file = new File(CONFIG_FILE_PATH);
        assertTrue(file.exists() && file.isFile(), "Config file should be created");
    }

    @Test
    @Order(3)
    public void testWriteToFile() {
        config.createConfigF();
        config.createFile();

        JsonObject testConfig = new JsonObject();
        testConfig.addProperty("World x", 100);
        testConfig.addProperty("World y", 100);
        testConfig.addProperty("WorldVis", 10);
        testConfig.addProperty("WorldReload", 5);
        testConfig.addProperty("WorldRepair", 5);
        testConfig.addProperty("WorldShield", 5);
        testConfig.addProperty("WorldShots", 3);
        testConfig.addProperty("WorldPlayers", 2);

        config.writeToFile(testConfig);

        try {
            Scanner reader = new Scanner(new File(CONFIG_FILE_PATH));
            StringBuilder content = new StringBuilder();
            while (reader.hasNextLine()) {
                content.append(reader.nextLine());
            }
            reader.close();

            JsonObject readConfig;
            readConfig = JsonParser.parseString(content.toString()).getAsJsonObject();
            assertEquals(testConfig, readConfig, "Written config should match the test config");
        } catch (IOException e) {
            fail("Failed to read the config file");
        }
    }



    private void createTestConfigFile() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIRECTORY));
            try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
                writer.write("{\"World x\":100,\"World y\":100,\"WorldVis\":10,\"WorldReload\":5,\"WorldRepair\":5,\"WorldShield\":5,\"WorldShots\":3,\"WorldPlayers\":2}");
            }
        } catch (IOException e) {
            fail("Failed to create test config file");
        }
    }

    private void deleteConfigDirectory() {
        File dir = new File(CONFIG_DIRECTORY);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            dir.delete();
        }
    }
}
