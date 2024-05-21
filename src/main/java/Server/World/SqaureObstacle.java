package Server.World;

import Server.Robots.Position;

public class SqaureObstacle implements Obstacles{
    private int x;
    private int y;


    public  SqaureObstacle(int x,int y){
        this.x = x;
        this.y = y;
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
        return 5;
    }

    @Override
    public boolean blocksPosition(Position position) {
        return (position.getX() >= this.x && position.getX() < this.x + 5) && (position.getY() >= this.y && position.getY() < this.y + 5);
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        int startX = Math.min(a.getX(), b.getX());
        int endX = Math.max(a.getX(), b.getX());
        int startY = Math.min(a.getY(), b.getY());
        int endY = Math.max(a.getY(), b.getY());

        for (int x = startX; x < endX; x++) {
            if (x == this.x && startX != this.x) {
                return true;
            }
        }
        for (int y = startY; y < endY; y++) {
            if (y == this.y && startY != this.y) {
                return true;
            }
        }
        return false;
    }

}
