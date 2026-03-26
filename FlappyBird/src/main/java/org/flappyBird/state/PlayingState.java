package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.component.GroundParallax;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.entity.Bird;
import org.flappyBird.entity.PipeColumn;
import org.flappyBird.render.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class PlayingState implements IState
{
    private final StateController controller;
    private final FullParallax parallax;

    private final Bird bird;
    private final List<PipeColumn> pipes = new ArrayList<>();

    private int score = 0;
    private float pipeSpawnTimer = 0;

    private static final int PIPE_MIN_GAP_Y = 150;
    private static final int PIPE_MAX_GAP_Y = 350;

    public PlayingState(StateController controller, FullParallax parallax)
    {
        this.controller = controller;
        this.bird = new Bird(120, 200);
        this.parallax = parallax;

        int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;
        pipes.add(new PipeColumn(400, 200, groundTopY));
    }

    public PlayingState(StateController controller)
    {
        this(controller, new FullParallax());
    }

    @Override public void onEnter()
    {
        // TODO: инициализация
    }
    @Override public void onExit()
    {
        // TODO: очистка
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        parallax.update(deltaMillis);
        bird.update(deltaMillis);
        pipes.forEach(p -> p.update(deltaMillis));
        pipes.removeIf(PipeColumn::isExpired);

        if (input.isJustPressed(GameAction.FLAP))
            bird.flap();

        Rectangle birdBounds = bird.getBounds();

        if (birdBounds.y >= MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT)
        {
            controller.setState(new MenuState(controller, parallax));  // TODO: Убрать костыль в onEnter()/onExit()
            System.out.println("GAME OVER (FALL)");
        }

        for (PipeColumn column : pipes)
        {
            // Если труба еще далеко справа
            if (column.getX() > bird.getX() + bird.getWidth())
                break;
            
            // Если мы тут, значит труба где-то в районе птицы
            if (column.checkCollision(birdBounds))
            {
                controller.setState(new MenuState(controller, parallax));  // TODO: Убрать костыль в onEnter()/onExit()
                System.out.println("GAME OVER");
            }

            // Проверка начисления очков по центру
            if (!column.isScored())
            {
                if (bird.getX() + (float) bird.getWidth() / 2 > column.getX() + (float) column.getWidth() / 2)
                {
                    score++;
                    column.setScored(true);
                }
            }
        }

        pipeSpawnTimer += (float) deltaMillis;
        if (pipeSpawnTimer > 2000)
        {
            pipeSpawnTimer = 0;
            int gapY = PIPE_MIN_GAP_Y + (int)(Math.random() * (PIPE_MAX_GAP_Y - PIPE_MIN_GAP_Y));
            int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;
            pipes.add(new PipeColumn(800, gapY, groundTopY));
        }
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        parallax.render(buffer, canvasWidth, canvasHeight);

        for (PipeColumn p : pipes)
            p.render(buffer);
        bird.render(buffer);

        /*
        // Хитбокс птицы (для отладки)
        var bound = bird.getBounds();
        buffer.add(new CmdRect(bound.x, bound.y, bound.width, bound.height, 0xFF0000)); // TODO: удалить
        */

        buffer.add(new CmdText("Score: " + score, 20, 30, 0xFFFFFF));
    }
}
