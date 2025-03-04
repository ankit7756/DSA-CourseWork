
/*
 * Algorithm Overview:
 * 1. Multithreaded Web Crawler Design:
 *    - Create a fixed thread pool using ExecutorService
 *    - Define a CrawlerTask that handles individual URL crawling
 *    - Use concurrent data structures to manage thread-safe operations
 *    - Process multiple URLs concurrently to improve crawling efficiency
 * 
 * Key Components:
 * - Thread Pool: Manages concurrent execution of crawling tasks
 * - Concurrent Set: Tracks visited URLs to prevent duplicates
 * - Synchronized List: Stores crawled content thread-safely
 * 
 * Workflow:
 * a) Initialize a list of URLs to crawl
 * b) Create a thread pool with a fixed number of threads
 * c) Submit crawling tasks for each URL
 * d) Execute tasks concurrently
 * e) Collect and process results
 * f) Shutdown the executor service
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Question6b {
    // Number of threads in the thread pool for concurrent crawling
    private static final int NUM_THREADS = 5;

    // Concurrent set to track visited URLs and prevent duplicate crawling
    // Uses ConcurrentHashMap.newKeySet() for thread-safe operations
    private static Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    // Thread-safe list to store crawled content
    // Synchronized to prevent race conditions during updates
    private static List<String> crawledContent = new ArrayList<>();

    // Inner class representing a crawling task
    // Implements Runnable to allow execution in a thread pool
    private static class CrawlerTask implements Runnable {
        // URL to be crawled
        private String url;

        // Constructor to initialize the URL for crawling
        public CrawlerTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            // Attempt to add URL to visited set
            // If URL already exists, skip crawling to avoid duplicates
            if (!visitedUrls.add(url)) {
                return;
            }

            try {
                // Create URL object for connection
                URL website = new URL(url);

                // Open HTTP connection to the website
                HttpURLConnection connection = (HttpURLConnection) website.openConnection();

                // Configure connection parameters
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000); // 5 seconds connection timeout
                connection.setReadTimeout(5000); // 5 seconds read timeout

                // Read webpage content
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {

                    // Build content from webpage
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        content.append(inputLine);
                    }

                    // Thread-safe addition of crawled content
                    // Synchronized to prevent concurrent modification
                    synchronized (crawledContent) {
                        crawledContent.add("URL: " + url + " Content Length: " + content.length());
                    }

                    // Print crawled URL for tracking
                    System.out.println("Crawled: " + url);
                }
            } catch (Exception e) {
                // Handle and print any crawling errors
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // List of predefined URLs to crawl
        String[] urlsToCrawl = {
                "https://www.example.com",
                "https://www.wikipedia.org",
                "https://www.github.com",
                "https://www.stackoverflow.com",
                "https://www.openai.com",
                "https://www.mit.edu",
                "https://www.nasa.gov",
                "https://www.python.org",
                "https://www.w3schools.com",
                "https://www.apache.org"
        };

        // Create a fixed thread pool for concurrent crawling
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit crawling tasks for each URL
        for (String url : urlsToCrawl) {
            executorService.submit(new CrawlerTask(url));
        }

        // Initiate orderly shutdown of executor service
        executorService.shutdown();

        try {
            // Wait for all tasks to complete (maximum 1 minute)
            executorService.awaitTermination(1, TimeUnit.MINUTES);

            // Print crawling summary
            System.out.println("\nCrawling Complete!");
            System.out.println("Total URLs Crawled: " + visitedUrls.size());
            System.out.println("\nCrawled Content Summary:");
            crawledContent.forEach(System.out::println);

        } catch (InterruptedException e) {
            // Handle thread interruption during crawling
            System.err.println("Crawling process interrupted: " + e.getMessage());
        }
    }
}

/*
 * Expected Test Results:
 * 1. Concurrent Execution:
 * - Multiple URLs should be crawled simultaneously
 * - Reduced total crawling time compared to sequential crawling
 * 
 * 2. Output Verification:
 * - Each URL should be printed as "Crawled: [URL]"
 * - Total URLs crawled should match input URLs
 * - Content length for each URL will be displayed
 * 
 * 3. Error Handling:
 * - Errors for unreachable or slow websites will be printed
 * - Crawler continues processing other URLs even if some fail
 * 
 * 4. Performance Metrics (Approximate):
 * - Thread Pool Size: 5 threads
 * - Total URLs: 10
 * - Expected Crawling Time: Significantly reduced vs sequential crawling
 * 
 * Potential Limitations:
 * - No deep link crawling
 * - Basic error handling
 * - Fixed timeout values
 * - No sophisticated content parsing
 */