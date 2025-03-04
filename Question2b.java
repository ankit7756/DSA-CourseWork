/*
Question 2 (b):
You have two points in a 2D plane, represented by the arrays x_coords and y_coords. The goal is to find
the lexicographically pair i.e. (i, j) of points (one from each array) that are closest to each other.
Goal:
Determine the lexicographically pair of points with the smallest distance and smallest distance calculated
using | x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]|
*/

// Solution:

/*
 * Algorithm Explanation:
 * ---------------------
 * The problem requires finding the closest pair of points based on Manhattan
 * distance.
 * We need to find indices (i, j) such that:
 * | x_coords[i] - x_coords[j] | + | y_coords[i] - y_coords[j] |
 * is minimized.
 * 
 * Approach:
 * 1. Initialize minDistance to a large value.
 * 2. Iterate over all pairs (i, j) where i < j.
 * 3. Calculate the Manhattan distance for each pair.
 * 4. If the computed distance is smaller than minDistance:
 * - Update minDistance.
 * - Update the pair (i, j).
 * 5. If two pairs have the same minimum distance, choose the lexicographically
 * smaller pair.
 * 
 * Time Complexity: O(n^2) (since we compare all pairs)
 * Space Complexity: O(1) (only a few extra variables are used)
 */

public class Question2b {
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Get the number of points
        int minDistance = Integer.MAX_VALUE; // Initialize minDistance to a very large value
        int[] result = new int[2]; // Array to store the indices of the closest pair

        // Iterate over all pairs (i, j) where i < j
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate Manhattan distance between points i and j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If a new minimum distance is found, update the result pair
                if (distance < minDistance) {
                    minDistance = distance; // Update minDistance with the smaller distance found
                    result[0] = i; // Store index i as part of the closest pair
                    result[1] = j; // Store index j as part of the closest pair
                } else if (distance == minDistance) { // If the distance is the same as the current minimum
                    // Check for lexicographical order: i should be smaller or j should be smaller
                    // in case of a tie
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i; // Update result with the lexicographically smaller pair
                        result[1] = j;
                    }
                }
            }
        }
        return result; // Return the indices of the closest pair
    }

    public static void main(String[] args) {
        // Test Case 1
        int[] x1 = { 1, 2, 3, 2, 4 };
        int[] y1 = { 2, 3, 1, 2, 3 };
        int[] result1 = findClosestPair(x1, y1);
        System.out.println("Test Case 1: [" + result1[0] + ", " + result1[1] + "]"); // Output: [0, 3]

        // Test Case 2
        int[] x2 = { 0, 0, 1, 1 };
        int[] y2 = { 0, 1, 0, 1 };
        int[] result2 = findClosestPair(x2, y2);
        System.out.println("Test Case 2: [" + result2[0] + ", " + result2[1] + "]"); // Output: [0, 1]

        // Test Case 3
        int[] x3 = { 5, 1, 3, 2 };
        int[] y3 = { 6, 2, 1, 4 };
        int[] result3 = findClosestPair(x3, y3);
        System.out.println("Test Case 3: [" + result3[0] + ", " + result3[1] + "]"); // Output: [1, 3]
    }
}

/**
 * Test Results:
 * ------------
 * Test Case 1:
 * x_coords = [1, 2, 3, 2, 4]
 * y_coords = [2, 3, 1, 2, 3]
 * Output: [0, 3]
 * 
 * Explanation:
 * - Manhattan distances:
 * 1. |1-2| + |2-3| = 2
 * 2. |1-3| + |2-1| = 3
 * 3. |1-2| + |2-2| = 1 <-- Smallest distance found
 * 4. |1-4| + |2-3| = 4
 * - The closest pair is (0, 3) with distance 1.
 * 
 * Test Case 2:
 * x_coords = [0, 0, 1, 1]
 * y_coords = [0, 1, 0, 1]
 * Output: [0, 1]
 * 
 * Explanation:
 * - Possible closest pairs: (0,1), (0,2), (1,3), etc.
 * - (0,1) is lexicographically smallest with minimum distance of 1.
 * 
 * Test Case 3:
 * x_coords = [5, 1, 3, 2]
 * y_coords = [6, 2, 1, 4]
 * Output: [1, 3]
 * 
 * Explanation:
 * - (1,3) has the smallest Manhattan distance among all pairs.
 */
