package org.flappyBird.state;

import org.flappyBird.render.*;

import java.util.List;

public class MenuState implements IState
{
    private final StateController controller;

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
    public void update(double deltaMillis)
    {
        // TODO: логика меню
        //  при нажатии на "Старт": controller.setState(new PlayingState(controller));
        //  при нажатии на "Статистика": "заморозить" рендеринг, изменить subState, а далее работать с саб-окном StatisticsSubState через это состояние
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        buffer.add(new CmdText("Flappy Bird", canvasWidth / 2, canvasHeight / 2, 0xFFFFFF));
    }
}
