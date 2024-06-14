package Server.Robots;

import Server.Server;

public class Position {
    /**
     * Represents a position in the world with x and y coordinates.
     */
    private final int x;
    private final int y;

    public Position(int x, int y) {
    /**
     * Constructs a Position with the given x and y coordinates.
     *
     * @param x the x-coordinate of the position.
     * @param y the y-coordinate of the position.
     */
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
        /**
         * Returns a string representation of the position.
         *
         * @return a string representing the position.
         */
        return "Position{ " +
                " x=" + getX()+
                ", y=" + getY() +
                " }";
    }
    public static boolean Isin(int newX, int newY,Position top, Position bot){
    /**
     * Checks if a given position (newX, newY) is within the bounds defined by the top-left and bottom-right positions.
     *
     * @param newX the x-coordinate of the position to check.
     * @param newY the y-coordinate of the position to check.
     * @param top the top-left position defining the bounds.
     * @param bot the bottom-right position defining the bounds.
     * @return true if the position is within bounds, false otherwise.
     */

        if (newX >= top.getX() && newX <= bot.getX() && newY <= top.getY() && newY >= bot.getY()){
            return true;
        }else{
            return false;}
    }

}