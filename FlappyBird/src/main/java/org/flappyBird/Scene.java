package org.flappyBird;

import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.*;
import org.flappyBird.state.MenuState;
import org.flappyBird.state.StateController;
import org.flappyBird.input.InputPoller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene
{
    private static final int TARGET_FPS = 144;
    private static final long OPTIMAL_NANOS = 1_000_000_000L / TARGET_FPS;

    private final InputPoller inputPoller;
    private final StateController stateController;
    private final MasterRenderer masterRenderer;
    private final JPanel view;

    private volatile boolean running;
    private volatile List<IRenderCmd> renderSnapshot = new ArrayList<>();

    private List<IRenderCmd> activeBuffer = new ArrayList<>(50);
    private List<IRenderCmd> snapshotBuffer = new ArrayList<>(50);

    public Scene(InputPoller inputPoller)
    {
        masterRenderer = new MasterRenderer();
        stateController = new StateController();
        stateController.pushState(new MenuState(stateController));
        this.inputPoller = inputPoller;

        view = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                masterRenderer.renderFrame((Graphics2D) g, view.getWidth(), view.getHeight(), renderSnapshot);
            }
        };
    }

    public JComponent getView()
    {
        return view;
    }

    public void start()
    {
        if (running) return;
        running = true;

        Thread gameThread = new Thread(this::gameLoop, "game-loop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void gameLoop()
    {
        long lastTime = System.nanoTime();

        while (running)
        {
            long now = System.nanoTime();
            double  deltaNanos = now - lastTime;
            lastTime = now;
            double deltaMillis = deltaNanos / 1_000_000;

            InputSnapshot frameInput = inputPoller.poll();

            synchronized (stateController)
            {
                activeBuffer.clear();
                stateController.update(deltaMillis, frameInput);

                int height = view.getHeight();
                if (height == 0)
                    continue; // Пропускаем кадр, пока Swing не инициализировал холст
                double scale = (double) height / MasterRenderer.VIRTUAL_HEIGHT;
                int dynamicVirtualWidth = (int) Math.ceil(view.getWidth() / scale);

                stateController.buildFrame(activeBuffer, dynamicVirtualWidth, MasterRenderer.VIRTUAL_HEIGHT);
            }

            List<IRenderCmd> temp = snapshotBuffer;
            snapshotBuffer = activeBuffer;
            activeBuffer = temp;

            // Публикуем неизменяемую копию для EDT, чтобы избежать ConcurrentModificationException
            renderSnapshot = new ArrayList<>(snapshotBuffer);
            SwingUtilities.invokeLater(view::repaint);

            long elapsedNanos = System.nanoTime() - lastTime;
            long sleepNanos = OPTIMAL_NANOS - elapsedNanos;

            if (sleepNanos > 0)
            {
                long sleepMillis = (sleepNanos / 1_000_000) - 2;
                if (sleepMillis > 0)
                {
                    try
                    {
                        Thread.sleep(sleepMillis);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        running = false;
                    }
                }

                long targetTime = lastTime + OPTIMAL_NANOS;
                while (System.nanoTime() < targetTime)
                {
                    Thread.onSpinWait();
                }
            }
        }
    }
}