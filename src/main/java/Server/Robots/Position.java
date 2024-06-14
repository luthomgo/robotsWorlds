package Server.Robots;

import Server.Server;

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
        return "Position{" +
                "x=" + getX()+
                ", y=" + getY() +
                "}";
    }
    public static boolean Isin(int newX, int newY,Position top, Position bot){

        if (newX >= top.getX() && newX <= bot.getX() && newY <= top.getY() && newY >= bot.getY()){
            return true;
        }else{
            return false;}
    }

}