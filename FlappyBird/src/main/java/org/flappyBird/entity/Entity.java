package org.flappyBird.entity;

import org.flappyBird.render.IRenderCmd;

import java.awt.Rectangle;
import java.util.List;

public abstract class Entity implements IEntity
{
    protected float x, y;
    protected int width, height;
    protected boolean expired = false;

    public Entity(float x, float y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public abstract void update(double deltaMillis);

    @Override
    public abstract void render(List<IRenderCmd> buffer);

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, width, height);
    }

    @Override
    public boolean isExpired()
    {
        return expired;
    }
}
