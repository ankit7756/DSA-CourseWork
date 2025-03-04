/**
 * Algorithm Explanation:
 * The problem is solved using Dynamic Programming approach where:
 * - We use a 2D DP table where dp[k][n] represents min measurements needed with k samples and n levels
 * - For each state (k,n), we try different first measurement points and take minimum of worst cases
 * - The recurrence relation considers both cases: when material changes and when it doesn't
 * 
 * Time Complexity: O(k * n * n)
 * Space Complexity: O(k * n)
 */

// Importing required packages
import java.util.Scanner;

public class Question1a {
    // Function to calculate the minimum measurements needed
    public static int minMeasurements(int k, int n) {
        // Create a DP table where dp[k][n] represents the minimum tests needed
        int[][] dp = new int[k + 1][n + 1];

        // Base cases:
        // When n = 0 (no levels to test), 0 tests are needed
        for (int i = 0; i <= k; i++) {
            dp[i][0] = 0;
        }

        // When k = 1 (one sample), all levels must be tested sequentially
        for (int j = 0; j <= n; j++) {
            dp[1][j] = j;
        }

        // Fill the DP table for k samples and n levels
        for (int i = 2; i <= k; i++) { // Loop over number of samples
            for (int j = 1; j <= n; j++) { // Loop over number of levels
                dp[i][j] = Integer.MAX_VALUE; // Initialize to maximum value

                // Check each possible level to test
                for (int x = 1; x <= j; x++) {
                    // Calculate the worst case: either the sample breaks or it doesn't
                    int worstCase = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    // Take the minimum of the worst cases
                    dp[i][j] = Math.min(dp[i][j], worstCase);
                }
            }
        }

        // The result is stored in dp[k][n]
        return dp[k][n];
    }

    // Main function to test the program
    public static void main(String[] args) {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Input: Number of samples (k) and temperature levels (n)
        System.out.print("Enter the number of samples (k): ");
        int k = scanner.nextInt();
        System.out.print("Enter the number of temperature levels (n): ");
        int n = scanner.nextInt();

        // Calculate the minimum measurements required
        int result = minMeasurements(k, n);

        // Output the result
        System.out.println("Minimum measurements required: " + result);

        // Close the scanner
        scanner.close();
    }
}

/*
 * Test Results:
 * 
 * Test Case 1:
 * Input: k = 1, n = 2
 * Output: 2
 * 
 * Explanation for k=1, n=2:
 * - With 1 sample and 2 temperature levels:
 * - First measurement at level 1
 * - If material changes, f=0
 * - If no change, test at level 2 to determine if f=1 or f=2
 * - Total measurements needed: 2
 * 
 * Test Case 2:
 * Input: k = 2, n = 6
 * Output: 3
 * 
 * Explanation for k=2, n=6:
 * - With 2 samples and 6 temperature levels:
 * - First measurement optimally at level 3
 * - Based on result, next measurement narrows down the critical point
 * - Maximum 3 measurements needed in worst case
 * 
 * Test Case 3:
 * Input: k = 3, n = 14
 * Output: 4
 * 
 * Explanation for k=3, n=14:
 * - With 3 samples and 14 temperature levels:
 * - Can use optimal strategy to reduce measurements
 * - Maximum 4 measurements needed in worst case
 * 
 * Test Case 4:
 * Input: k = 1, n = 5
 * Output: 5
 * 
 * Explanation for k=1, n=5:
 * - With 1 sample and 5 temperature levels:
 * - Must use sequential testing strategy since we have only one sample
 * - First measurement at level 1
 * - If no change, test level 2
 * - If still no change, test level 3, and so on
 * - In worst case, need to test all 5 levels sequentially
 * - Total measurements needed: 5 (one for each temperature level)
 * 
 * Test Case 5:
 * Input: k = 2, n = 10
 * Output: 4
 * 
 * Explanation for k=2, n=10:
 * - With 2 samples and 10 temperature levels:
 * - First measurement optimally at level 4
 * - If material changes, use second sample to binary search levels 1-3
 * - If no change, use remaining sample to test remaining 6 levels optimally
 * - This divides the problem into smaller subproblems
 * - Maximum 4 measurements needed in worst case scenario
 * 
 */