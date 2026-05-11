package org.flappyBird.state;

import org.flappyBird.input.InputSnapshot;
import org.flappyBird.render.IRenderCmd;
import org.flappyBird.stats.StatsRepository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class StateController
{
    private final Deque<IState> states = new ArrayDeque<>();
    private final StatsRepository statsRepository;

    public StateController(StatsRepository statsRepository)
    {
        this.statsRepository = statsRepository;
    }

    public StatsRepository getStatsRepository()
    {
        return statsRepository;
    }

    public void setState(IState newState)
    {
        if (states.peek() instanceof PlayingState playingState)
            recordPlayingStateRun(playingState);

        while (!states.isEmpty())
            states.pop();

        pushState(newState);
    }

    public void pushState(IState newState)
    {
        IState current = states.peek();
        if (current instanceof PlayingState playingState && isRunExitState(newState))
            recordPlayingStateRun(playingState);

        states.push(newState);
        newState.onEnter();
    }

    public void popState()
    {
        if (states.peek() instanceof PlayingState playingState)
            recordPlayingStateRun(playingState);

        if (!states.isEmpty())
            states.pop();
    }

    public void update(double deltaMillis, InputSnapshot input)
    {
        IState current = states.peek();
        if (current != null)
            current.update(deltaMillis, input);
    }

    public void buildFrame(List<IRenderCmd> buffer, int width, int height)
    {
        Iterator<IState> iterator = states.descendingIterator();
        while (iterator.hasNext())
            iterator.next().buildFrame(buffer, width, height);
    }

    private void recordPlayingStateRun(PlayingState playingState)
    {
        if (statsRepository != null)
            statsRepository.recordRun(playingState.getScore());
    }

    private boolean isRunExitState(IState newState)
    {
        return newState instanceof PauseState || newState instanceof ResetState;
    }
}