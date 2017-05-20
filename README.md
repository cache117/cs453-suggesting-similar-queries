# Project 2 - Suggesting Queries Based on Word Similarity and Query Modification Patterns


This is an implementation of the query-suggestion approach, WebQS, presented in the paper, "Assisting Web Search Using Query Suggestion Based on Word Similarity Measure and Query Modification Patterns," published in the Journal of World Wide Web, Volume 17, Number 5, 2014.

WebQS provides a guide to the users for formulating/completing a keyword query Q using suggested keywords (extracted from the AOL query logs) as potential keywords in Q. The query-suggestion approach considers initial and modified queries in the AOL query logs, along with word-similarity measures, in making query suggestions. WebQS facilitates the formulation of queries in a trie data structure and determines the rankings of suggested keyword queries using distinguished features exhibited in the raw data in the AOL query logs.
WebQS relies on the AOL query logs to suggest queries. The logs of AOL, which include 50 million queries that were created by millions of AOL users over a three-month period between March 1, 2006 and May 31, 2006. These logs are found in [the resource directory](src/main/resources).
An AOL query log includes a number of query sessions, each of which captures a period of sustained user activities on the search engine. Each AOL session differs in length and includes a 
* (i) user ID, 
  * A user ID, which is an anonymous identifier of its user who performs the search, determines the boundary of each session (as each user ID is associated with a distinct session).
* (ii) the query text, 
  * Query text are keywords in a user query and multiple queries may be created under the same session.
* (iii) date and time of search,
  * The date and time of a search can be used to determine whether two or more queries were created by the same user within 10 minutes, which is the time period that dictates whether two queries should be treated as related.
* (iv) optionally clicked documents. 
  * Clicked documents are retrieved documents that the user has clicked on and are ranked by the search engine. Queries and documents include stopwords, which are commonly-occurring keywords, such as prepositions, articles, and pronouns, that carry little meaning and often do not represent the content of a document. Stopwords are not considered by WebQS during the query creation process.asdasd
