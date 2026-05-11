package org.flappyBird.component;

import org.flappyBird.render.CmdCircle;
import org.flappyBird.render.CmdRect;
import org.flappyBird.render.CmdSprite;
import org.flappyBird.render.CmdText;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.render.SpriteType;

import java.util.List;

public class MedalBadge
{
    private static final int PANEL_COLOR = 0x36454F;
    private static final int MEDAL_BASE_OUTER_COLOR = 0x2C3238;
    private static final int TOP_PADDING = 8;
    private static final int SIDE_PADDING = 10;
    private static final int TEXT_TO_MEDAL_GAP = 10;
    private static final int BOTTOM_PADDING = 8;
    private static final int MEDAL_RING_PADDING = 3;

    private final ScoreTier scoreTier;

    public MedalBadge(int score)
    {
        this.scoreTier = ScoreTier.fromScore(score);
    }

    public void render(List<IRenderCmd> buffer, int x, int y, int width, int height)
    {
        buffer.add(new CmdRect(x, y, width, height, PANEL_COLOR));

        int textX = x + SIDE_PADDING;
        int textY = y + TOP_PADDING + 12;
        buffer.add(new CmdText(scoreTier.medalText(), textX, textY, scoreTier.color()));

        int medalAreaTop = textY + TEXT_TO_MEDAL_GAP;
        int availableWidth = Math.max(0, width - SIDE_PADDING * 2);
        int availableHeight = Math.max(0, (y + height - BOTTOM_PADDING) - medalAreaTop);
        int medalDiameter = Math.min(availableWidth, availableHeight);

        int medalX = x + SIDE_PADDING + Math.max(0, (availableWidth - medalDiameter) / 2);
        int medalY = medalAreaTop + Math.max(0, (availableHeight - medalDiameter) / 2);

        buffer.add(new CmdCircle(medalX, medalY, medalX + medalDiameter, medalY + medalDiameter, MEDAL_BASE_OUTER_COLOR));

        int innerX = medalX + MEDAL_RING_PADDING;
        int innerY = medalY + MEDAL_RING_PADDING;
        int innerDiameter = Math.max(0, medalDiameter - MEDAL_RING_PADDING * 2);
        buffer.add(new CmdCircle(innerX, innerY, innerX + innerDiameter, innerY + innerDiameter, scoreTier.color()));

        if (scoreTier == ScoreTier.NONE)
            return;

        SpriteType spriteType = scoreTier.medalSpriteType();

        int spriteWidth = getSpriteWidth(spriteType);
        int spriteHeight = getSpriteHeight(spriteType);

        int spriteX = medalX + Math.max(0, (medalDiameter - spriteWidth) / 2);
        int spriteY = medalY + Math.max(0, (medalDiameter - spriteHeight) / 2);
        buffer.add(new CmdSprite(spriteType, spriteX, spriteY, 0f));
    }

    private int getSpriteWidth(SpriteType spriteType)
    {
        return switch (spriteType)
        {
            case MEDAL_SILVER_CLOUD -> 56;
            case MEDAL_DIAMOND_BIRD -> 32;
            case MEDAL_BRONZE_PLUS, MEDAL_GOLD_STAR -> 24;
            default -> 0;
        };
    }

    private int getSpriteHeight(SpriteType spriteType)
    {
        return switch (spriteType)
        {
            case MEDAL_SILVER_CLOUD -> 36;
            case MEDAL_DIAMOND_BIRD, MEDAL_BRONZE_PLUS, MEDAL_GOLD_STAR -> 24;
            default -> 0;
        };
    }
}



