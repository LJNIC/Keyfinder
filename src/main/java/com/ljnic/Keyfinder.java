package com.ljnic;

import org.codetome.zircon.api.*;
import org.codetome.zircon.api.animation.Animation;
import org.codetome.zircon.api.animation.AnimationHandler;
import org.codetome.zircon.api.animation.AnimationResource;
import org.codetome.zircon.api.animation.AnimationResult;
import org.codetome.zircon.api.builder.*;
import org.codetome.zircon.api.color.ANSITextColor;
import org.codetome.zircon.api.color.TextColor;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.component.*;
import org.codetome.zircon.api.component.builder.ButtonBuilder;
import org.codetome.zircon.api.component.builder.ColorThemeBuilder;
import org.codetome.zircon.api.component.builder.PanelBuilder;
import org.codetome.zircon.api.graphics.Layer;
import org.codetome.zircon.api.graphics.StyleSet;
import org.codetome.zircon.api.graphics.TextImage;
import org.codetome.zircon.api.input.Input;
import org.codetome.zircon.api.input.KeyStroke;
import org.codetome.zircon.api.modifier.Border;
import org.codetome.zircon.api.modifier.BorderBuilder;
import org.codetome.zircon.api.modifier.BorderPosition;
import org.codetome.zircon.api.modifier.BorderType;
import org.codetome.zircon.api.resource.CP437TilesetResource;
import org.codetome.zircon.api.resource.REXPaintResource;
import org.codetome.zircon.api.screen.Screen;
import org.codetome.zircon.api.terminal.Terminal;
import org.codetome.zircon.internal.component.DefaultColorTheme;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Keyfinder{

    public static void main(String[] args) throws InterruptedException{
        //region REXPaintStreams
        final InputStream TITLE = Simple.class.getResourceAsStream("/title11.xp");
        final InputStream BACKGROUND = Simple.class.getResourceAsStream("/blueBackground.xp");
        final InputStream FIRST = Simple.class.getResourceAsStream("/title1.xp");
        REXPaintResource title = REXPaintResource.loadREXFile(TITLE);
        REXPaintResource bg = REXPaintResource.loadREXFile(BACKGROUND);
        REXPaintResource first = REXPaintResource.loadREXFile(FIRST);
        List<Layer> fLayer = first.toLayerList();
        Layer firstLayer = fLayer.get(0);
        List<Layer> tLayer = title.toLayerList();
        Layer titleLayer = tLayer.get(0);
        List<Layer> bgLayer = bg.toLayerList();
        Layer backgroundLayer = bgLayer.get(0);
        //endregion

        //region ComponentBuilders
        final String START_LABEL = "START GAME";

        Engine engine = new Engine();

        final TextCharacter back = TextCharacterBuilder.newBuilder()
                .backgroundColor(ANSITextColor.BLACK)
                .foregroundColor(ANSITextColor.WHITE)
                .character('.')
                .build();
        final Terminal terminal = SwingTerminalBuilder.newBuilder()
                .initialTerminalSize(Size.of(Constants.MAPWIDTH, Constants.MAPHEIGHT))
                .font(CP437TilesetResource.WANDERLUST_16X16.toFont())
                .title("Keyfinder")
                .build();
        final TextCharacter heroChar = TextCharacterBuilder.newBuilder()
                .backgroundColor(ANSITextColor.BLACK)
                .foregroundColor(ANSITextColor.CYAN)
                .character('@')
                .build();
        final Border menuBorder = BorderBuilder.newBuilder()
                .borderType(BorderType.SOLID)
                .borderPositions(BorderPosition.BOTTOM, BorderPosition.LEFT, BorderPosition.RIGHT, BorderPosition.TOP)
                .build();
        final Panel menuPanel = PanelBuilder.newBuilder()
                .addBorder(menuBorder)
                .position(Position.OFFSET_1x1)
                .size(Size.of(Constants.MAPWIDTH - 2, Constants.MAPHEIGHT - 2))
                .build();
        final Button newGameButton = ButtonBuilder.newBuilder()
                .text(START_LABEL)
                .position(Position.of(50,25))
                .build();
        final StyleSet buttonStyle = StyleSetBuilder.newBuilder()
                .foregroundColor(TextColorFactory.fromRGB(255, 255, 0, 0))
                .backgroundColor(TextColorFactory.fromRGB(0, 0, 0, 0))
                .modifiers(Modifiers.GLOW)
                .build();
        final StyleSet normalButtonStyle = StyleSetBuilder.newBuilder()
                .foregroundColor(TextColorFactory.fromRGB(241, 180, 0))
                .backgroundColor(TextColorFactory.fromRGB(78, 32, 1, 0 ))
                .build();
        final ComponentStyles newStyles = ComponentStylesBuilder.newBuilder()
                .mouseOverStyle(buttonStyle)
                .defaultStyle(normalButtonStyle)
                .build();
        DefaultColorTheme theme = ColorThemeBuilder.newBuilder()
                .accentColor(ANSITextColor.YELLOW)
                .brightBackgroundColor(TextColorFactory.fromRGB(80, 80, 80, 0))
                .darkBackgroundColor(TextColorFactory.fromRGB(50, 50, 50, 0))
                .brightForegroundColor(ANSITextColor.GREEN)
                .darkForegroundColor(ANSITextColor.BLUE)
                .build();
        final Layer playerLayer = LayerBuilder.newBuilder()
                .size(Size.of(1, 1))
                .filler(heroChar)
                .build();

        final Screen screen = ScreenBuilder.createScreenFor(terminal);
        //endregion

        Entity newHero = new Entity();
        newHero.add(new PositionComponent());
        engine.addEntity(newHero);





        Screen mainMenu = ScreenBuilder.createScreenFor(terminal);
        mainMenu.draw(firstLayer, Position.TOP_LEFT_CORNER);

        playMenuAnimation(mainMenu);

        mainMenu.draw(titleLayer, Position.TOP_LEFT_CORNER);
        newGameButton.setComponentStyles(newStyles);
        menuPanel.addComponent(newGameButton);
        mainMenu.addComponent(menuPanel);
        mainMenu.display();

        Map map = new Map();
        Tile[][] theMap = map.makeMap(backgroundLayer);

        GameObject hero = new GameObject(5, 5, heroChar, theMap);
        GameObject enemy = new GameObject(10,5, heroChar, theMap);
        playerLayer.moveTo(hero.getPosition());

        newGameButton.onMouseReleased((mouseAction -> {
            screen.pushLayer(backgroundLayer);
            screen.pushLayer(playerLayer);
            screen.display();
        }));
        InputHandler inputHandler = new InputHandler(hero);

        boolean foundKey = false;
        double previous = System.currentTimeMillis();
        double lag = 0.0;

        terminal.onInput(input -> inputHandler.handleKeys(input));
        while(true){
            double current = System.currentTimeMillis();
            double elapsed = current - previous;
            previous = current;
            lag += elapsed;
            while (lag >= 10){
                lag -= 10;
                if(hero.hasTaken()){
                    playerLayer.moveTo(hero.getPosition());
                    screen.pushLayer(playerLayer);
                    screen.refresh();
                    hero.tookAction(false);

                    if(hero.getPosition().compareTo(map.getKeyPosition()) == 0 && !foundKey){
                        map.openDoor();
                        screen.pushLayer(backgroundLayer);
                        foundKey = true;
                    }else if(hero.getPosition().compareTo(map.getExitPosition()) == 0){
                        System.exit(0);
                    }
                }
            }
        }

    }


    private static void enableMovement(Terminal terminal, GameObject hero){
        terminal.onInput((input -> {
            if(input.isKeyStroke()) {
                KeyStroke keyStroke = input.asKeyStroke();
                switch(keyStroke.getCharacter()) {
                    case 'w':
                        hero.move(0, -1);
                        hero.tookAction(true);
                        break;
                    case 's':
                        hero.move(0, 1);
                        hero.tookAction(true);
                        break;
                    case 'd':
                        hero.move(1, 0);
                        hero.tookAction(true);
                        break;
                    case 'a':
                        hero.move(-1, 0);
                        hero.tookAction(true);
                        break;
                }
            }
        }));
    }
    private static void playMenuAnimation(Screen mainMenu){
        AnimationBuilder titleBuilder = AnimationResource.loadAnimationFromStream(Simple.class.getResourceAsStream("/title.zap"));
        titleBuilder.loopCount(1);
        for (int i = 0; i < titleBuilder.getLength(); i++) {
            titleBuilder.addPosition(Position.TOP_LEFT_CORNER);
        }
        Animation titleAnimation = titleBuilder.build();
        final AnimationHandler animationHandler = new AnimationHandler(mainMenu);
        animationHandler.addAnimation(titleAnimation).waitUntilFinish(13, TimeUnit.SECONDS);
    }
}
