package edu.byu.cstaheli.cs453.suggesting_similar_queries;

import edu.byu.cstaheli.cs453.common.util.WordTokenizer;
import edu.byu.cstaheli.cs453.common.util.StopWordsRemover;
import edu.byu.cstaheli.cs453.suggesting_similar_queries.process.AolQueryLogsProcessor;
import edu.byu.cstaheli.cs453.suggesting_similar_queries.process.QueryLog;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Created by cstaheli on 5/19/2017.
 */
public class Driver
{
    private List<QueryLog> queryLogs;

    public Driver()
    {
        Trie<String, String> trie = new PatriciaTrie<>();
        queryLogs = new ArrayList<>();
    }

    public static void main(String[] args)
    {
        String resourcesDirectory = "src/main/resources";
        Driver driver = new Driver();
        driver.readInAolQueries(resourcesDirectory);
        System.out.println("Enter a query. Type \"done\" to exit\n");
        Scanner scanner = new Scanner(System.in);
        String queryInput = scanner.nextLine();
        while (!"done".equals(queryInput) && !"".equals(queryInput))
        {
            driver.processQuery(queryInput);
            queryInput = scanner.nextLine();
        }
    }

    private void processQuery(String query)
    {
        List<String> words = new WordTokenizer(query.toLowerCase()).getWords();
        while(StopWordsRemover.getInstance().contains(words.get(0)))
        {
            if (words.size() > 1)
            {
                words = words.subList(1, words.size());
            }
            else
            {
                throw new RuntimeException("Your query contains only stopwords. This will not currently work for query expansion.");
            }
        }

    }

    private void readInAolQueries(String directory)
    {
        try (Stream<Path> paths = Files.walk(Paths.get(directory)))
        {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains("Clean-Data-01"))
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
}
