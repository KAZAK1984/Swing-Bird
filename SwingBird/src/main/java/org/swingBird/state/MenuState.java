package org.swingBird.state;

import org.swingBird.component.FullParallax;
import org.swingBird.component.GroundParallax;
import org.swingBird.input.InputSnapshot;
import org.swingBird.render.CmdRect;
import org.swingBird.render.IRenderCmd;
import org.swingBird.component.UIButton;
import org.swingBird.component.UIManager;

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
        // Инициализируем кнопки нулями, их размеры будут установлены в buildFrame() в зависимости от размера окна
        uiManager.addButton(new UIButton(0, 0, 0, 0, "PLAY", this::startGame));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "STATISTICS", this::openStats));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "EXIT", this::exitGame));
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

        uiManager.changeButtonsBounds(0, canvasHeight - GroundParallax.GROUND_HEIGHT, canvasWidth, GroundParallax.GROUND_HEIGHT);
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
        controller.pushState(new StatisticsState(controller));
    }

    private void exitGame()
    {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
