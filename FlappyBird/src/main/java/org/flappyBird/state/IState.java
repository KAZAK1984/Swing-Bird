package org.flappyBird.state;

import java.awt.*;

public interface IState
{
    void update(double delta);
    void render(Graphics2D g);
    void onEnter();
    void onExit();
}

