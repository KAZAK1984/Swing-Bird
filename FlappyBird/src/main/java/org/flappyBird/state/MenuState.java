package org.flappyBird.state;

import org.flappyBird.component.BuildingParallax;
import org.flappyBird.component.CloudParallax;
import org.flappyBird.component.GroundParallax;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;

import java.util.List;

public class MenuState implements IState
{
    private final StateController controller;
    private final CloudParallax clouds = new CloudParallax();
    private final BuildingParallax buildings = new BuildingParallax();
    private final GroundParallax ground = new GroundParallax();

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
        controller.setState(new PlayingState(controller));
        // TODO: очистка
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        // TODO: логика меню
        //  при нажатии на "Старт": controller.setState(new PlayingState(controller));
        //  при нажатии на "Статистика": "заморозить" рендеринг, изменить subState, а далее работать с саб-окном StatisticsSubState через это состояние

        clouds.update(deltaMillis);
        buildings.update(deltaMillis);
        ground.update(deltaMillis);

        if (input.isJustPressed(GameAction.FLAP))
            onExit();
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        clouds.render(buffer, canvasWidth, canvasHeight);
        buildings.render(buffer, canvasWidth, canvasHeight);
        ground.render(buffer, canvasWidth, canvasHeight);
    }
}
