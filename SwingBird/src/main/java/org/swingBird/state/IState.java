package org.swingBird.state;

import org.swingBird.render.IRenderCmd;
import org.swingBird.input.InputSnapshot;

import java.util.List;

public interface IState
{
    void update(double deltaMillis, InputSnapshot input);
    void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight);
    void onEnter();
}

