package edu.byu.cstaheli.cs453.suggesting_similar_queries.rank;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

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

    public void addAll(Map<String, Collection<QueryLog>> inputs)
    {
        this.trie.putAll(inputs);
    }

    public Collection<QueryLog> get(String key)
    {
        return trie.get(key);
    }

    public SortedMap<String, Collection<QueryLog>> prefixMap(String query)
    {
        return trie.prefixMap(query);
    }
}
