package org.swingBird.stats;

public interface StatsRepository
{
    StatsData load();
    void recordRun(int score);
}
