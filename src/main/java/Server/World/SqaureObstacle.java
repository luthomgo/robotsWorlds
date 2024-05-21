package Server.World;

import Server.Robots.Position;

public class SqaureObstacle implements Obstacles{
    private int x;
    private int y;
    private String type = "obstacle";


    public  SqaureObstacle(int x,int y){
        this.x = x;
        this.y = y;
    }

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
        return 5;
    }

    @Override
    public boolean blocksPosition(Position position) {
        return (position.getX() >= this.x && position.getX() < this.x + 5) && (position.getY() >= this.y && position.getY() < this.y + 5);
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        if (aX >= this.x && aX <= this.x + 5 && bX >= this.x && bX <= this.x + 5){
            if (this.y >= aY && this.y <= bY || this.y <= aY && this.y >= bY) return true;
        }
        if (aY >= this.y && aY <= this.y + 5 && bY >= this.y && bY <= this.y + 5){
            return this.x >= aX && this.x <= bX;
        }
        return false;

    }
}
