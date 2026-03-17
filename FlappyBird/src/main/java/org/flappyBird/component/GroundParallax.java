package org.flappyBird.component;

import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;

import java.util.List;

/**
 * Параллакс-слой земли с движущимся травяным паттерном.
 */
public class GroundParallax
{
    private static final double GROUND_HEIGHT_PCT = 0.18;
    private static final int GROUND_HEIGHT_MIN = 60;
    private static final int GROUND_HEIGHT_MAX = 160;

    private static final int GROUND_COLOR = 0x8B5A2B; // коричневый
    private static final int GRASS_COLOR = 0x58B957;  // зелёный

    private static final int BASE_GRASS_HEIGHT = 12;
    private static final int GRASS_SEGMENT_WIDTH = 12;

    private static final int[] GRASS_PATTERN = {0, 2, 4, 2, 0, -2, -4, -2};
    private static final int PATTERN_LENGTH = GRASS_PATTERN.length;
    private static final int PATTERN_PERIOD = GRASS_SEGMENT_WIDTH * PATTERN_LENGTH;

    private static final float SPEED_PX_PER_SEC = 80f; // быстрее переднего плана

    private double shift = 0;

    public void update(double deltaMillis)
    {
        shift += SPEED_PX_PER_SEC * deltaMillis / 1000.0;
        if (shift >= PATTERN_PERIOD)
        {
            shift -= PATTERN_PERIOD;
        }
        else if (shift < 0)
        {
            shift += PATTERN_PERIOD;
        }
    }

    public void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int groundHeight = resolveGroundHeight(canvasHeight);
        double scale = groundHeight / 70.0;

        int grassHeightBase = (int) Math.max(6, BASE_GRASS_HEIGHT * scale);

        int groundY = canvasHeight - groundHeight;
        buffer.add(new CmdRect(0, groundY, canvasWidth, groundHeight, GROUND_COLOR));

        int startX = -GRASS_SEGMENT_WIDTH;
        int endX = canvasWidth + GRASS_SEGMENT_WIDTH;
        for (int worldX = startX; worldX < endX + shift; worldX += GRASS_SEGMENT_WIDTH)
        {
            int segX = (int) (worldX - shift);
            int idx = mod(worldX / GRASS_SEGMENT_WIDTH);
            int offset = GRASS_PATTERN[idx];
            int segHeight = grassHeightBase + offset;
            int segY = groundY - segHeight;

            buffer.add(new CmdRect(segX, segY, GRASS_SEGMENT_WIDTH, segHeight, GRASS_COLOR));
        }
    }

    public static int resolveGroundHeight(int canvasHeight)
    {
        int h = (int) (canvasHeight * GROUND_HEIGHT_PCT);
        if (h < GROUND_HEIGHT_MIN) h = GROUND_HEIGHT_MIN;
        if (h > GROUND_HEIGHT_MAX) h = GROUND_HEIGHT_MAX;
        return h;
    }

    private int mod(int value)
    {
        int r = value % GroundParallax.PATTERN_LENGTH;
        return r < 0 ? r + GroundParallax.PATTERN_LENGTH : r;
    }
}
