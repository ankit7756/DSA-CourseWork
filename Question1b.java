/**
 * Algorithm Explanation:
 * ---------------------
 * The problem can be solved using a Min-Heap (Priority Queue) approach:
 * 1. For each element in returns1[i], we first add (returns1[i] * returns2[0]) to the min heap
 *    along with indices (i, 0)
 * 2. Then, we perform k-1 operations:
 *    - Remove the minimum element from heap
 *    - If j+1 < returns2.length, add the next product with (i, j+1) to heap
 * 3. After k-1 operations, the top of heap is our kth smallest product
 * 
 * Time Complexity: O(k * log(n)) where n is the length of returns1
 * Space Complexity: O(n) for the priority queue
 */


import java.util.PriorityQueue;

public class Question1b {
    // Function to calculate the kth lowest combined return
    public static int kthLowestReturn(int[] returns1, int[] returns2, int k) {
        // Min-heap to store the smallest combined returns
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Initialize the heap with the first row of combinations (returns1[0] *
        // returns2[j])
        for (int j = 0; j < Math.min(k, returns2.length); j++) {
            minHeap.offer(new int[] { returns1[0] * returns2[j], 0, j });
        }

        // Process the heap to find the kth smallest product
        int result = 0;
        while (k > 0 && !minHeap.isEmpty()) {
            // Extract the smallest element
            int[] current = minHeap.poll();
            result = current[0]; // Current kth smallest value
            int i = current[1], j = current[2];

            // If there are more elements in the current row, push the next combination
            if (i + 1 < returns1.length) {
                minHeap.offer(new int[] { returns1[i + 1] * returns2[j], i + 1, j });
            }

            // Decrement k
            k--;
        }

        return result; // The kth smallest combined return
    }

    // Main function to test the program
    public static void main(String[] args) {
        // Test case 1
        int[] returns1a = { 2, 5 };
        int[] returns2a = { 3, 4 };
        int ka = 2;
        System.out.println("Test Case 1: " + kthLowestReturn(returns1a, returns2a, ka)); // Output: 8

        // Test case 2
        int[] returns1b = { -4, -2, 0, 3 };
        int[] returns2b = { 2, 4 };
        int kb = 6;
        System.out.println("Test Case 2: " + kthLowestReturn(returns1b, returns2b, kb)); // Output: 0
    }
}

/*
 * Test Results:
 * 
 * Test Case 1:
 * returns1 = [2, 5]
 * returns2 = [3, 4]
 * k = 2
 * Output: 8
 * 
 * Explanation for Test Case 1:
 * - Initial heap contains: (2*3=6), (2*4=8)
 * - Products in ascending order:
 * 1. 2*3 = 6
 * 2. 2*4 = 8
 * 3. 5*3 = 15
 * 4. 5*4 = 20
 * - 2nd smallest product is 8
 *
 * Test Case 2:
 * returns1 = [-4, -2, 0, 3]
 * returns2 = [2, 4]
 * k = 6
 * Output: 0
 * 
 * Explanation for Test Case 2:
 * - Products in ascending order:
 * 1. -4*4 = -16
 * 2. -4*2 = -8
 * 3. -2*4 = -8
 * 4. -2*2 = -4
 * 5. 0*2 = 0
 * 6. 0*4 = 0
 * 7. 3*2 = 6
 * 8. 3*4 = 12
 * - 6th smallest product is 0
 */
