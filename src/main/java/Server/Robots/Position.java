package Server.Robots;

import Server.Server;
import Server.World.Config;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Position{ " +
                " x=" + getX()+
                ", y=" + getY() +
                " }";
    }
    public static boolean Isin(int newX, int newY){
        Position top = Server.world.getTOP_LEFT();
        Position bot = Server.world.getBOTTOM_RIGHT();

        if (newX >= top.getX() && newX <= bot.getX() && newY <= top.getY() && newY >= bot.getY()){
            return true;
        }else{
            return false;}
    }

}