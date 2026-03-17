package org.flappyBird.component;

import org.flappyBird.render.CmdSprite;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.render.SpriteType;

import java.util.List;

/**
 * Визуальный компонент для параллакс-облаков.
 * Константы подобраны вручную для бесшовного и красивого вида.
 */
public class CloudParallax
{
    private static final int PARALLAX_DISTANCE = 60;
    private static final float SPEED_PX_PER_SEC = 30f;

    private static final int BASE_Y_MIN = 180;
    private static final double BASE_Y_PCT = 0.60;
    private static final double BASE_Y_MAX_PCT = 0.75;

    private static final int TALLEST_BUILDING = 175;

    private static final int[] CLOUD_PATTERN = {0, 12, -8, 6, -4, 10, -6, 4};
    private static final int PATTERN_LENGTH = CLOUD_PATTERN.length;
    private static final int PATTERN_PERIOD = PARALLAX_DISTANCE * PATTERN_LENGTH;

    private double parallaxShift = 0;

    public void update(double deltaMillis)
    {
        parallaxShift += SPEED_PX_PER_SEC * deltaMillis / 1000.0;

        if (parallaxShift >= PATTERN_PERIOD)
            parallaxShift -= PATTERN_PERIOD;
        else if (parallaxShift < 0)
            parallaxShift += PATTERN_PERIOD;
    }

    public void render(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int cloudStartX = -PARALLAX_DISTANCE;
        int cloudEndX = canvasWidth + PARALLAX_DISTANCE;

        int baseY = getBaseY(canvasHeight);

        for (int worldX = cloudStartX; worldX < cloudEndX + parallaxShift; worldX += PARALLAX_DISTANCE)
        {
            int cloudX = (int) (worldX - parallaxShift);
            int patternIndex = mod(worldX / PARALLAX_DISTANCE);
            int shiftY = CLOUD_PATTERN[patternIndex];

            int cloudY = baseY + shiftY;
            buffer.add(new CmdSprite(SpriteType.CLOUD, cloudX, cloudY, 0));
        }
    }

    private int getBaseY(int canvasHeight)
    {
        int baseY = (int) (canvasHeight * BASE_Y_PCT);
        baseY = Math.max(baseY, BASE_Y_MIN);
        baseY = Math.min(baseY, (int) (canvasHeight * BASE_Y_MAX_PCT));

        // Привязка к пикам зданий: вычисляем максимальную высоту зданий с учётом масштаба и земли
        int groundHeight = GroundParallax.resolveGroundHeight(canvasHeight);
        double buildingScale = clamp(canvasHeight / 480.0);
        int peakY = canvasHeight - groundHeight - (int) (TALLEST_BUILDING * buildingScale);
        int peakCap = Math.max(0, peakY - 10);
        baseY = Math.min(baseY, peakCap);
        if (baseY < 0) baseY = 0;
        return baseY;
    }

    private double clamp(double v)
    {
        return v < 0.75 ? 0.75 : Math.min(v, 1.5);
    }

    private int mod(int value)
    {
        int r = value % CloudParallax.PATTERN_LENGTH;
        return r < 0 ? r + CloudParallax.PATTERN_LENGTH : r;
    }
}
