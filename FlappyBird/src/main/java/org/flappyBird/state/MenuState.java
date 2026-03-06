package org.flappyBird.state;

import java.awt.*;

public class MenuState implements IState
{
    private final StateController controller;
    private IState subState = null;

    public MenuState(StateController controller)
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
        // TODO: логика меню
        //  при нажатии на "Старт": controller.setState(new PlayingState(controller)));
        //  при нажатии на "Статистика": "заморозить" рендеринг, изменить subState, а далее работать с саб-окном StatisticsSubState через это состояние
    }

    @Override
    public void render(Graphics2D g)
    {
        // TODO: Отрисовка
    }
}

