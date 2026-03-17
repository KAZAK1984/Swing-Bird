package org.flappyBird.state;

import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;

import java.util.List;

public class PauseState implements IState
{
    private final StateController controller;

    public PauseState(StateController controller)
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
        // TODO: логика меню
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        // TODO: отрисовка поверх родительского состояния
    }
}

