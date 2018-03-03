package com.ljnic;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.Size;
import org.codetome.zircon.api.Symbols;
import org.codetome.zircon.api.TextCharacter;
import org.codetome.zircon.api.builder.TextCharacterBuilder;
import org.codetome.zircon.api.builder.TextImageBuilder;
import org.codetome.zircon.api.color.ANSITextColor;
import org.codetome.zircon.api.graphics.Layer;
import org.codetome.zircon.api.graphics.TextImage;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Map {

    private static Tile[][] map;

    final TextCharacter doorWall = TextCharacterBuilder.newBuilder()
            .backgroundColor(ANSITextColor.YELLOW)
            .foregroundColor(Colours.GOLD)
            .character(Symbols.BLOCK_SOLID)
            .build();
    final TextImage door = TextImageBuilder.newBuilder()
            .filler(doorWall)
            .size(Size.of(3, 3))
            .build();
    final TextCharacter key = TextCharacterBuilder.newBuilder()
            .backgroundColor(ANSITextColor.BLACK)
            .foregroundColor(Colours.GOLD)
            .character('\u255C')
            .build();
    final TextCharacter back = TextCharacterBuilder.newBuilder()
            .backgroundColor(ANSITextColor.BLACK)
            .foregroundColor(ANSITextColor.WHITE)
            .character('.')
            .build();

    private Position doorPosition, keyPosition, exitPosition;
    private Layer backgroundLayer;

    public Tile[][] makeMap(Layer backgroundLayer){

        this.backgroundLayer = backgroundLayer;
        int doorPositionX = ThreadLocalRandom.current().nextInt(1, Constants.MAPWIDTH - 4);
        int doorPositionY = ThreadLocalRandom.current().nextInt(1, Constants.MAPHEIGHT - 4);
        int keyPositionX = ThreadLocalRandom.current().nextInt(1, Constants.MAPWIDTH - 1);
        int keyPositionY = ThreadLocalRandom.current().nextInt(1, Constants.MAPHEIGHT - 1);

        doorPosition = Position.of(doorPositionX + 1, doorPositionY + 2);
        keyPosition = Position.of(keyPositionX, keyPositionY);
        Position doorImagePosition = Position.of(doorPositionX, doorPositionY);
        exitPosition = Position.of(doorPositionX + 1, doorPositionY + 1);

        door.setBackgroundColor(Colours.DARKGREY);
        door.setCharacterAt(Position.of(1, 1), '>');
        door.setCharacterAt(Position.of(1, 2), '\u2551');


        door.drawOnto(backgroundLayer, doorImagePosition);
        backgroundLayer.setCharacterAt(keyPosition, key);

        map = new Tile[Constants.MAPWIDTH][Constants.MAPHEIGHT];

        for(int row = 0; row < map[0].length; row++){
            for(int column = 0; column < map.length; column++){
                Optional<TextCharacter> current = backgroundLayer.getCharacterAt(Position.of(column, row));
                if(current.get().getCharacter() == '.' || current.get().getCharacter() == '>' || current.get().getCharacter() == '\u255C'){
                    map[column][row] = new Tile(true);
                }else{
                    map[column][row] = new Tile(false);
                }
            }
        }
        return map;
    }

    public Position getDoorPosition() {
        return doorPosition;
    }

    public Position getKeyPosition() {
        return keyPosition;
    }

    public Position getExitPosition() {
        return exitPosition;
    }

    public void openDoor(){
        map[doorPosition.getColumn()][doorPosition.getRow()] = new Tile(true);
        backgroundLayer.setCharacterAt(getDoorPosition(), back);
        backgroundLayer.setCharacterAt(getKeyPosition(), back);

    }
}
