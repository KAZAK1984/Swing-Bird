package org.flappyBird.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.flappyBird.logic.RunEntry;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StatsStore implements StatsRepository
{
    private static final int MAX_RUNS = 10;
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

        System.out.println("Stats will be stored at: " + statsPath.toAbsolutePath());
    }

    @Override
    public StatsData load()
    {
        StatsData primary = tryRead(statsPath, "Failed to read stats.json: ");
        if (primary != null)
        {
            normalize(primary);
            return primary;
        }

        StatsData backup = tryRead(backupPath, "Failed to read stats backup: ");
        if (backup != null)
        {
            normalize(backup);
            restorePrimaryFromBackup();
            return backup;
        }

        return new StatsData();
    }

    @Override
    public void recordRun(int score)
    {
        StatsData data = load();
        data.getRuns().addFirst(new RunEntry(score, System.currentTimeMillis()));
        data.setMaxScore(Math.max(data.getMaxScore(), score));

        if (data.getRuns().size() > MAX_RUNS)
            data.getRuns().subList(MAX_RUNS, data.getRuns().size()).clear();

        save(data);
    }

    private void save(StatsData data)
    {
        try
        {
            Path parent = statsPath.getParent();
            if (parent != null)
                Files.createDirectories(parent);

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
        runs.removeIf(Objects::isNull);

        for (RunEntry run : runs)
            observedMax = Math.max(observedMax, run.score());

        data.setMaxScore(observedMax);

        if (runs.size() > MAX_RUNS)
            runs.subList(MAX_RUNS, runs.size()).clear();
    }

    private StatsData tryRead(Path path, String errorPrefix)
    {
        if (!Files.exists(path))
            return null;

        try
        {
            return mapper.readValue(path.toFile(), StatsData.class);
        }
        catch (IOException ex)
        {
            System.err.println(errorPrefix + ex.getMessage());
            return null;
        }
    }

    private void restorePrimaryFromBackup()
    {
        try
        {
            Path parent = statsPath.getParent();
            if (parent != null)
                Files.createDirectories(parent);

            Files.copy(backupPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
            moveTempToTarget();
        }
        catch (IOException ex)
        {
            System.err.println("Failed to restore stats.json from backup: " + ex.getMessage());
        }
    }
}
