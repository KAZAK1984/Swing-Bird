package org.swingBird.render;

public sealed interface IRenderCmd permits CmdRect, CmdText, CmdSprite, CmdCircle {}

