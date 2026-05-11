package org.flappyBird;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            // Пытаемся установить системный внешний вид
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e)
            {
                System.err.println("Не удалось установить системный Look and Feel: " + e.getMessage());
            }

            Window window = new Window();
            window.display();
        });
    }
}
