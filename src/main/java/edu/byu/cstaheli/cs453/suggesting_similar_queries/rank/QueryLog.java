package edu.byu.cstaheli.cs453.suggesting_similar_queries.rank;

import java.time.LocalDateTime;

/**
 * Created by cstaheli on 5/19/2017.
 */
public class QueryLog implements Comparable<QueryLog>
{
    private final String anonId;
    private final String query;
    private final LocalDateTime timeStamp;

    public QueryLog(String anonId, String[] query, LocalDateTime timeStamp)
    {
        this.anonId = anonId;
        this.query = String.join(" ", query);
        ;
        this.timeStamp = timeStamp;
    }

    public String getAnonId()
    {
        return anonId;
    }

    public String getQuery()
    {
        return String.join(" ", query);
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryLog log = (QueryLog) o;

        if (!anonId.equals(log.anonId)) return false;
        if (!query.equals(log.query)) return false;
        return timeStamp.equals(log.timeStamp);
    }

    @Override
    public int hashCode()
    {
        int result = anonId.hashCode();
        result = 31 * result + query.hashCode();
        result = 31 * result + timeStamp.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return String.format("%s\t[%s]\t%s\n", anonId, query, timeStamp.toString());
    }

    @Override
    public int compareTo(QueryLog other)
    {
        int idComparison = this.anonId.compareTo(other.anonId);
        if (idComparison == 0)
        {
            int timeComparison = this.timeStamp.compareTo(other.timeStamp);
            if (timeComparison == 0)
            {
                return this.getQuery().compareTo(other.getQuery());
            }
            else
            {
                return timeComparison;
            }
        }
        else
        {
            return idComparison;
        }
    }
}
