package org.flappyBird.state;

import org.flappyBird.component.UIButton;
import org.flappyBird.component.UIManager;
import org.flappyBird.input.GameAction;
import org.flappyBird.input.InputSnapshot;
import org.flappyBird.logic.RunEntry;
import org.flappyBird.stats.StatsData;
import org.flappyBird.stats.StatsRepository;
import org.flappyBird.render.CmdRect;
import org.flappyBird.render.CmdText;
import org.flappyBird.render.IRenderCmd;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsState implements IState
{
    private static final int MAX_LINES = 10;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final StateController controller;
    private final UIManager uiManager = new UIManager();
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
        uiManager.addButton(new UIButton(0, 0, 0, 0, "BACK", this::close));
        refreshStats();
    }

    @Override
    public void update(double deltaMillis, InputSnapshot input)
    {
        if (input.isJustPressed(GameAction.PAUSE))
        {
            close();
            return;
        }

        uiManager.update(input);
    }

    @Override
    public void buildFrame(List<IRenderCmd> buffer, int canvasWidth, int canvasHeight)
    {
        buffer.add(new CmdRect(0, 0, canvasWidth, canvasHeight, 0x101010));

        int panelWidth = (int) (canvasWidth * 0.7f);
        int panelHeight = (int) (canvasHeight * 0.7f);
        int panelX = (canvasWidth - panelWidth) / 2;
        int panelY = (canvasHeight - panelHeight) / 2;

        buffer.add(new CmdRect(panelX, panelY, panelWidth, panelHeight, 0x1E1E1E));

        int textX = panelX + 20;
        int textY = panelY + 28;
        buffer.add(new CmdText("STATISTICS", textX, textY, 0xFFFFFF));
        buffer.add(new CmdText("Max score: " + maxScore, textX, textY + 22, 0xFFFFFF));
        buffer.add(new CmdText("Recent runs:", textX, textY + 44, 0xFFFFFF));

        int lineY = textY + 66;
        for (int i = 0; i < cachedLines.size(); i++)
            buffer.add(new CmdText(cachedLines.get(i), textX, lineY + i * 18, 0xFFFFFF));

        uiManager.changeButtonsBounds(panelX, panelY + panelHeight - 50, panelWidth, 40);
        uiManager.render(buffer);
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
            cachedLines.add(formatRunLine(i + 1, run));
        }

        if (cachedLines.isEmpty())
            cachedLines.add("No runs yet");
    }

    private String formatRunLine(int index, RunEntry run)
    {
        String time = formatTimestamp(run.timestamp());
        return index + ") Score: " + run.score() + "  " + time;
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
