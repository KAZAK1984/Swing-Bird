package org.flappyBird.state;

import java.awt.*;

public class GameOverSubState implements IState
{
    private final StateController controller;

    public GameOverSubState(StateController controller)
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
    public void update(double delta, int targetFPS)
    {
        // TODO: логика меню конца игры
        //  при повторе: изменить мейн состояние на PlayingState
        //  при выходе в меню: изменить мейн состояние на MenuState
    }

    @Override
    public void render(Graphics2D g)
    {
        // TODO: отрисовка поверх родительского состояния
    }
}

