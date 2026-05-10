package org.flappyBird.logic;

import java.util.ArrayList;
import java.util.List;

public class StatsData
{
    private int version = 1;
    private int maxScore = 0;
    private List<RunEntry> runs = new ArrayList<>();

    public StatsData()
    {
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(int maxScore)
    {
        this.maxScore = maxScore;
    }

    public List<RunEntry> getRuns()
    {
        return runs;
    }

    public void setRuns(List<RunEntry> runs)
    {
        this.runs = runs;
    }
}

