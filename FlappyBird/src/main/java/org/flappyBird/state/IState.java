package org.flappyBird.state;

import java.awt.*;

public interface IState
{
    void update(double delta, int targetFPS);
    void render(Graphics2D g);
    void onEnter();
    void onExit();
}

