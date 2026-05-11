package org.flappyBird.stats;

public interface StatsRepository
{
    StatsData load();
    void recordRun(int score);
}
