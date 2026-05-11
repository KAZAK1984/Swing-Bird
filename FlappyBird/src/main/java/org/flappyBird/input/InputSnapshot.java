package org.flappyBird.input;

import java.util.List;

public record InputSnapshot(long currentState, long previousState)
{
    private static final GameAction[] ACTIONS = GameAction.values();
    private static final long ALL_ACTIONS_MASK = buildAllActionsMask();

    private static long buildAllActionsMask()
    {
        long mask = 0L;
        for (GameAction action : ACTIONS)
            mask |= 1L << action.ordinal();

        return mask;
    }

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

        long justPressedMask = (currentState & ~previousState) & ALL_ACTIONS_MASK;
        for (GameAction action : ACTIONS)
        {
            long actionMask = 1L << action.ordinal();
            if ((justPressedMask & actionMask) != 0)
                outBuffer.add(action);
        }
    }
}