package org.flappyBird.render;

public record CmdCircle(int x1, int y1, int x2, int y2, int rgb) implements IRenderCmd {}
