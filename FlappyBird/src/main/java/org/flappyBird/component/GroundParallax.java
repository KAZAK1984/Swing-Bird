package org.flappyBird.component;

import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;

import java.util.List;

public class GroundParallax implements IParallaxLayer
{
    public static final int GROUND_HEIGHT = 110; // Фиксированная константа!

    private static final int GROUND_COLOR = 0x8B5A2B;
    private static final int GRASS_COLOR = 0x58B957;

    private static final int BASE_GRASS_HEIGHT = 12;
    private static final int GRASS_SEGMENT_WIDTH = 12;

    private static final int[] GRASS_PATTERN = {0, 2, 4, 2, 0, -2, -4, -2};
    private static final int PATTERN_LENGTH = GRASS_PATTERN.length;
    private static final int PATTERN_PERIOD = GRASS_SEGMENT_WIDTH * PATTERN_LENGTH;

    private static final float SPEED_PX_PER_SEC = 80f;

    private double shift = 0;

    @Override
    public void update(double deltaMillis)
    {
        shift += SPEED_PX_PER_SEC * deltaMillis / 1000.0;

        if (shift >= PATTERN_PERIOD)
            shift -= PATTERN_PERIOD;
        else if (shift < 0)
            shift += PATTERN_PERIOD;
    }

    @Override
    public void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int groundY = canvasHeight - GROUND_HEIGHT;
        buffer.add(new CmdRect(0, groundY, canvasWidth, GROUND_HEIGHT, GROUND_COLOR));

        int startX = -GRASS_SEGMENT_WIDTH;
        int endX = canvasWidth + GRASS_SEGMENT_WIDTH;

        for (int worldX = startX; worldX < endX + shift; worldX += GRASS_SEGMENT_WIDTH)
        {
            int segX = (int) (worldX - shift);
            int idx = mod(worldX / GRASS_SEGMENT_WIDTH);

            int segHeight = BASE_GRASS_HEIGHT + GRASS_PATTERN[idx];
            int segY = groundY - segHeight;

            buffer.add(new CmdRect(segX, segY, GRASS_SEGMENT_WIDTH, segHeight, GRASS_COLOR));
        }
    }

    private int mod(int value)
    {
        int r = value % PATTERN_LENGTH;
        return r < 0 ? r + PATTERN_LENGTH : r;
    }
}