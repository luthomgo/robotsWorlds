package Server.World;

import Server.Robots.Position;

public class PitsObstacle implements Obstacles{
    /**
     * The PitsObstacle class represents a pit obstacle on the game board.
     * It implements the Obstacles interface, providing methods to get the pit's type,
     * position, size, and to check if it blocks a specific position or path.
     */
    private final int x;
    private final int y;
    private final int size = 1;
    private String type = "pit";


    public PitsObstacle(int x, int y){
        /**
         * Constructs a PitsObstacle at the specified coordinates.
         *
         * @param x The x-coordinate of the bottom-left corner of the pit.
         * @param y The y-coordinate of the bottom-left corner of the pit.
         */
        this.x = x;
        this.y = y;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getBottomLeftX() {
        return this.x;
    }

    @Override
    public int getBottomLeftY() {
        return this.y;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean blocksPosition(Position position) {
        /**
         * Checks if the pit blocks a given position.
         * A position is blocked if it lies within the bounds of the pit.
         *
         * @param position The position to check.
         * @return True if the position is blocked by the pit, false otherwise.
         */
        return (position.getX() >= this.x && position.getX() < this.x + this.size) && (position.getY() >= this.y && position.getY() < this.y + this.size);
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        /**
         * Checks if the pit blocks a path between two positions.
         * A path is blocked if it passes through the pit's location.
         *
         * @param a The starting position of the path.
         * @param b The ending position of the path.
         * @return True if the path is blocked by the pit, false otherwise.
         */
        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        if (blocksPosition(b)) return true;

        if (aX >= this.x && aX <= this.x + this.size && bX >= this.x && bX <= this.x + this.size){
            if (this.y >= aY && this.y <= bY || this.y <= aY && this.y >= bY) return true;
        }
        if (aY >= this.y && aY <= this.y + this.size && bY >= this.y && bY <= this.y + this.size){
            return this.x >= aX && this.x <= bX;
        }
        return false;
    }

    @Override
    public String toString() {
        /**
         * Returns a string representation of the PitsObstacle.
         *
         * @return A string describing the pit's coordinates.
         */
        return "Pit Obstacle" + "\n{" +
                "x = " + getBottomLeftX()+
                ", y = " + getBottomLeftY() +
                "}" +
                " to " +
                "{x = " + (getBottomLeftX() + size) +
                ", y = " + (getBottomLeftY() + size) + "}";
    }
}
