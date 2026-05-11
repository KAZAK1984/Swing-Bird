package org.flappyBird.state;

import org.flappyBird.render.IRenderCmd;
import org.flappyBird.input.InputSnapshot;

import java.util.List;

public interface IState
{
    void update(double deltaMillis, InputSnapshot input);
    void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight);
    void onEnter();
}

