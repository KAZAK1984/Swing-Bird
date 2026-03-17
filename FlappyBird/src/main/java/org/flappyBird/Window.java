package org.flappyBird;

import org.flappyBird.input.AwtInputAdapter;
import org.flappyBird.input.InputPoller;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    private final Scene scene;

    public Window()
    {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        InputPoller inputPoller = new InputPoller();
        AwtInputAdapter adapter = new AwtInputAdapter(inputPoller);
        addKeyListener(adapter);

        scene = new Scene(inputPoller);
        JComponent view = scene.getView();
        view.setPreferredSize(new Dimension((int) screenSize.getWidth() / 2, (int) screenSize.getHeight() / 2));

        add(view, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    public void display()
    {
        setVisible(true);
        scene.start();
    }
}
