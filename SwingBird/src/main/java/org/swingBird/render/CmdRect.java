package org.swingBird.render;

public record CmdRect(int x, int y, int w, int h, int rgb) implements IRenderCmd {}
