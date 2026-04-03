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
                case CmdCircle c -> drawCircle(g, c);
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

    private void drawCircle(Graphics2D g, CmdCircle circle)
    {
        g.setColor(new Color(circle.rgb()));
        int ovalWidth = Math.max(0, circle.x2() - circle.x1());
        int ovalHeight = Math.max(0, circle.y2() - circle.y1());
        g.fillOval(circle.x1(), circle.y1(), ovalWidth, ovalHeight);
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
                drawBird(g);
                break;
            case CLOUD:
                drawCloud(g);
                break;
            case MEDAL_BRONZE_PLUS:
                drawBronzePlus(g);
                break;
            case MEDAL_SILVER_CLOUD:
                drawSilverCloud(g);
                break;
            case MEDAL_GOLD_STAR:
                drawGoldStar(g);
                break;
            case MEDAL_DIAMOND_BIRD:
                drawDiamondBird(g);
                break;
            default:
                throw new IllegalArgumentException("Unknown sprite type: " + s.spriteType());
        }

        g.setTransform(old);
    }

    private void drawBird(Graphics2D g)
    {
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
    }

    private void drawCloud(Graphics2D g)
    {
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
    }

    private void drawBronzePlus(Graphics2D g)
    {
        g.setColor(new Color(0x8A4F1F));
        g.fillRect(8, 0, 8, 24);
        g.fillRect(0, 8, 24, 8);

        g.setColor(new Color(0xCD7F32));
        g.fillRect(10, 2, 4, 20);
        g.fillRect(2, 10, 20, 4);
    }

    private void drawSilverCloud(Graphics2D g)
    {
        g.setColor(new Color(0x8EA1AF));
        g.fillOval(0, 20, 24, 16);
        g.fillOval(16, 14, 28, 20);
        g.fillOval(34, 20, 22, 16);

        g.setColor(new Color(0xB8C3CB));
        g.fillOval(4, 11, 24, 18);
        g.fillOval(18, 6, 28, 22);
        g.fillOval(34, 11, 20, 18);

        g.setColor(new Color(0xE3E9ED));
        g.fillOval(10, 8, 20, 14);
        g.fillOval(26, 4, 18, 14);
    }

    private void drawGoldStar(Graphics2D g)
    {
        g.setColor(new Color(0xB8860B));
        g.fillRect(10, 0, 4, 8);
        g.fillRect(0, 9, 8, 4);
        g.fillRect(16, 9, 8, 4);
        g.fillRect(6, 16, 4, 8);
        g.fillRect(14, 16, 4, 8);

        g.setColor(new Color(0xFFD700));
        g.fillRect(10, 2, 4, 6);
        g.fillRect(8, 8, 8, 8);
        g.fillRect(2, 10, 20, 2);
        g.fillRect(8, 16, 8, 6);
    }

    private void drawDiamondBird(Graphics2D g)
    {
        g.setColor(new Color(0x9EE8F7));
        g.fillRect(3, 6, 20, 14);
        g.fillRect(6, 3, 12, 4);
        g.fillRect(2, 10, 4, 6);

        g.setColor(new Color(0xCFF6FF));
        g.fillRect(8, 4, 8, 3);

        g.setColor(new Color(0x84D7EA));
        g.fillRect(6, 18, 15, 3);

        g.setColor(new Color(0x8FDDED));
        g.fillRect(9, 10, 9, 6);
        g.setColor(new Color(0x6FC8DB));
        g.fillRect(11, 12, 5, 2);

        g.setColor(new Color(0xFFFFFF));
        g.fillRect(15, 8, 4, 4);
        g.setColor(new Color(0x3A86A8));
        g.fillRect(17, 9, 2, 2);

        g.setColor(new Color(0x6FB5C8));
        g.fillRect(22, 11, 10, 7);
        g.setColor(new Color(0x87CCDD));
        g.fillRect(22, 11, 8, 3);
    }
}