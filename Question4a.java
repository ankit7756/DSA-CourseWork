/*
Question 4 (a):
Input: 
Tweets table:
| Column Name | Type 
| user_id | int 
| tweet_id| int 
tweet_date | date 
| tweet | varchar | 
tweet_id is the primary key (column with unique values) for this table. Each row of this table contains user_id, tweet_id, tweet_date and tweet.

Write a solution to find the top 3 trending hashtags in February 2024. Every tweet may 
contain several hashtags. 
Return the result table ordered by count of hashtag, hashtag in descending order. 
The result format is in the following example.

Example 1:
Input:
Tweet Tables:
| user_id | tweet_id | tweet | tweet_date | 
| 135 | 13 |  Enjoying a great start to the day. #HappyDay #MorningVibes | 2024-02-01 | 
| 136 | 14 |  Another #HappyDay with good vibes! #FeelGood | 2024-02-03 | 
| 137 | 15 |  Productivity peaks! #WorkLife #ProductiveDay | 2024-02-04 | 
| 138 | 16 |  Exploring new tech frontiers. #TechLife #Innovation | 2024-02-04 | 
| 139 | 17 |  Gratitude for today's moments. #HappyDay #Thankful | 2024-02-05 | 
| 140 | 18 |  Innovation drives us. #TechLife #FutureTech | 2024-02-07 | 
| 141 | 19 |  Connecting with nature's serenity. #Nature #Peaceful | 2024-02-09 |

Output:

| hashtag ❘ count | 
| #HappyDay | 3 | 
| #TechLife | 2 | 
| #WorkLife | 1 |

Explanation: 
#HappyDay: Appeared in tweet IDs 13, 14, and 17, with a total count of 3 mentions. 
#TechLife: Appeared in tweet IDs 16 and 18, with a total count of 2 mentions. 
#WorkLife: Appeared in tweet ID 15, with a total count of 1 mention. 
Note: Output table is sorted in descending order by hashtag_count and hashtag respectively
 */

// Solution:

/*
    Algorithm Explanation:
    1. We first define the `Tweet` class to store the tweet's details such as userId, tweetId, tweet content, and the date the tweet was posted.
    2. We create a list of `Tweet` objects with sample data representing tweets from different users.
    3. We define a date range (from February 1st, 2024 to February 29th, 2024) and filter out any tweets that are not in this date range.
    4. We define a regular expression to identify hashtags in the tweet text (hashtags start with '#').
    5. For each tweet in the filtered list, we use the regex to find all hashtags and count their occurrences using a `HashMap`.
    6. Once we have the counts for all hashtags, we sort them:
        - First by the frequency of the hashtags (in descending order).
        - If multiple hashtags have the same frequency, we sort them lexicographically (in descending order).
    7. Finally, we print the top 3 hashtags, or fewer if there are not enough hashtags.
*/

import java.util.*; // For collections, ArrayList, HashMap, etc.
import java.util.regex.*; // For regex operations (Pattern, Matcher)
import java.time.LocalDate; // For handling dates
import java.time.format.DateTimeFormatter; // For parsing dates

class Tweet {
    int userId; // ID of the user who posted the tweet
    int tweetId; // Unique tweet ID
    String tweet; // Tweet content
    LocalDate tweetDate; // Date when the tweet was posted

    // Constructor to initialize a Tweet object
    public Tweet(int userId, int tweetId, String tweet, LocalDate tweetDate) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.tweet = tweet;
        this.tweetDate = tweetDate;
    }
}

public class Question4a {
    public static void main(String[] args) {
        // Define a date formatter to parse date strings in "yyyy-MM-dd" format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Create a list of tweets using the sample input data
        List<Tweet> tweets = Arrays.asList(
                new Tweet(135, 13, "Enjoying a great start to the day. #HappyDay #MorningVibes",
                        LocalDate.parse("2024-02-01", formatter)),
                new Tweet(136, 14, "Another #HappyDay with good vibes! #FeelGood",
                        LocalDate.parse("2024-02-03", formatter)),
                new Tweet(137, 15, "Productivity peaks! #WorkLife #ProductiveDay",
                        LocalDate.parse("2024-02-04", formatter)),
                new Tweet(138, 16, "Exploring new tech frontiers. #TechLife #Innovation",
                        LocalDate.parse("2024-02-04", formatter)),
                new Tweet(139, 17, "Gratitude for today's moments. #HappyDay #Thankful",
                        LocalDate.parse("2024-02-05", formatter)),
                new Tweet(140, 18, "Innovation drives us. #TechLife #FutureTech",
                        LocalDate.parse("2024-02-07", formatter)),
                new Tweet(141, 19, "Connecting with nature's serenity. #Nature #Peaceful",
                        LocalDate.parse("2024-02-09", formatter)));

        // Define the date range for February 2024
        LocalDate startDate = LocalDate.parse("2024-02-01", formatter);
        LocalDate endDate = LocalDate.parse("2024-02-29", formatter);

        // Create a map to hold hashtag counts (key: hashtag, value: frequency)
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Define a regex pattern to match hashtags (i.e., words that start with '#')
        Pattern pattern = Pattern.compile("#\\w+");

        // Process each tweet in the list
        for (Tweet tweet : tweets) {
            // Filter tweets to include only those in February 2024
            if (!tweet.tweetDate.isBefore(startDate) && !tweet.tweetDate.isAfter(endDate)) {
                // Create a matcher to find all hashtags in the tweet content
                Matcher matcher = pattern.matcher(tweet.tweet);
                while (matcher.find()) {
                    String hashtag = matcher.group(); // Extract the hashtag
                    // Update the count for this hashtag in the map
                    hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Convert the hashtag count map to a list of entries for sorting
        List<Map.Entry<String, Integer>> list = new ArrayList<>(hashtagCount.entrySet());

        // Sort the list: first by count in descending order, then by hashtag in
        // descending order (if counts are equal)
        Collections.sort(list, (a, b) -> {
            if (!b.getValue().equals(a.getValue())) {
                return b.getValue() - a.getValue(); // Higher counts first
            } else {
                return b.getKey().compareTo(a.getKey()); // Descending lexicographical order for hashtags
            }
        });

        // Determine the top 3 trending hashtags (or fewer if there aren't enough)
        int top = Math.min(3, list.size());
        System.out.println("| hashtag   | count |");
        // Print the top 3 hashtags in the required format
        for (int i = 0; i < top; i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            System.out.println("| " + entry.getKey() + " | " + entry.getValue() + " |");
        }
    }
}

/*
 * Test Results:
 * -------------
 * When running the above code with the sample data, the following output is
 * produced:
 * 
 * | hashtag | count |
 * | #HappyDay | 3 |
 * | #TechLife | 2 |
 * | #WorkLife | 1 |
 * 
 * Explanation:
 * - "#HappyDay" appears in tweet IDs 13, 14, and 17 (total count: 3).
 * - "#TechLife" appears in tweet IDs 16 and 18 (total count: 2).
 * - "#WorkLife" appears in tweet ID 15 (total count: 1).
 * 
 * This matches the expected output for the top 3 trending hashtags in February
 * 2024.
 */
