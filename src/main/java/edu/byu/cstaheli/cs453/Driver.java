package edu.byu.cstaheli.cs453;

import edu.byu.cstaheli.cs453.process.AolQueryLogsProcessor;
import edu.byu.cstaheli.cs453.process.QueryLog;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by cstaheli on 5/19/2017.
 */
public class Driver
{
    private List<QueryLog> queryLogs;
    public static void main(String[] args)
    {
        String resourcesDirectory = "src/main/resources";
        Driver driver = new Driver();
        driver.readInAolQueries(resourcesDirectory);
    }

    private void readInAolQueries(String directory)
    {
        try (Stream<Path> paths = Files.walk(Paths.get(directory)))
        {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains("Clean-Data-"))
                    .forEach(path ->
                    {
                        String fileName = path.toString();
                        List<QueryLog> queryLogs = new AolQueryLogsProcessor(fileName).getQueryLogs();
                        this.queryLogs.addAll(queryLogs);
                    });
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println();
    }

    public Driver()
    {
        Trie<String, String> trie = new PatriciaTrie<>();
        queryLogs = new ArrayList<>();
    }
}
