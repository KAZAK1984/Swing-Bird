package org.flappyBird.entity;

import org.flappyBird.render.CmdSprite;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.render.SpriteType;

import java.awt.*;
import java.util.List;

public class Bird extends Entity
{
    private static final float GRAVITY = 0.0025f;
    private static final float FLAP_STRENGTH = -0.6f;
    private static final float MAX_UP_TILT = (float) Math.toRadians(-45);
    private static final float MAX_DOWN_TILT = (float) Math.toRadians(70);
    private static final float TILT_SPEED = 0.006f;

    private float velocityY = 0;
    private float renderRotation = 0f;

    public Bird(float x, float y)
    {
        super(x, y, 34, 24); 
    }

    @Override
    public void update(double deltaMillis)
    {
        velocityY += (float) (GRAVITY * deltaMillis);
        y += (float) (velocityY * deltaMillis);

        if (y < 0)
        {
            y = 0;
            velocityY = 0;
        }

        float targetRotation = clamp(velocityY * 0.9f);
        float maxStep = TILT_SPEED * (float) deltaMillis;
        renderRotation = moveTowards(renderRotation, targetRotation, maxStep);
    }

    @Override
    public void render(List<IRenderCmd> buffer)
    {
        buffer.add(new CmdSprite(SpriteType.BIRD, Math.round(x), Math.round(y), renderRotation));
    }

    public void flap()
    {
        velocityY = FLAP_STRENGTH;
    }

    private static float clamp(float value)
    {
        return Math.max(Bird.MAX_UP_TILT, Math.min(Bird.MAX_DOWN_TILT, value));
    }

    private static float moveTowards(float current, float target, float maxDelta)
    {
        float delta = target - current;
        if (Math.abs(delta) <= maxDelta)
        {
            return target;
        }
        return current + Math.signum(delta) * maxDelta;
    }

    @Override
    public Rectangle getBounds()
    {
        int px = Math.round(x);
        int py = Math.round(y);

        // База (ровный/слегка приподнятый полет)
        int bx = px + 5;
        int by = py + 5;
        int bw = 23;
        int bh = 15;

        if (velocityY > 0.20f)
        {
            bx = px + 7;
            by = py + 4;
            bw = 20;
            bh = 17;
        }
        else if (velocityY > 0.10f)
        {
            bx = px + 6;
            by = py + 4;
            bw = 22;
            bh = 16;
        }

        bounds.setBounds(bx, by, bw, bh);
        return bounds;
    }
}
