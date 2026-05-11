package org.swingBird.state;

import org.swingBird.component.FullParallax;
import org.swingBird.component.GroundParallax;
import org.swingBird.component.MedalBadge;
import org.swingBird.component.UIButton;
import org.swingBird.component.UIManager;
import org.swingBird.entity.Bird;
import org.swingBird.input.GameAction;
import org.swingBird.input.InputSnapshot;
import org.swingBird.render.*;

import java.awt.*;
import java.util.List;

public class ResetState implements IState
{
    private static final int OUT_TIME = 300;

    private final StateController controller;
    private final FullParallax parallax;
    private final UIManager uiManager = new UIManager();
    private final Bird bird;
    private final MedalBadge medalBadge;

    private float deathTimer = 0;
    private boolean isBirdOnGround = false;
    private boolean isTimeOut = false;

    public ResetState(StateController controller, FullParallax parallax, Bird bird, MedalBadge scoreBadge)
    {
        this.controller = controller;
        this.parallax = parallax;
        this.bird = bird;
        this.medalBadge = scoreBadge;
    }

    @Override public void onEnter()
    {
        // Инициализируем кнопки нулями, их размеры будут установлены в buildFrame() в зависимости от размера окна
        uiManager.addButton(new UIButton(0, 0, 0, 0, "RESTART", this::restartGame));
        uiManager.addButton(new UIButton(0, 0, 0, 0, "MAIN MENU", this::goToMainMenu));
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        if (!isBirdOnGround)
        {
            bird.update(deltaMillis);

            Rectangle birdBounds = bird.getBounds();
            int groundTopY = MasterRenderer.VIRTUAL_HEIGHT - GroundParallax.GROUND_HEIGHT;

            if (birdBounds.getY() + birdBounds.getHeight() >= groundTopY)
            {
                bird.landAt(groundTopY);
                isBirdOnGround = true;
            }

            return;
        }

        if (!isTimeOut)
        {
            deathTimer += (float) deltaMillis;
            if (deathTimer > OUT_TIME)
                isTimeOut = true;
            return;
        }

        if (input.isJustPressed(GameAction.PAUSE))
            goToMainMenu();

        uiManager.update(input);
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        if (!isBirdOnGround || !isTimeOut)
            return;

        int panelWidth = canvasWidth / 2;
        int panelHeight = canvasHeight / 2;
        int panelX = (canvasWidth - panelWidth) / 2;
        int panelY = (canvasHeight - panelHeight) / 2;

        buffer.add(new CmdRect(panelX, panelY, panelWidth, panelHeight, 0x101010));
        buffer.add(new CmdRect(panelX + 4, panelY + 4, panelWidth - 8, 36, 0x1E1E1E));
        buffer.add(new CmdText("GAME OVER", panelX + 20, panelY + 27, 0xFFFFFF));

        medalBadge.render(buffer, panelX + (panelWidth / 2) + 15, panelY + 72, panelWidth / 2 - 50, panelHeight - 104);

        uiManager.changeButtonsBoundsVertical(panelX + 15, panelY + 42, panelWidth / 2, panelHeight - 44);
        uiManager.render(buffer);
    }

    private void restartGame()
    {
        controller.setState(new PlayingState(controller, parallax));
        System.out.println("Restarting...");
    }

    private void goToMainMenu()
    {
        controller.setState(new MenuState(controller, parallax));
        System.out.println("Returning to Main Menu...");
    }
}
