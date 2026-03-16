package org.flappyBird.render;

public sealed interface IRenderCmd permits CmdRect, CmdText, CmdSprite {}

