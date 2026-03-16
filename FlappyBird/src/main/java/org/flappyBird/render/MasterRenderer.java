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

        if (s.spriteId() == 1)
        {
            g.setColor(new Color(0xFFE178));
            g.fillRect(-14, -10, 28, 20);

            g.setColor(new Color(0xFAAA32));
            g.fillRect(6, -4, 10, 8);
        }

        g.setTransform(old);
    }
}