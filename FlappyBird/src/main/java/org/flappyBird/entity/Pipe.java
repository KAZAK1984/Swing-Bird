package org.flappyBird.entity;

import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;

import java.util.List;

public class Pipe extends Entity
{
    private static final int WIDTH = 52;
    private static final int SPEED = 3;

    private static final int COLOR_GREEN = 0x4CAF50;
    private static final int COLOR_CAP = 0x145A18;
    private static final int CAP_HEIGHT = 8;
    private static final int CAP_WIDTH = WIDTH + 6;

    private final boolean isTop;

    public Pipe(float x, float y, int height, boolean isTop)
    {
        super(x, y, WIDTH, height);
        this.isTop = isTop;
    }

    @Override
    public void update(double deltaMillis)
    {
        x -= (float) (SPEED * (deltaMillis / 16.0)); 

        if (x + width < 0)
            expired = true;
    }

    @Override
    public void render(List<IRenderCmd> buffer)
    {
        int px = (int) x;
        int py = (int) y;
        
        if (isTop)
        {
            // Основная часть
            buffer.add(new CmdRect(px, py, WIDTH, height, COLOR_GREEN));
            
            // Конечная часть
            int capX = px - 3;
            int capY = py + height - CAP_HEIGHT;
            buffer.add(new CmdRect(capX, capY, CAP_WIDTH, CAP_HEIGHT, COLOR_CAP));
        }
        else
        {
            // Основная часть
            buffer.add(new CmdRect(px, py, WIDTH, height, COLOR_GREEN));
            
            // Конечная часть
            int capX = px - 3;
            buffer.add(new CmdRect(capX, py, CAP_WIDTH, CAP_HEIGHT, COLOR_CAP));
        }
    }

    public boolean isTop()
    {
        return isTop;
    }
}