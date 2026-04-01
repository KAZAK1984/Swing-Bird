package org.flappyBird.logic;

import org.flappyBird.component.GroundParallax;
import org.flappyBird.entity.PipeColumn;
import org.flappyBird.render.MasterRenderer;

import java.util.List;

public class PipeSpawner
{
    private static final int PIPE_MIN_GAP_Y = 150;
    private static final int PIPE_MAX_GAP_Y = 350;
    private static final int SPAWN_INTERVAL_MS = 2000;

    private float timer = 0;

    public void update(double deltaMillis, List<PipeColumn> pipes)
    {
        timer += (float) deltaMillis;
        if (timer > SPAWN_INTERVAL_MS)
        {
            timer = 0;
            int gapY = PIPE_MIN_GAP_Y + (int)(Math.random() * (PIPE_MAX_GAP_Y - PIPE_MIN_GAP_Y));
            int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;
            pipes.add(new PipeColumn(800, gapY, groundTopY));
        }
    }
}