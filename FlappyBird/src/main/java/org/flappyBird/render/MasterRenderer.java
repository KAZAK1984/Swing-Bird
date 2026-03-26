package org.flappyBird.render;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class MasterRenderer
{
    public static final int VIRTUAL_HEIGHT = 600;

    public void renderFrame(Graphics2D g, int screenWidth, int screenHeight, List<IRenderCmd> commands)
    {
        double scale = (double) screenHeight / VIRTUAL_HEIGHT;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        AffineTransform originalTransform = g.getTransform();

        g.scale(scale, scale);

        for (IRenderCmd cmd : commands)
        {
            switch (cmd)
            {
                case CmdRect r -> drawRect(g, r);
                case CmdText t -> drawText(g, t);
                case CmdSprite s -> drawSprite(g, s);
            }
        }

        g.setTransform(originalTransform);
    }

    private void drawRect(Graphics2D g, CmdRect r)
    {
        g.setColor(new Color(r.rgb()));
        g.fillRect(r.x(), r.y(), r.w(), r.h());
    }

    private void drawText(Graphics2D g, CmdText t)
    {
        g.setColor(new Color(t.rgb()));
        g.drawString(t.text(), t.x(), t.y());
    }

    private void drawSprite(Graphics2D g, CmdSprite s)
    {
        AffineTransform old = g.getTransform();

        if (s.spriteType() == SpriteType.BIRD)
        {
            // Птица вращается вокруг своего центра
            g.translate(s.x() + 17, s.y() + 12);
            g.rotate(s.rotationRadians());
            g.translate(-17, -12);
        }
        else
        {
            g.translate(s.x(), s.y());
            g.rotate(s.rotationRadians());
        }

        switch (s.spriteType())
        {
            case BIRD:
                // Тело птицы
                g.setColor(new Color(0xF7DF59));
                g.fillRect(3, 6, 20, 14);
                g.fillRect(6, 3, 12, 4);
                g.fillRect(2, 10, 4, 6);

                g.setColor(new Color(0xFFE97A));
                g.fillRect(8, 4, 8, 3);

                g.setColor(new Color(0xE7C948));
                g.fillRect(6, 18, 15, 3);

                g.setColor(new Color(0xF2C94C));
                g.fillRect(9, 10, 9, 6);
                g.setColor(new Color(0xE6B93E));
                g.fillRect(11, 12, 5, 2);

                // Глаз
                g.setColor(new Color(0xFFFFFF));
                g.fillRect(15, 8, 4, 4);
                g.setColor(new Color(0x5B3FA6));
                g.fillRect(17, 9, 2, 2);

                // Клюв
                g.setColor(new Color(0xD93B2B));
                g.fillRect(22, 11, 10, 7);
                g.setColor(new Color(0xEF4A35));
                g.fillRect(22, 11, 8, 3);
                break;
            case CLOUD:
                // Нижний слой
                g.setColor(new Color(0xCC7FA9BB, true));
                g.fillOval(0, 35, 40, 20);
                g.fillOval(30, 30, 50, 25);
                g.fillOval(55, 30, 35, 25);

                // Средний слой
                g.setColor(new Color(0xE6C8E0EB, true));
                g.fillOval(5, 15, 45, 32);
                g.fillOval(25, 12, 50, 35);
                g.fillOval(55, 20, 35, 30);

                // Верхний слой
                g.setColor(new Color(0xFFFFFFFF, true));
                g.fillOval(15, 8, 40, 28);
                g.fillOval(35, 0, 40, 28);
                g.fillOval(55, 10, 30, 20);
                break;
            default:
                throw new IllegalArgumentException("Unknown sprite type: " + s.spriteType());
        }

        g.setTransform(old);
    }
}