package org.flappyBird;

import org.flappyBird.state.MenuState;
import org.flappyBird.state.StateController;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class Scene
{
    private static final int TARGET_FPS = 120;
    private static final long OPTIMAL_NANOS = 1_000_000_000L / TARGET_FPS;

    private final StateController stateController;
    private final JPanel view;
    private volatile boolean running;

    public Scene()
    {
        stateController = new StateController();
        stateController.setState(new MenuState(stateController));

        view = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                stateController.render((Graphics2D) g);
            }
        };
    }

    public JComponent getView()
    {
        return view;
    }

    public void start()
    {
        if (running)
            return;

        running = true;
        Thread gameThread = new Thread(this::gameLoop, "game-loop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void gameLoop()
    {
        var lastTime = Instant.now();

        while (running)
        {
            long delta = Duration.between(lastTime, Instant.now()).toMillis();
            lastTime = Instant.now();

            try
            {
                stateController.update(delta, TARGET_FPS);
                SwingUtilities.invokeLater(view::repaint);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                running = false;
            }

            long frameNanos = Duration.between(lastTime, Instant.now()).toNanos();
            long sleepNanos = OPTIMAL_NANOS - frameNanos;
            if (sleepNanos > 0)
            {
                try
                {
                    Thread.sleep(sleepNanos / 1_000_000, (int)(sleepNanos % 1_000_000));
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        }
    }
}
