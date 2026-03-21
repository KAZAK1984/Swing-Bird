package org.flappyBird.entity;

import org.flappyBird.render.CmdSprite;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.render.SpriteType;

import java.util.List;

public class Bird extends Entity
{
    private static final float GRAVITY = 0.003f;
    private static final float FLAP_STRENGTH = -0.6f;

    private float velocityY = 0;

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

        // TODO: Обработка пола (смерть или остановка)
    }

    @Override
    public void render(List<IRenderCmd> buffer)
    {
        float rotation = (float) Math.toRadians(velocityY * 3); // Пример наклона
        buffer.add(new CmdSprite(SpriteType.BIRD, (int)x, (int)y, rotation));
    }

    public void flap()
    {
        velocityY = FLAP_STRENGTH;
    }
}
