package org.flappyBird;

import org.flappyBird.input.AwtInputAdapter;
import org.flappyBird.input.InputPoller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame
{
    private final Scene scene;

    private boolean isFullscreen;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;

    public Window()
    {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        isFullscreen = false;

        InputPoller inputPoller = new InputPoller();
        AwtInputAdapter adapter = new AwtInputAdapter(inputPoller);
        addKeyListener(adapter);

        // Переключение экрана по F11
        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_F11)
                    toggleFullscreen();
            }
        });

        scene = new Scene(inputPoller);
        JComponent view = scene.getView();

        setResizable(false);
        view.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        add(view, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void toggleFullscreen()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (isFullscreen)
        {
            gd.setFullScreenWindow(null);
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            setLocationRelativeTo(null);
            isFullscreen = false;
        }
        else
        {
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            gd.setFullScreenWindow(this);
            isFullscreen = true;
        }

        revalidate();
        repaint();
    }

    public void display()
    {
        setVisible(true);
        scene.start();
    }
}

