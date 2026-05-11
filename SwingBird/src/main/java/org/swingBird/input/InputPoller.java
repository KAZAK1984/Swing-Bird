package org.swingBird.input;

import java.util.concurrent.atomic.AtomicLong;

public class InputPoller
{
    private final AtomicLong pendingState = new AtomicLong(0L);

    private long previousFrameState = 0L;
    private long currentFrameState = 0L;

    public void setActionState(GameAction action, boolean isPressed)
    {
        long mask = 1L << action.ordinal();
        pendingState.updateAndGet(state -> isPressed ? (state | mask) : (state & ~mask));
    }

    public InputSnapshot poll()
    {
        previousFrameState = currentFrameState;
        currentFrameState = pendingState.get();
        return new InputSnapshot(currentFrameState, previousFrameState);
    }
}