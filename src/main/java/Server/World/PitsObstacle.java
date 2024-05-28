package Server.World;

import Server.Robots.Position;

public class PitsObstacle implements Obstacles{
    private int x;
    private int y;
    private int size = 5;
    private String type = "pit";


    @Override
    public int getBottomLeftX() {
        return 0;
    }

    @Override
    public int getBottomLeftY() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean blocksPosition(Position position) {
        return false;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
