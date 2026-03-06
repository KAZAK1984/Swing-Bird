package org.flappyBird;

import org.flappyBird.state.MenuState;
import org.flappyBird.state.StateController;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class Scene
{
    private final StateController stateController;

    public Scene()
    {
        stateController = new StateController();
        stateController.setState(new MenuState(stateController));

        GameLoop();
    }

    private void GameLoop()
    {
        final int TARGET_FPS = 120;
        var lastTime = Instant.now();

        for (;;)
        {
            long delta = Duration.between(lastTime, Instant.now()).toMillis();

            try
            {
                stateController.update(delta, TARGET_FPS);
                // TODO: render
            }
            catch (Exception e)
            {
                // TODO: Вывод ошибки в мелком окошке а-ля winError
                break;
            }

            lastTime = Instant.now();
        }
    }
}
