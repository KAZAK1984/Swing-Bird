package org.flappyBird.state;

import org.flappyBird.render.*;
import java.util.List;

public class GameOverState implements IState
{
    private final StateController controller;

    public GameOverState(StateController controller)
    {
        this.controller = controller;
        // TODO: сохранение информации о счёте при его появлении
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
        // TODO: логика меню конца игры
        //  при повторе: изменить мейн состояние на PlayingState
        //  при выходе в меню: изменить мейн состояние на MenuState
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        // TODO: отрисовка поверх родительского состояния
    }
}

