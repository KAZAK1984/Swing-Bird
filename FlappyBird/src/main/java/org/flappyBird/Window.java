package org.flappyBird;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    public Window()
    {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setMinimumSize(new Dimension((int)screenSize.getWidth() / 2,(int)screenSize.getHeight() / 2));
        setMaximumSize(screenSize);
        setSize(screenSize);
        setLocationRelativeTo(null);
    }

    public void display()
    {
        setVisible(true);
    }
}
