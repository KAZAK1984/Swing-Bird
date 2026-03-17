package org.flappyBird.render;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class MasterRenderer
{
    public void renderFrame(Graphics2D g, List<IRenderCmd> commands)
    {
        for (IRenderCmd cmd : commands)
        {
            switch (cmd)
            {
                case CmdRect r -> drawRect(g, r);
                case CmdText t -> drawText(g, t);
                case CmdSprite s -> drawSprite(g, s);
            }
        }
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
        g.translate(s.x(), s.y());
        g.rotate(s.rotationRadians());

        switch (s.spriteType())
        {
            case BIRD:
                // Тело
                g.setColor(new Color(0xFFE178));
                g.fillRect(-14, -10, 28, 20);

                // Клюв
                g.setColor(new Color(0xFAAA32));
                g.fillRect(6, -4, 10, 8);
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