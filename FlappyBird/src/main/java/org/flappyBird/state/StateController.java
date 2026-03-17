package org.flappyBird.state;

import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.IRenderCmd;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class StateController
{
    private final Deque<IState> states = new ArrayDeque<>();

    public void setState(IState newState)
    {
        while (!states.isEmpty())
        {
            states.pop().onExit();
        }
        pushState(newState);
    }

    public void pushState(IState newState)
    {
        states.push(newState);
        newState.onEnter();
    }

    public void popState()
    {
        if (!states.isEmpty())
        {
            states.pop().onExit();
        }
    }

    public void update(double deltaMillis, InputSnapshot input)
    {
        IState current = states.peek();
        if (current != null)
        {
            current.update(deltaMillis, input);
        }
    }

    public void buildFrame(List<IRenderCmd> buffer, int width, int height)
    {
        Iterator<IState> iterator = states.descendingIterator();
        while (iterator.hasNext())
        {
            iterator.next().buildFrame(buffer, width, height);
        }
    }
}