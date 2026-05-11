package org.flappyBird.logic;

import org.flappyBird.component.GroundParallax;
import org.flappyBird.entity.Bird;
import org.flappyBird.entity.Pipe;
import org.flappyBird.entity.PipeColumn;
import org.flappyBird.render.MasterRenderer;

import java.util.List;

public class PipeSpawner
{
    private static final float GAP_MIN_MULTIPLIER = 3.8f;
    private static final float GAP_MAX_MULTIPLIER = 5.0f;

    private static final int SPAWN_X_MIN = 1600;
    private static final int SPAWN_X_MAX = 2000;

    private static final int INITIAL_SPAWN_TIME_MS = 1500;
    private static final int INITIAL_SPAWN_JITTER_PX = 80;

    private static final int MIN_COLUMN_DISTANCE = 220;

    private static final int MIN_SPAWN_INTERVAL_MS = 900;
    private static final int MAX_SPAWN_INTERVAL_MS = 1500;

    private static final int INITIAL_FILL_SPREAD = 260;
    private static final int INITIAL_FILL_MAX_X = SPAWN_X_MIN - MIN_COLUMN_DISTANCE;

    private float timer = 0;
    private float nextIntervalMs = pickNextInterval();

    public void update(double deltaMillis, List<PipeColumn> pipes, int birdHeight)
    {
        timer += (float) deltaMillis;
        if (timer >= nextIntervalMs)
        {
            int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;
            PipeColumn column = createPipeColumn(groundTopY, birdHeight, pipes, SPAWN_X_MIN, SPAWN_X_MAX);
            if (column == null)
                return;

            timer -= nextIntervalMs;
            nextIntervalMs = pickNextInterval();
            pipes.add(column);
        }
    }

    public PipeColumn createInitialPipeColumn(int groundTopY, Bird bird)
    {
        int birdHeight = bird.getHeight();
        int birdWidth = 34;
        float birdX = bird.getX();

        float speedPxPerMs = Pipe.getSpeed() / 16.0f;
        float baseDistance = speedPxPerMs * INITIAL_SPAWN_TIME_MS;

        int minX = Math.round(birdX + birdWidth + baseDistance - INITIAL_SPAWN_JITTER_PX);
        int maxX = Math.round(birdX + birdWidth + baseDistance + INITIAL_SPAWN_JITTER_PX);

        return createPipeColumn(groundTopY, birdHeight, null, minX, maxX);
    }

    public void seedInitialPipes(List<PipeColumn> pipes, int groundTopY, Bird bird)
    {
        PipeColumn first = createInitialPipeColumn(groundTopY, bird);
        if (first == null)
            return;

        pipes.add(first);

        while (findRightmostX(pipes) < INITIAL_FILL_MAX_X)
        {
            int rightmostX = findRightmostX(pipes);
            int minX = rightmostX + MIN_COLUMN_DISTANCE;
            int maxX = Math.min(minX + INITIAL_FILL_SPREAD, INITIAL_FILL_MAX_X);

            PipeColumn column = createPipeColumn(groundTopY, bird.getHeight(), pipes, minX, maxX);
            if (column == null)
                break;

            pipes.add(column);
        }
    }

    private PipeColumn createPipeColumn(int groundTopY, int birdHeight, List<PipeColumn> pipes, int spawnMinX, int spawnMaxX)
    {
        int effectiveMinX = Math.max(spawnMinX, findRightmostX(pipes) + MIN_COLUMN_DISTANCE);
        if (effectiveMinX > spawnMaxX)
            return null;

        int gapSize = Math.round(randomRange(birdHeight * GAP_MIN_MULTIPLIER, birdHeight * GAP_MAX_MULTIPLIER));
        float halfGap = gapSize / 2.0f;

        float safeMaxGapY = Math.max(halfGap, groundTopY - halfGap);

        float minGapY = halfGap + gapSize;
        float maxGapY = safeMaxGapY - gapSize;
        if (maxGapY < minGapY)
        {
            minGapY = halfGap;
            maxGapY = safeMaxGapY;
        }

        float gapY = randomRange(minGapY, maxGapY);

        int spawnX = randomInt(effectiveMinX, spawnMaxX);
        return new PipeColumn(spawnX, gapY, gapSize, groundTopY);
    }

    private static int findRightmostX(List<PipeColumn> pipes)
    {
        if (pipes == null || pipes.isEmpty())
            return 0;

        int rightmostX = 0;
        for (PipeColumn column : pipes)
            rightmostX = Math.max(rightmostX, Math.round(column.getX()));

        return rightmostX;
    }

    private static float pickNextInterval()
    {
        return randomRange(MIN_SPAWN_INTERVAL_MS, MAX_SPAWN_INTERVAL_MS);
    }

    private static int randomInt(int min, int max)
    {
        if (max <= min)
            return min;

        return min + (int) (Math.random() * (max - min + 1));
    }

    private static float randomRange(float min, float max)
    {
        if (max <= min)
            return min;

        return min + (float) (Math.random() * (max - min));
    }
}