package org.flappyBird.state;

import java.awt.*;

public class StateController
{
    private IState currentState;

    public void setState(IState newState)
    {
        if (currentState != null) currentState.onExit();
        currentState = newState;
        currentState.onEnter();
    }

    public void update(double delta)
    {
        if (currentState != null) currentState.update(delta);
    }

    public void render(Graphics2D g)
    {
        if (currentState != null) currentState.render(g);
    }
}

