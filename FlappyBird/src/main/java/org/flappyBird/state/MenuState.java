package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.component.UIButton;
import org.flappyBird.component.UIManager;
import org.flappyBird.render.MasterRenderer;

import java.util.List;

public class MenuState implements IState
{
    private final StateController controller;
    private final FullParallax parallax;

    private final UIManager uiManager = new UIManager();

    public MenuState(StateController controller, FullParallax parallax)
    {
        this.controller = controller;
        this.parallax = parallax;
    }

    public MenuState(StateController controller)
    {
        this(controller, new FullParallax());
    }

    @Override public void onEnter()
    {
        // Числа затычки для избежания возможных ошибок при неправильной отрисовке
        // Адекватное расположение кнопок будет выполнено в buildFrame
        uiManager.addButton(new UIButton(100, 2 * (MasterRenderer.VIRTUAL_HEIGHT / 3), 100, 100, "PLAY", this::startGame));
        uiManager.addButton(new UIButton(300, 2 * (MasterRenderer.VIRTUAL_HEIGHT / 3), 100, 100, "STATISTICS", this::openStats));
        uiManager.addButton(new UIButton(500, 2 * (MasterRenderer.VIRTUAL_HEIGHT / 3), 100, 100, "EXIT", this::exitGame));
    }
    @Override public void onExit()
    {
        // TODO: очистка при необходимости
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        parallax.update(deltaMillis);
        uiManager.update(input);
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x4CBDFD));
        parallax.render(buffer, canvasWidth, canvasHeight);

        uiManager.changeButtonsBounds(canvasWidth, canvasHeight);
        uiManager.render(buffer);
    }

    private void startGame()
    {
        System.out.println("Transitioning to PlayingState...");
        controller.setState(new PlayingState(controller, parallax));
    }

    private void openStats()
    {
        System.out.println("Opening Statistics...");
        // controller.pushState(new StatisticsSubState(controller));
    }

    private void exitGame()
    {
        System.out.println("Saving data and exiting...");
        // saveTelemetryToDisk();
        System.exit(0);
    }
}
