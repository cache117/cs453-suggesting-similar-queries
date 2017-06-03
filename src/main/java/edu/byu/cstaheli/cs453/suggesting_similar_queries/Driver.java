package edu.byu.cstaheli.cs453.suggesting_similar_queries;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import edu.byu.cstaheli.cs453.common.util.WordTokenizer;
import edu.byu.cstaheli.cs453.suggesting_similar_queries.process.AolQueryLogsProcessor;
import edu.byu.cstaheli.cs453.suggesting_similar_queries.rank.QueryLog;
import edu.byu.cstaheli.cs453.suggesting_similar_queries.rank.QueryTrie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cstaheli on 5/19/2017.
 */
public class Driver
{
    private QueryTrie queryLogs;

    public Driver()
    {
        queryLogs = new QueryTrie();
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
            List<String> suggestedQueries = driver.processQuery(queryInput);
            System.out.println(Arrays.toString(suggestedQueries.toArray()));
            queryInput = scanner.nextLine();
        }
    }

    private List<String> processQuery(String query)
    {
        List<String> words = new WordTokenizer(query.toLowerCase()).getWords();
        /*while (StopWordsRemover.getInstance().contains(words.get(0)))
        {
            if (words.size() > 1)
            {
                words = words.subList(1, words.size());
            }
            else
            {
                throw new RuntimeException("Your query contains only stopwords. This will not currently work for query expansion.");
            }
        }*/

        String sanitizedQuery = String.join(" ", words);
        SortedMap<String, Collection<QueryLog>> prefixMap = this.queryLogs.prefixMap(sanitizedQuery);
        Collection<QueryLog> exactMatches = prefixMap.get(sanitizedQuery);
        Map<String, Collection<QueryLog>> filteredPrefixMap = filterOutOriginalQuery(sanitizedQuery, prefixMap);
        List<String> suggestions = new ArrayList<>();
        for (QueryLog exactMatch : exactMatches)
        {
            suggestions.addAll(getCloseQueriesFromSameUser(filteredPrefixMap, exactMatch));
            System.out.println();
        }

        return getBestSuggestions(suggestions, sanitizedQuery);
    }

    private List<String> getBestSuggestions(List<String> suggestions, String sanitizedQuery)
    {
        if (suggestions.size() <= 8)
        {
            return suggestions;
        }
        else
        {
            /*List<String> bestSuggestions = new ArrayList<>();

            return bestSuggestions;*/
            return suggestions.subList(0, 8);
        }
    }

    private List<String> getCloseQueriesFromSameUser(Map<String, Collection<QueryLog>> filteredPrefixMap, QueryLog original)
    {
        return filteredPrefixMap.values()
                .stream()
                .filter(collection -> collection
                        .stream()
                        .anyMatch(queryLog -> queryLog
                                .getAnonId()
                                .equals(original.getAnonId())))
                .filter(collection -> collection
                        .stream()
                        .anyMatch(queryLog -> ChronoUnit.MINUTES.between(original.getTimeStamp(), queryLog.getTimeStamp()) < 10))
                .flatMap(Collection::stream)
                .map(QueryLog::getQueryString)
                .collect(Collectors.toList());
    }

    private Map<String, Collection<QueryLog>> filterOutOriginalQuery(String originalQuery, SortedMap<String, Collection<QueryLog>> prefixMap)
    {
        return prefixMap
                .entrySet()
                .stream()
                .filter(entry -> entry
                        .getValue()
                        .stream()
                        .noneMatch(
                                queryLog ->
                                {
                                    return queryLog.
                                            getQueryString().
                                            equals(originalQuery);
                                })
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
                        Multimap<String, QueryLog> queryLogMap = Multimaps.index(queryLogs, QueryLog::getQueryString);
                        this.queryLogs.addAll(queryLogMap.asMap());
                    });
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
