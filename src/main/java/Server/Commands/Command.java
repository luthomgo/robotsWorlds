package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;

public abstract class Command {
/**
 * The Command class represents an abstract base class for all commands that can be executed by a robot.
 * It provides methods for creating specific command instances, validating arguments, and generating error responses.
 */
    private JsonArray args;
    public Command(JsonArray args){
        this.args = args;
    }
    public Command(){}

    public abstract JsonObject execute(Robot target);

    public JsonArray getArgument(){
        return this.args;
    }

    public static Command create(JsonObject response) {
    /**
     * Creates a specific command instance based on the provided JsonObject.
     *
     * @param response the JsonObject containing the command and its arguments.
     * @return a Command instance representing the specific command.
     */

        String command = response.get("command").getAsString();
        JsonArray args = response.get("arguments").getAsJsonArray();

        if (!isValidArg(args)){
            return new ErrorResponse(generateErrorResponse("Could not parse arguments"));
        }
        if (args.size() > 2){
            return new ErrorResponse(generateErrorResponse("Could not parse arguments"));
        }

        return switch (command) {
            case "state" -> new StateCommand();
            case "look" -> new LookCommand();
            case "forward" -> new ForwardCommand(args);
            case "back" -> new BackwardCommand(args);
            case "repair" -> new RepairCommand();
            case "reload" -> new ReloadCommand();
            case"turn" -> new TurnCommand(args);
            case "fire" -> new FireCommand();
            case "help" -> new HelpCommand(args);
            case "exit" -> new ExitCommand();
            default -> new ErrorResponse(generateErrorResponse("Unsupported command"));
        };
    }
    public static boolean isValidArg(JsonArray args){
    /**
     * Validates the arguments for a command.
     *
     * @param args the arguments to validate.
     * @return true if the arguments are valid, false otherwise.
     */

        if (args.isEmpty()){
            return true;
        }
        String[] validArgs = {"sniper", "tank", "brad1", "left", "right", " "};
        for (JsonElement argElement : args){
            String arg = argElement.getAsString();
            try{
                int intValue = Integer.parseInt(arg);
                if (intValue > 0){
                    return true;
                }

            }catch (NumberFormatException e){
                if (Arrays.asList(validArgs).contains(arg)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static JsonObject generateErrorResponse(String message){
    /**
     * Generates an error response with the specified message.
     *
     * @param message the error message.
     * @return a JsonObject containing the error response.
     */

        JsonObject Response = new JsonObject();
        Response.addProperty("result", "ERROR");
        JsonObject data = new JsonObject();
        data.addProperty("message", message);
        Response.add("data", data);
        return  Response;
    }

    public static class ErrorResponse extends Command{
        private final JsonObject errorResponse;

        public ErrorResponse(JsonObject errorResponse){
            this.errorResponse = errorResponse;
        }

        @Override
        public JsonObject execute(Robot target) {
            return errorResponse;
        }
    }
}