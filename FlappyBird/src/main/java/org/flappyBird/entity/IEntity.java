package org.flappyBird.entity;

import org.flappyBird.render.IRenderCmd;

import java.awt.Rectangle;
import java.util.List;

public interface IEntity
{
    void update(double deltaMillis);
    void render(List<IRenderCmd> buffer);
    Rectangle getBounds();
    boolean isExpired();
}