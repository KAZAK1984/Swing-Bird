package org.swingBird.render;

public record CmdText(String text, int x, int y, int rgb) implements IRenderCmd {}
