package org.flappyBird.component;

import org.flappyBird.render.CmdSprite;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.render.SpriteType;

import java.util.List;

public class CloudParallax implements IParallaxLayer
{
    private static final int PARALLAX_DISTANCE = 60;
    private static final float SPEED_PX_PER_SEC = 30f;

    private static final int BASE_Y = 260;

    private static final int[] CLOUD_PATTERN = {0, 12, -8, 6, -4, 10, -6, 4};
    private static final int PATTERN_LENGTH = CLOUD_PATTERN.length;
    private static final int PATTERN_PERIOD = PARALLAX_DISTANCE * PATTERN_LENGTH;

    private double parallaxShift = 0;

    @Override
    public void update(double deltaMillis)
    {
        parallaxShift += SPEED_PX_PER_SEC * deltaMillis / 1000.0;

        if (parallaxShift >= PATTERN_PERIOD)
            parallaxShift -= PATTERN_PERIOD;
        else if (parallaxShift < 0)
            parallaxShift += PATTERN_PERIOD;
    }

    @Override
    public void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int cloudStartX = -PARALLAX_DISTANCE;
        int cloudEndX = canvasWidth + PARALLAX_DISTANCE;

        for (int worldX = cloudStartX; worldX < cloudEndX + parallaxShift; worldX += PARALLAX_DISTANCE)
        {
            int cloudX = (int) (worldX - parallaxShift);
            int patternIndex = mod(worldX / PARALLAX_DISTANCE);
            int cloudY = BASE_Y + CLOUD_PATTERN[patternIndex];

            buffer.add(new CmdSprite(SpriteType.CLOUD, cloudX, cloudY, 0));
        }
    }

    private int mod(int value)
    {
        int r = value % PATTERN_LENGTH;
        return r < 0 ? r + PATTERN_LENGTH : r;
    }
}