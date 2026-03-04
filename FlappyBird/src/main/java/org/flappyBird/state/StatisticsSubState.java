package org.flappyBird.state;

import java.awt.*;

public class StatisticsSubState implements IState
{
    private final StateController controller;

    public StatisticsSubState(StateController controller)
    {
        this.controller = controller;
    }

    @Override public void onEnter()
    {
        // Получаем статистику из файла
        /* инициализация */
    }
    @Override public void onExit()
    {
        /* очистка */
    }

    @Override
    public void update(double delta)
    {
        // Вывод статистики
        // при выходе: мягко перезапустить рендеринг родителя без потери данных, изменить subState в нём на NONE
    }

    @Override
    public void render(Graphics2D g)
    {
        // отрисовка поверх родительского состояния
    }
}

