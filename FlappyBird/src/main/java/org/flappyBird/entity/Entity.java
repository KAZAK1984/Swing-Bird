package org.flappyBird.entity;

import org.flappyBird.render.IRenderCmd;

import java.awt.Rectangle;
import java.util.List;

public abstract class Entity implements IEntity
{
    protected final Rectangle bounds = new Rectangle();

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
     public void setX(float x) { this.x = x; }

    public int getHeight() { return height; }

    @Override
    public abstract void update(double deltaMillis);

    @Override
    public abstract void render(List<IRenderCmd> buffer);

    @Override
    public Rectangle getBounds()
    {
        bounds.setBounds((int) x, (int) y, width, height);
        return bounds;
    }

    @Override
    public boolean isExpired()
    {
        return expired;
    }
}
