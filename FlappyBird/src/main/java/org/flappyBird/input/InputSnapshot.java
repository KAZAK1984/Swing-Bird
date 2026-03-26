package org.flappyBird.input;

import java.util.List;

public record InputSnapshot(long currentState, long previousState)
{
    public boolean isJustPressed(GameAction action)
    {
        long mask = 1L << action.ordinal();
        boolean current = (currentState & mask) != 0;
        boolean previous = (previousState & mask) != 0;

        return current && !previous;
    }

    public void getJustPressedActions(List<GameAction> outBuffer)
    {
        outBuffer.clear();

        if (isJustPressed(GameAction.FLAP))
            outBuffer.add(GameAction.FLAP);
        if (isJustPressed(GameAction.PAUSE))
            outBuffer.add(GameAction.PAUSE);
        if (isJustPressed(GameAction.UI_UP))
            outBuffer.add(GameAction.UI_UP);
        if (isJustPressed(GameAction.UI_DOWN))
            outBuffer.add(GameAction.UI_DOWN);
        if (isJustPressed(GameAction.UI_CONFIRM))
            outBuffer.add(GameAction.UI_CONFIRM);
    }
}