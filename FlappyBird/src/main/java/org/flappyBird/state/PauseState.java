package org.flappyBird.state;

import org.flappyBird.component.FullParallax;
import org.flappyBird.component.UIButton;
import org.flappyBird.component.UIManager;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;

import java.util.List;

public class PauseState implements IState
{
    private final StateController controller;
    private final FullParallax parallax;
    private final UIManager uiManager = new UIManager();

    public PauseState(StateController controller, FullParallax parallax)
    {
        this.controller = controller;
        this.parallax = parallax;
    }

    @Override public void onEnter()
    {
        // Инициализируем кнопки нулями, их размеры будут установлены в buildFrame() в зависимости от размера окна
        uiManager.addButton(new UIButton(0, 0, 0, 0, "CONTINUE", this::resumeGame));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "RESTART", this::restartGame));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "MAIN MENU", this::goToMainMenu));
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        if (input.isJustPressed(GameAction.PAUSE))
        {
            resumeGame();
            return;
        }

        uiManager.update(input);
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int panelWidth = canvasWidth / 2;
        int panelHeight = canvasHeight / 2;
        int panelX = (canvasWidth - panelWidth) / 2;
        int panelY = (canvasHeight - panelHeight) / 2;

        buffer.add(new CmdRect(panelX, panelY, panelWidth, panelHeight, 0x101010));
        buffer.add(new CmdRect(panelX + 4, panelY + 4, panelWidth - 8, 36, 0x1E1E1E));
        buffer.add(new CmdText("Paused...", panelX + 20, panelY + 27, 0xFFFFFF));

        uiManager.changeButtonsBoundsVertical(panelX, panelY + 42, panelWidth, panelHeight - 44);
        uiManager.render(buffer);
    }

    private void resumeGame()
    {
        controller.popState();
        System.out.println("Resuming game...");
    }

    private void restartGame()
    {
        controller.setState(new PlayingState(controller, parallax));
        System.out.println("Restarting game...");
    }

    private void goToMainMenu()
    {
        controller.setState(new MenuState(controller, parallax));
        System.out.println("Returning to Main Menu...");
    }
}
