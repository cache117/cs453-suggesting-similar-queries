package edu.byu.cstaheli.cs453.suggesting_similar_queries.rank;

import edu.byu.cstaheli.cs453.suggesting_similar_queries.util.WordCorrelationEvaluator;

/**
 * Created by cstaheli on 6/2/2017.
 */
public class SuggestedQueryRanker
{
    private double rank;
    private String originalQuery;
    private String suggestedQuery;
    private QueryTrie trie;

    public SuggestedQueryRanker(String query, String suggestedQuery, QueryTrie trie)
    {
        this.originalQuery = query;
        this.suggestedQuery = suggestedQuery;
        this.trie = trie;
        rank = rankQuery();
    }

    private double rankQuery()
    {
        double freqSq = getOccurrenceOfSuggestedQuery();
        double wcf = getWordCorrelationFactorOfQueries();
        wcf = wcf == -1 ? 0 : wcf;
        double mod = getNumberOfDirectModifications();

        return -1;
    }

    private double getOccurrenceOfSuggestedQuery()
    {
        return trie.frequency(suggestedQuery) / trie.getMostCommonFrequency();
    }

    private double getWordCorrelationFactorOfQueries()
    {
        return WordCorrelationEvaluator.getWordCorrelationFactor(originalQuery, suggestedQuery);
    }

    private double getNumberOfDirectModifications()
    {
        return 1;
    }
    public double getRank()
    {
        return rank;
    }
}
