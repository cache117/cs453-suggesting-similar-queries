package edu.byu.cstaheli.cs453.suggesting_similar_queries.rank;

/**
 * Created by cstaheli on 6/2/2017.
 */
public class SuggestedQueryRanker
{
    private double rank;

    public SuggestedQueryRanker(String query, String suggestedQuery)
    {
        rank = rankQuery(query, suggestedQuery);
    }

    private double rankQuery(String query, String suggestedQuery)
    {
        return -1;
    }

    public double getRank()
    {
        return rank;
    }
}
