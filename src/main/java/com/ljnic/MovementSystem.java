package com.ljnic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;

public class MovementSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public void update(float deltaTime){
        for(int i = 0; i < entities.size(); i++ ){
            
        }
    }
}
