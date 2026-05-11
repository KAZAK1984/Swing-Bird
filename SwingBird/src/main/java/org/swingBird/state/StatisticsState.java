package org.swingBird.state;

import org.swingBird.input.GameAction;
import org.swingBird.input.InputSnapshot;
import org.swingBird.logic.RunEntry;
import org.swingBird.stats.StatsData;
import org.swingBird.stats.StatsRepository;
import org.swingBird.render.CmdRect;
import org.swingBird.render.CmdText;
import org.swingBird.render.IRenderCmd;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsState implements IState
{
    private static final int MAX_LINES = 10;
    private static final int CHAR_WIDTH = 6;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final StateController controller;
    private final StatsRepository statsRepository;

    private List<String> cachedLines = new ArrayList<>();
    private int maxScore = 0;

    public StatisticsState(StateController controller)
    {
        this.controller = controller;
        this.statsRepository = controller.getStatsRepository();
    }

    @Override public void onEnter()
    {
        refreshStats();
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        if (input.isJustPressed(GameAction.PAUSE) || input.isJustPressed(GameAction.UI_CONFIRM))
        {
            System.out.println("Exiting StatisticsState...");
            close();
        }
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        int panelWidth = canvasWidth / 2;
        int panelHeight = canvasHeight / 2;
        int panelX = (canvasWidth - panelWidth) / 2;
        int panelY = (canvasHeight - panelHeight) / 2;

        buffer.add(new CmdRect(panelX, panelY, panelWidth, panelHeight, 0x101010));
        buffer.add(new CmdRect(panelX + 4, panelY + 4, panelWidth - 8, 36, 0x1E1E1E));

        String title = "STATISTICS";
        int titleY = panelY + 27;
        buffer.add(new CmdText(title, getCenteredTextX(title, panelX, panelWidth), titleY, 0xFFFFFF));

        int textY = panelY + 60;
        String maxScoreLine = "Max score: " + maxScore;
        buffer.add(new CmdText(maxScoreLine, getCenteredTextX(maxScoreLine, panelX, panelWidth), textY, 0xFFFFFF));

        String recentRunsLine = "Recent runs:";
        buffer.add(new CmdText(recentRunsLine, getCenteredTextX(recentRunsLine, panelX, panelWidth), textY + 22, 0xFFFFFF));

        int lineY = textY + 44;
        for (int i = 0; i < cachedLines.size(); i++)
        {
            String line = cachedLines.get(i);
            buffer.add(new CmdText(line, getCenteredTextX(line, panelX, panelWidth), lineY + i * 18, 0xFFFFFF));
        }
    }

    private void refreshStats()
    {
        if (statsRepository == null)
        {
            cachedLines = new ArrayList<>();
            cachedLines.add("No stats repository");
            maxScore = 0;
            return;
        }

        StatsData data = statsRepository.load();
        maxScore = data.getMaxScore();
        cachedLines = new ArrayList<>();

        int count = Math.min(MAX_LINES, data.getRuns().size());
        for (int i = 0; i < count; i++)
        {
            RunEntry run = data.getRuns().get(i);
            cachedLines.add(formatRunLine(run));
        }

        if (cachedLines.isEmpty())
            cachedLines.add("No runs yet");
    }

    private String formatRunLine(RunEntry run)
    {
        String time = formatTimestamp(run.timestamp());
        return time + "   |   " + "Score: " + run.score();
    }

    private int getCenteredTextX(String text, int panelX, int panelWidth)
    {
        int textWidth = text.length() * CHAR_WIDTH;
        return panelX + (panelWidth - textWidth) / 2;
    }

    private String formatTimestamp(long timestamp)
    {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return DATE_FORMAT.format(date);
    }

    private void close()
    {
        controller.popState();
    }
}
