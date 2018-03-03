package com.ljnic;

import com.badlogic.ashley.core.Component;
import org.codetome.zircon.api.Position;

public class PositionComponent implements Component {
    public Position position = Position.of(0,0);
}
