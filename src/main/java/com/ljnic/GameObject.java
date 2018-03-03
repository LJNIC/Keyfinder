package com.ljnic;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.TextCharacter;
import org.codetome.zircon.api.graphics.Layer;

public class GameObject {
    private int positionX;
    private int positionY;
    private Position position;
    private TextCharacter symbol;
    private Layer objectLayer;
    private Tile[][] theMap;
    private boolean tookAction = false;

    public GameObject(int positionX, int positionY, TextCharacter symbol, Tile[][] theMap){
        this.positionX = positionX;
        this.positionY = positionY;
        this.position = Position.of(positionY, positionX);
        this.symbol = symbol;
        this.theMap = theMap;

    }

    public void move(int dX, int dY){
        if(!(isBlocked(dX + positionX, dY + positionY))){
            positionX += dX;
            positionY += dY;
            position = Position.of(positionX, positionY);
        }
    }

    public Position getPosition() {
        return position;
    }
    public TextCharacter getSymbol(){
        return symbol;
    }
    public boolean isBlocked(int x, int y){
        if(theMap[x][y].isPassable())
            return false;
        return true;
    }
    public void tookAction(boolean took){
        tookAction = took;
    }
    public boolean hasTaken(){
        return tookAction;
    }

}
