package com.ljnic;

public class Tile {

    private boolean passable;


    public Tile(boolean passable){
        this.passable = passable;
    }
    public boolean isPassable(){
        return passable;
    }
}
