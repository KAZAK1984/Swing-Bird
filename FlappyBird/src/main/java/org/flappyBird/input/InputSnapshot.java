package org.flappyBird.input;

public record InputSnapshot(long currentState, long previousState)
{
    public boolean isPressed(GameAction action)
    {
        long mask = 1L << action.ordinal();

        return (currentState & mask) != 0;
    }

    public boolean isJustPressed(GameAction action)
    {
        long mask = 1L << action.ordinal();
        boolean current = (currentState & mask) != 0;
        boolean previous = (previousState & mask) != 0;

        return current && !previous;
    }
}