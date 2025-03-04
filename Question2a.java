/*
Question 2 (a):
You have a team of n employees, and each employee is assigned a performance rating given in the
integer array ratings. You want to assign rewards to these employees based on the following rules:
Every employee must receive at least one reward.
Employees with a higher rating must receive more rewards than their adjacent colleagues.
Goal:
Determine the minimum number of rewards you need to distribute to the employees.
Input:
ratings: The array of employee performance ratings.
Output:
The minimum number of rewards needed to distribute. 
*/

// SOlution:

/*
 * Algorithm Explanation:
 * 
 * The problem can be solved using a greedy approach:
 * 
 * 1. Left to Right Pass:
 *    - Start with 1 reward for each employee
 *    - If current rating > previous rating, current gets previous + 1 rewards
 * 
 * 2. Right to Left Pass:
 *    - Compare each employee with their right neighbor
 *    - If current rating > right rating, current should get max of:
 *      - its current rewards
 *      - right neighbor's rewards + 1
 * 
 * This ensures both conditions are met:
 * - Every employee gets at least one reward
 * - Higher rated employees get more than their adjacent colleagues
 * 
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 */

public class Question2a {

    // Function to calculate the minimum number of rewards
    public static int minRewards(int[] ratings) {
        int n = ratings.length;

        // Base case: if there are no ratings, no rewards are needed
        if (n == 0)
            return 0;

        // Step 1: Initialize rewards array with 1 for each employee (every employee
        // gets at least one reward)
        int[] rewards = new int[n];
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        // Step 2: Left to Right pass
        // Traverse from left to right to ensure employees with higher ratings than
        // their previous ones
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Step 3: Right to Left pass
        // Traverse from right to left to ensure employees with higher ratings than
        // their next ones
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Calculate total rewards by summing the rewards array
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        // Return the total number of rewards
        return totalRewards;
    }

    public static void main(String[] args) {
        // Test case 1
        int[] ratings1 = { 1, 0, 2 };
        System.out.println("Test Case 1: " + minRewards(ratings1)); // Output: 5

        // Test case 2
        int[] ratings2 = { 1, 2, 2 };
        System.out.println("Test Case 2: " + minRewards(ratings2)); // Output: 4

        // Test case 3
        int[] ratings3 = { 4, 3, 2, 1 };
        System.out.println("Test Case 3: " + minRewards(ratings3)); // Output: 10

        // Test case 4
        int[] ratings4 = { 1, 2, 3, 4 };
        System.out.println("Test Case 4: " + minRewards(ratings4)); // Output: 10

        // Test case 5: All ratings are the same
        int[] ratings5 = { 1, 3, 2, 4 };
        System.out.println("Test Case 5: " + minRewards(ratings5)); // Output: 7
    }
}

/*
 * Test Results:
 * ------------
 * Test Case 1:
 * ratings = [1, 0, 2]
 * Output: 5
 * Explanation:
 * - Initial rewards: [1, 1, 1]
 * - After left to right: [1, 1, 2]
 * - After right to left: [2, 1, 2]
 * - Total rewards = 2 + 1 + 2 = 5
 * 
 * Test Case 2:
 * ratings = [1, 2, 2]
 * Output: 4
 * Explanation:
 * - Initial rewards: [1, 1, 1]
 * - After left to right: [1, 2, 1]
 * - After right to left: [1, 2, 1]
 * - Total rewards = 1 + 2 + 1 = 4
 * 
 * Test Case 3:
 * ratings = [4, 3, 2, 1]
 * Output: 10
 * Explanation:
 * - Initial rewards: [1, 1, 1, 1]
 * - After left to right: [1, 1, 1, 1]
 * - After right to left: [4, 3, 2, 1]
 * - Total rewards = 4 + 3 + 2 + 1 = 10
 * 
 * Test Case 4:
 * ratings = [1, 2, 3, 4]
 * Output: 10
 * Explanation:
 * - Initial rewards: [1, 1, 1, 1]
 * - After left to right: [1, 2, 3, 4]
 * - After right to left: [1, 2, 3, 4]
 * - Total rewards = 1 + 2 + 3 + 4 = 10
 * 
 * Test Case 5:
 * ratings = [1, 3, 2, 4]
 * Output: 7
 * Explanation:
 * - Initial rewards: [1, 1, 1, 1]
 * - After left to right: [1, 2, 1, 2]
 * - After right to left: [1, 3, 1, 2]
 * - Total rewards = 1 + 3 + 1 + 2 = 7
 */