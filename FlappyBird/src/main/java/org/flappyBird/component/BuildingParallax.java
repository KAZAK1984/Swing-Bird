package org.flappyBird.component;

import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;

import java.util.List;

/**
 * Параллакс-слой зданий: повторяемый узор и ограничение подряд идущих одинаковых паттернов.
 */
public class BuildingParallax
{
    private static final int BUILDING_WIDTH = 60;
    private static final float SPEED_PX_PER_SEC = 45f;

    // Паттерн: высота (0 = пропуск/нет здания)
    private static final int[] HEIGHT_PATTERN =
    {
            120, 150, 0, 170, 140, 0, 160, 130,      // блок 1
            145, 165, 0, 135, 155, 0, 175, 125,      // блок 2
            138, 158, 0, 148, 168, 0, 142, 152       // блок 3
    };
    private static final int[] COLOR_PATTERN =
    {
            0x8FA5B5, 0x7F95A5, 0x0, 0x9FB5C5,        // блок 1
            0x8FA5B5, 0x0, 0x95ADB8, 0x85959F,
            0x8DB3C3, 0x7D93A3, 0x0, 0x9DAABC,        // блок 2
            0x8DB3C3, 0x0, 0x93A8B6, 0x83939D,
            0x8FA9BD, 0x7F9BAD, 0x0, 0x9FB9CD,        // блок 3
            0x8FA9BD, 0x0, 0x95B1BA, 0x859196
    };
    private static final int PATTERN_LENGTH = HEIGHT_PATTERN.length;
    private static final int PATTERN_PERIOD = BUILDING_WIDTH * PATTERN_LENGTH;

    private static final int WINDOW_COLOR = 0xF7F4D7;
    private static final int WINDOW_GAP = 10;
    private static final int WINDOW_SIZE = 6;

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
        int groundHeight = GroundParallax.resolveGroundHeight(canvasHeight);
        double heightScale = clamp(canvasHeight / 480.0);

        int baseY = canvasHeight - groundHeight;
        int startX = -BUILDING_WIDTH;
        int endX = canvasWidth + BUILDING_WIDTH;

        for (int worldX = startX; worldX < endX + parallaxShift; worldX += BUILDING_WIDTH)
        {
            int idx = mod(worldX / BUILDING_WIDTH);
            int height = (int) (HEIGHT_PATTERN[idx] * heightScale);

            if (height == 0)
                continue; // пропуск - нет здания

            int color = COLOR_PATTERN[idx];
            int x = (int) (worldX - parallaxShift);
            int y = baseY - height;

            buffer.add(new CmdRect(x, y, BUILDING_WIDTH, height, color));
            addWindows(buffer, x, y, height);
        }
    }

    private void addWindows(List<IRenderCmd> buffer, int x, int yTop, int height)
    {
        int usableHeight = height - WINDOW_GAP * 2;
        int rows = Math.max(1, usableHeight / (WINDOW_SIZE + WINDOW_GAP));
        int cols = (BUILDING_WIDTH - WINDOW_GAP * 2) / (WINDOW_SIZE + WINDOW_GAP);

        for (int row = 0; row < rows; row++)
        {
            int wy = yTop + WINDOW_GAP + row * (WINDOW_SIZE + WINDOW_GAP);
            for (int col = 0; col < cols; col++)
            {
                int wx = x + WINDOW_GAP + col * (WINDOW_SIZE + WINDOW_GAP);

                buffer.add(new CmdRect(wx, wy, WINDOW_SIZE, WINDOW_SIZE, WINDOW_COLOR));
            }
        }
    }

    private int mod(int value)
    {
        int r = value % BuildingParallax.PATTERN_LENGTH;
        return r < 0 ? r + BuildingParallax.PATTERN_LENGTH : r;
    }

    private double clamp(double v)
    {
        return v < 0.75 ? 0.75 : Math.min(v, 2.1);
    }
}
