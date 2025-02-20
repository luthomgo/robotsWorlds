package Server.World;

import Server.Robots.Position;

public class MountainObstacle implements Obstacles{
    private final int x;
    private final int y;
    private final int size = 5;
    private final String type = "mountain";

    public MountainObstacle(int x, int y){
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
        return (position.getX() >= this.x && position.getX() < this.x + this.size) && (position.getY() >= this.y && position.getY() < this.y + this.size);
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
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
        return "Mountain Obstacle" + "\n{" +
                "x = " + getBottomLeftX()+
                ", y = " + getBottomLeftY() +
                "}" +
                " to " +
                "{x = " + (getBottomLeftX() + size) +
                ", y = " + (getBottomLeftY() + size) + "}";
    }
}
