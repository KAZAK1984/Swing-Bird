package org.flappyBird.render;

public record CmdSprite(SpriteType spriteType, int x, int y, float rotationRadians) implements IRenderCmd {}
