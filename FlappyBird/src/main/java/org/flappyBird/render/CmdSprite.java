package org.flappyBird.render;

public record CmdSprite(int spriteId, int x, int y, float rotationRadians) implements IRenderCmd {}
