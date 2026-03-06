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
        // TODO: Получаем статистику из файла

        // TODO: инициализация
    }
    @Override public void onExit()
    {
        // TODO: очистка
    }

    @Override
    public void update(double delta, int targetFPS)
    {
        // TODO: Вывод статистики
        //  при выходе: мягко перезапустить рендеринг родителя без потери данных, изменить subState в нём на NONE
    }

    @Override
    public void render(Graphics2D g)
    {
        // TODO: отрисовка поверх родительского состояния
    }
}

