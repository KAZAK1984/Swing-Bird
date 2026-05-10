package org.flappyBird.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class StatsStore implements StatsRepository
{
    private static final int MAX_RUNS = 100;
    private static final String FILE_NAME = "stats.json";
    private static final String BACKUP_SUFFIX = ".bak";
    private static final String TEMP_SUFFIX = ".tmp";

    private final Path statsPath;
    private final Path backupPath;
    private final Path tempPath;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public StatsStore()
    {
        this(getDefaultPath());
    }

    public StatsStore(Path statsPath)
    {
        this.statsPath = statsPath;
        String fileName = statsPath.getFileName().toString();
        this.backupPath = statsPath.resolveSibling(fileName + BACKUP_SUFFIX);
        this.tempPath = statsPath.resolveSibling(fileName + TEMP_SUFFIX);
    }

    @Override
    public StatsData load()
    {
        if (!Files.exists(statsPath))
            return new StatsData();

        try
        {
            StatsData data = mapper.readValue(statsPath.toFile(), StatsData.class);
            normalize(data);
            return data;
        }
        catch (IOException ex)
        {
            System.err.println("Failed to read stats.json: " + ex.getMessage());
        }

        if (Files.exists(backupPath))
        {
            try
            {
                StatsData data = mapper.readValue(backupPath.toFile(), StatsData.class);
                normalize(data);
                return data;
            }
            catch (IOException ex)
            {
                System.err.println("Failed to read stats backup: " + ex.getMessage());
            }
        }

        return new StatsData();
    }

    @Override
    public void recordRun(int score)
    {
        StatsData data = load();
        data.getRuns().add(0, new RunEntry(score, System.currentTimeMillis()));
        data.setMaxScore(Math.max(data.getMaxScore(), score));

        if (data.getRuns().size() > MAX_RUNS)
            data.getRuns().subList(MAX_RUNS, data.getRuns().size()).clear();

        save(data);
    }

    private void save(StatsData data)
    {
        try
        {
            Files.createDirectories(statsPath.getParent());
            mapper.writeValue(tempPath.toFile(), data);

            if (Files.exists(statsPath))
                Files.copy(statsPath, backupPath, StandardCopyOption.REPLACE_EXISTING);

            moveTempToTarget();
        }
        catch (IOException ex)
        {
            System.err.println("Failed to save stats.json: " + ex.getMessage());
        }
    }

    private void moveTempToTarget() throws IOException
    {
        try
        {
            Files.move(tempPath, statsPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }
        catch (AtomicMoveNotSupportedException ex)
        {
            Files.move(tempPath, statsPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Path getDefaultPath()
    {
        String home = System.getProperty("user.home", ".");
        return Paths.get(home, ".flappybird", FILE_NAME);
    }

    private static void normalize(StatsData data)
    {
        if (data == null)
            return;

        if (data.getRuns() == null)
            data.setRuns(new ArrayList<>());

        int observedMax = data.getMaxScore();
        List<RunEntry> runs = data.getRuns();
        runs.removeIf(run -> run == null);

        for (RunEntry run : runs)
            observedMax = Math.max(observedMax, run.score());

        data.setMaxScore(observedMax);

        if (runs.size() > MAX_RUNS)
            runs.subList(MAX_RUNS, runs.size()).clear();
    }
}
