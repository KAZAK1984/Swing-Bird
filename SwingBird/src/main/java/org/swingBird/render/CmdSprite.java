package org.swingBird.render;

public record CmdSprite(SpriteType spriteType, int x, int y, float rotationRadians) implements IRenderCmd {}
