package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.component.MedalBadge;
import org.flappyBird.entity.PipeColumn;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.logic.GameWorld;
import org.flappyBird.render.*;

import java.util.List;

public class PlayingState implements IState
{
    private final StateController controller;
    private final GameWorld world;

    private int cachedScore = Integer.MIN_VALUE;
    private String cachedScoreText = "Score: 0";

    public PlayingState(StateController controller, FullParallax parallax)
    {
        this.controller = controller;
        this.world = new GameWorld(parallax);
    }

    @Override public void onEnter()
    {
        // Всё инициализируется в конструкторе.
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        world.update(deltaMillis);

        if (input.isJustPressed(GameAction.FLAP))
            world.flapBird();

        if (input.isJustPressed(GameAction.PAUSE))
        {
            controller.pushState(new PauseState(controller, world.getParallax()));
            System.out.println("PAUSED");
        }

        if (world.isGameOver())
        {
            MedalBadge medalBadge = new MedalBadge(world.getScore());
            controller.pushState(new ResetState(controller, world.getParallax(), world.getBird(), medalBadge));
            System.out.println("GAME OVER");
        }
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int score = world.getScore();

        if (score != cachedScore)
            refreshScoreCache(score);

        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        world.getParallax().render(buffer, canvasWidth, canvasHeight);

        for (PipeColumn p : world.getPipes())
            p.render(buffer);

        world.getBird().render(buffer);

        buffer.add(new CmdText(cachedScoreText, 20, 30, 0xFFFFFF));
    }

    public int getScore()
    {
        return world.getScore();
    }

    private void refreshScoreCache(int score)
    {
        cachedScore = score;
        cachedScoreText = "Score: " + score;
    }
}
