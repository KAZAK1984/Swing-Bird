package org.flappyBird.entity;

import org.flappyBird.render.IRenderCmd;

import java.awt.Rectangle;
import java.util.List;

/**
 * Логическая пара труб.
 * Содержит две физические сущности - Pipe, которые реализуют IEntity.
 * Сам PipeColumn - это контейнер, а не сущность.
 */
public class PipeColumn
{
    private static final int GAP_SIZE = 100;
    private static final int COLUMN_WIDTH = 52; // TODO: Заменить костыль на нормальную константу
    
    private final Pipe topPipe;
    private final Pipe bottomPipe;

    private boolean scored = false;
    private boolean expired = false;


    public PipeColumn(float x, float gapY)
    {
        this(x, gapY, 600);
    }

    public PipeColumn(float x, float gapY, int bottomLimitY)
    {
        float halfGap = GAP_SIZE / 2.0f;
        float topHeight = gapY - halfGap;
        float bottomY = gapY + halfGap;

        int bottomHeight = Math.max(0, bottomLimitY - (int) bottomY);

        topPipe = new Pipe(x, 0, (int) topHeight, true);
        bottomPipe = new Pipe(x, bottomY, bottomHeight, false);
    }

    public void update(double deltaMillis)
    {
        topPipe.update(deltaMillis);
        bottomPipe.update(deltaMillis);

        // Трубы двигаются синхронно - значит можно проверять только по одной
        if (topPipe.isExpired())
            this.expired = true;
    }

    public void render(List<IRenderCmd> buffer)
    {
        topPipe.render(buffer);
        bottomPipe.render(buffer);
    }

    public boolean checkCollision(Rectangle birdBounds)
    {
        return topPipe.getBounds().intersects(birdBounds) || bottomPipe.getBounds().intersects(birdBounds);
    }

    public boolean isExpired()
    {
        return expired;
    }
    
    public boolean isScored()
    {
        return scored;
    }
    
    public void setScored(boolean scored)
    {
        this.scored = scored;
    }

    // Методы для оптимизации в цикле (break/continue)
    public float getX()
    {
        return topPipe.getX();
    }
    
    public int getWidth()
    {
        return COLUMN_WIDTH;
    }
}
