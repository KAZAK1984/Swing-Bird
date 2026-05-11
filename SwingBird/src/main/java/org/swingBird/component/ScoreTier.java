package org.swingBird.component;

import org.swingBird.render.SpriteType;

public enum ScoreTier
{
    NONE(0, 0x808080, "NONE", null),
    BRONZE(10, 0xCD7F32, "BRONZE", SpriteType.MEDAL_BRONZE_PLUS),
    SILVER(20, 0xC0C0C0, "SILVER", SpriteType.MEDAL_SILVER_CLOUD),
    GOLD(30, 0xFFD700, "GOLD", SpriteType.MEDAL_GOLD_STAR),
    DIAMOND(40, 0xB9F2FF, "DIAMOND", SpriteType.MEDAL_DIAMOND_BIRD);

    private final int minScore;
    private final int color;
    private final String medalText;
    private final SpriteType medalSpriteType;

    ScoreTier(int minScore, int color, String label, SpriteType medalSpriteType)
    {
        this.minScore = minScore;
        this.color = color;
        this.medalText = "MEDAL: " + label;
        this.medalSpriteType = medalSpriteType;
    }

    public int color()
    {
        return color;
    }

    public String medalText()
    {
        return medalText;
    }

    public SpriteType medalSpriteType()
    {
        return medalSpriteType;
    }

    public static ScoreTier fromScore(int score)
    {
        if (score >= DIAMOND.minScore)
            return DIAMOND;
        if (score >= GOLD.minScore)
            return GOLD;
        if (score >= SILVER.minScore)
            return SILVER;
        if (score >= BRONZE.minScore)
            return BRONZE;
        return NONE;
    }
}


