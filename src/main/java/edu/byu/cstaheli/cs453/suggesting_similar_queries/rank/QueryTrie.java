package edu.byu.cstaheli.cs453.suggesting_similar_queries.rank;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cstaheli on 6/2/2017.
 */
public class QueryTrie
{
    private Trie<String, Collection<QueryLog>> trie;

    public QueryTrie(Map<String, Collection<QueryLog>> inputs)
    {
        this.trie = new PatriciaTrie<>(inputs);
        System.out.println();
    }

    public QueryTrie()
    {
        this.trie = new PatriciaTrie<>();
    }

    private static <T> BinaryOperator<T> throwingMerger()
    {
        return (u, v) ->
        {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
    }

    /**
     * Removes any duplicates from the list
     *
     * @param list the list to remove duplicates from
     * @return a copy of the list without duplicates
     */
    private static List<String> removeDuplicatesFromList(List<String> list)
    {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    public void addAll(Map<String, Collection<QueryLog>> inputs)
    {
        this.trie.putAll(inputs);
    }

    public SortedMap<String, Collection<QueryLog>> prefixMap(String query)
    {
        return trie.prefixMap(query);
    }

    public SortedMap<String, Collection<QueryLog>> prefixMapWithoutOriginalQuery(String query)
    {
        return prefixMap(query)
                .entrySet()
                .stream()
                .filter(entry -> entry
                        .getValue()
                        .stream()
                        .noneMatch(
                                queryLog ->
                                {
                                    return queryLog.
                                            getQuery().
                                            equals(query);
                                })
                ).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        throwingMerger(),
                        TreeMap::new));
    }

    public Collection<QueryLog> exactMatch(String query)
    {
        Collection<QueryLog> result = trie.get(query);
        return (result == null) ? new ArrayList<>(0) : result;
    }

    public int frequency(String query)
    {
        Collection<QueryLog> queryLogs = this.exactMatch(query);
        if (queryLogs != null)
        {
            return queryLogs.size();
        }
        else
        {
            return -1;
        }
    }

    public int getFrequencyOfAdjacency(String query, String suggestedQuery)
    {
        return (int) getAllSuggestionsFromQuery(query)
                .stream()
                .filter(queryLog -> queryLog
                        .getQuery()
                        .equals(suggestedQuery)
                )
                .count();
    }

    private Collection<QueryLog> getAllSuggestionsFromQuery(String query)
    {
        Collection<QueryLog> queryLogs = new ArrayList<>();
        Collection<QueryLog> exactMatches = exactMatch(query);
        for (QueryLog exactMatch : exactMatches)
        {
            queryLogs.addAll(modifiedQueriesFromOriginal(exactMatch));
        }
        return queryLogs;
    }

    public List<String> getUniqueQuerySuggestionsFromQuery(String query)
    {
        return removeDuplicatesFromList(getAllSuggestionsFromQuery(query)
                .stream()
                .map(QueryLog::getQuery)
                .collect(Collectors.toList())
        );
    }

    private Collection<QueryLog> modifiedQueriesFromOriginal(QueryLog original)
    {
        return prefixMapWithoutOriginalQuery(original.getQuery()).values()
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
                .filter(queryLog -> queryLog.getQuery().contains(original.getQuery() + " "))
                .collect(Collectors.toList());
    }

    public int getMostCommonFrequency()
    {
        return trie
                .values()
                .stream()
                .mapToInt(Collection::size)
                .max()
                .orElse(-1);
    }
}
