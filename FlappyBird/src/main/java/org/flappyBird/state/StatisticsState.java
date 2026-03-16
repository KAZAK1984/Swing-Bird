package org.flappyBird.state;

import org.flappyBird.render.*;

import java.util.List;

public class StatisticsState implements IState
{
    private final StateController controller;

    public StatisticsState(StateController controller)
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
    public void update(double deltaMillis)
    {
        // TODO: Вывод статистики
        //  при выходе: мягко перезапустить рендеринг родителя без потери данных, изменить subState в нём на NONE
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        // TODO: отрисовка поверх родительского состояния
    }
}

