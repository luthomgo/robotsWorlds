package Server.Commands;

import Server.Robots.Robot;
import Server.World.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

public abstract class Command {
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
        String command = response.get("command").getAsString();
        JsonArray args = response.get("arguments").getAsJsonArray();
        System.out.println(args);

        if (!isValidArg(args)){
            return new ErrorResponse(generateErrorResponse("Could not parse arguments"));
        }
        return switch (command) {
            case "state" -> new StateCommand();
            case "look" -> new LookCommand();
            case "forward" -> new ForwardCommand(args);
            default -> new ErrorResponse(generateErrorResponse("Unsupported command"));
        };
    }
    private static boolean isValidArg(JsonArray args){
        String[] validArgs = {"sniper", "tank", "brad1"};
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
        JsonObject Response = new JsonObject();
        Response.addProperty("result", "ERROR");
        JsonObject data = new JsonObject();
        data.addProperty("message", message);
        Response.add("data", data);
        return  Response;
    }

    public static class ErrorResponse extends Command{
        private JsonObject errorResponse;

        public ErrorResponse(JsonObject errorResponse){
            this.errorResponse = errorResponse;
        }

        @Override
        public JsonObject execute(Robot target) {
            return errorResponse;
        }
    }
}