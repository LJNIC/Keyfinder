package com.ljnic;

import org.codetome.zircon.api.input.Input;
import org.codetome.zircon.api.input.KeyStroke;
import org.codetome.zircon.api.terminal.Terminal;

public class InputHandler {

    private GameObject hero;

    public InputHandler(GameObject hero){
        this.hero = hero;
    }
    public void handleKeys(Input input){
        if(input.isKeyStroke()) {
            KeyStroke keyStroke = input.asKeyStroke();
            switch (keyStroke.getCharacter()) {
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
    }
}

