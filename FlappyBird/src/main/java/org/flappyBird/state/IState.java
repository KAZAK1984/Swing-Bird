package org.flappyBird.state;

import org.flappyBird.render.IRenderCmd;

import java.util.List;

public interface IState
{
    void update(double deltaMillis);
    void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight);
    void onEnter();
    void onExit();
}

