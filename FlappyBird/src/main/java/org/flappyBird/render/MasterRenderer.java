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
                g.setColor(new Color(0xFFE178));
                g.fillRect(-14, -10, 28, 20);

                g.setColor(new Color(0xFAAA32));
                g.fillRect(6, -4, 10, 8);
                break;
            case PIPE:
                if (s.length() <= 0)
                    throw new IllegalArgumentException("Pipe length must be positive: " + s.length());

                g.setColor(new Color(0x005000));
                g.fillRect(0, 0, 56, 40);

                g.setColor(new Color(0x00A000));
                g.fillRect(3, 0, 50, s.length());
                break;
            case BACKGROUND:
                if (s.length() <= 0)
                    throw new IllegalArgumentException("Background length must be positive: " + s.length());

                g.setColor(new Color(0x4CBDFD));
                g.fillRect(0, 0, s.length(),0);
                break;
            default:
                throw new IllegalArgumentException("Unknown sprite type: " + s.spriteType());
        }

        g.setTransform(old);
    }
}