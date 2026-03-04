package org.flappyBird.state;

import java.awt.*;

public class PauseSubState implements IState
{
    private final StateController controller;

    public PauseSubState(StateController controller)
    {
        this.controller = controller;
    }

    @Override public void onEnter()
    {
        /* инициализация */
    }
    @Override public void onExit()
    {
        /* очистка */
    }

    @Override
    public void update(double delta)
    {
        // логика меню
        // при повторе: изменить мейн состояние на PlayingState
        // при выходе в меню: изменить мейн состояние на MenuState
        // при возобновлении: мягко перезапустить рендеринг родителя без потери данных, изменить subState в нём на NONE
    }

    @Override
    public void render(Graphics2D g)
    {
        // отрисовка поверх родительского состояния
    }
}

