package org.flappyBird.state;

import java.awt.*;

public class PlayingState implements IState
{
    private final StateController controller;
    private IState subState = null;

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
    public void update(double delta, int targetFPS)
    {
        // TODO: логика игры
        //  при проигрыше: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном GameOverSubState через это состояние
        //  при паузе: "заморозить" рендеринг, изменить subState, а далее работать с саб-окном PauseSubState через это состояние
    }

    @Override
    public void render(Graphics2D g)
    {
        // TODO: отрисовка
    }
}

