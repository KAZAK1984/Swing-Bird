package org.swingBird.logic;

import org.swingBird.component.FullParallax;
import org.swingBird.component.GroundParallax;
import org.swingBird.entity.Bird;
import org.swingBird.entity.PipeColumn;
import org.swingBird.render.MasterRenderer;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GameWorld
{
    private final Bird bird;
    private final List<PipeColumn> pipes = new ArrayList<>();
    private final FullParallax parallax;
    private final PipeSpawner spawner;

    private int score = 0;
    private boolean isGameOver = false;

    public GameWorld(FullParallax parallax)
    {
        this.parallax = parallax;
        this.bird = new Bird(120, 200);
        this.spawner = new PipeSpawner();

        int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;
        spawner.seedInitialPipes(pipes, groundTopY, bird);
    }

    public void update(double deltaMillis)
    {
        if (isGameOver)
            return;

        parallax.update(deltaMillis);
        bird.update(deltaMillis);

        pipes.forEach(p -> p.update(deltaMillis));
        pipes.removeIf(PipeColumn::isExpired);

        spawner.update(deltaMillis, pipes, bird.getHeight());
        checkCollisionsAndScore();
    }

    public void flapBird()
    {
        if (!isGameOver)
            bird.flap();
    }

    private void checkCollisionsAndScore()
    {
        Rectangle birdBounds = bird.getBounds();
        int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;

        if (birdBounds.getY() + birdBounds.getHeight() >= groundTopY)
        {
            bird.landAt(groundTopY);
            isGameOver = true;
            return;
        }

        for (PipeColumn column : pipes)
        {
            if (column.getX() > birdBounds.getX() + birdBounds.getWidth())
                break;

            if (column.checkCollision(birdBounds))
            {
                isGameOver = true;
                return;
            }

            if (!column.isScored())
            {
                if (birdBounds.getX() + birdBounds.getWidth() / 2 > column.getX() + (float) column.getWidth() / 2)
                {
                    score++;
                    column.setScored(true);
                }
            }
        }
    }

    public Bird getBird() { return bird; }
    public List<PipeColumn> getPipes() { return pipes; }
    public FullParallax getParallax() { return parallax; }
    public int getScore() { return score; }
    public boolean isGameOver() { return isGameOver; }
}