package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;

import java.util.List;

public class MenuState implements IState
{
    private final StateController controller;
    private final FullParallax parallax;

    public MenuState(StateController controller, FullParallax parallax)
    {
        this.controller = controller;
        this.parallax = parallax;
    }

    public MenuState(StateController controller)
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

        if (input.isJustPressed(GameAction.PAUSE))
        {
            controller.setState(new PlayingState(controller, parallax));  // TODO: Убрать костыль в onEnter()/onExit()
        }
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        parallax.render(buffer, canvasWidth, canvasHeight);
    }
}
