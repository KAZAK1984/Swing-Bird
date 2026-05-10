package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.component.GroundParallax;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.CmdRect;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.component.UIButton;
import org.flappyBird.component.UIManager;
import org.flappyBird.logic.StatsRepository;

import java.util.List;

public class MenuState implements IState
{
    private final StateController controller;
    private final FullParallax parallax;
    private final UIManager uiManager = new UIManager();
    private final StatsRepository statsRepository;

    public MenuState(StateController controller, FullParallax parallax, StatsRepository statsRepository)
    {
        this.controller = controller;
        this.parallax = parallax;
        this.statsRepository = statsRepository;
    }

    public MenuState(StateController controller, StatsRepository statsRepository)
    {
        this(controller, new FullParallax(), statsRepository);
    }

    @Override public void onEnter()
    {
        // Инициализируем кнопки нулями, их размеры будут установлены в buildFrame() в зависимости от размера окна
        uiManager.addButton(new UIButton(0, 0, 0, 0, "PLAY", this::startGame));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "STATISTICS", this::openStats));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "EXIT", this::exitGame));
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

        uiManager.changeButtonsBounds(0, canvasHeight - GroundParallax.GROUND_HEIGHT, canvasWidth, GroundParallax.GROUND_HEIGHT);
        uiManager.render(buffer);
    }

    private void startGame()
    {
        System.out.println("Transitioning to PlayingState...");
        controller.setState(new PlayingState(controller, parallax, statsRepository));
    }

    private void openStats()
    {
        System.out.println("Opening Statistics...");
        controller.pushState(new StatisticsState(controller, statsRepository));
    }

    private void exitGame()
    {
        System.out.println("Saving data and exiting...");
        // TODO: saveTelemetryToDisk();
        System.exit(0);
    }
}
