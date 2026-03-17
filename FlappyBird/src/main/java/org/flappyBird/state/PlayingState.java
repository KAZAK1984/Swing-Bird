package org.flappyBird.state;

import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;

import java.util.List;

public class PlayingState implements IState
{
    private final StateController controller;
    private int birdX = 120;
    private int birdY = 200;
    private int score = 0;

    public PlayingState(StateController controller)
    {
        this.controller = controller;
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
        if (input.isJustPressed(GameAction.FLAP))
            birdY = Math.max(0, birdY - 50);

        birdY += (int)(0.2 * deltaMillis);
        // TODO: логика игры
        //  при проигрыше: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном GameOverSubState через это состояние
        //  при паузе: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном PauseSubState через это состояние
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));

        buffer.add(new CmdSprite(1, birdX, birdY, 0f));

        buffer.add(new CmdText("Score: " + score, 20, 30, 0xFFFFFF));
    }
}
