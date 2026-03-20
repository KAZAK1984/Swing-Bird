package org.flappyBird;

import org.flappyBird.input.AwtInputAdapter;
import org.flappyBird.input.InputPoller;
import org.flappyBird.render.MasterRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame
{
    private final Scene scene;

    private boolean isFullscreen;

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

        view.setPreferredSize(new Dimension(800, MasterRenderer.VIRTUAL_HEIGHT));
        setResizable(false);
        add(view, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void toggleFullscreen()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        var isVisible = isDisplayable();
        if (isVisible)
            dispose();

        if (isFullscreen)
        {
            setUndecorated(false);
            gd.setFullScreenWindow(null);
            setLocationRelativeTo(null);
            isFullscreen = false;
        }
        else
        {
            setUndecorated(true);
            setSize(800, MasterRenderer.VIRTUAL_HEIGHT);
            gd.setFullScreenWindow(this);
            isFullscreen = true;
        }

        if (isVisible)
            setVisible(true);

        revalidate();
        repaint();
    }

    public void display()
    {
        setVisible(true);
        scene.start();
    }
}

