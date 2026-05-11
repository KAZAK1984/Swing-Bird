package org.swingBird.stats;

import org.swingBird.logic.RunEntry;

import java.util.ArrayList;
import java.util.List;

public class StatsData
{
    private int maxScore = 0;
    private List<RunEntry> runs = new ArrayList<>();

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

