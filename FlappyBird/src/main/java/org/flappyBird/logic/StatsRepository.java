package org.flappyBird.logic;

public interface StatsRepository
{
    StatsData load();

    void recordRun(int score);
}

