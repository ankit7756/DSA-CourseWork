
/*
 * Algorithm Explanation:
 * 
 * In this problem, we want to determine the minimum number of roads that must be traversed 
 * to collect all packages and return to the starting location. Each location in the city is 
 * represented as a node in a graph, and roads between locations are the edges.
 *
 * Key Points:
 * 1. Each node may have a package (denoted by 1) or not (denoted by 0).
 * 2. From any starting node, you can collect packages from all nodes that are within a distance 
 *    of 2 (using BFS to compute distances).
 * 3. You can move to an adjacent node, and if needed, collect additional packages from that new 
 *    position (again within a distance of 2).
 * 4. The goal is to compute the minimum number of roads traversed such that all packages are 
 *    collected and you return to your starting location.
 *
 * Approach:
 * - Construct the graph using an adjacency list.
 * - For each possible starting node, use BFS to determine nodes reachable within a distance of 2 
 *   and collect packages.
 * - If some packages remain uncollected, simulate a move to each adjacent node, update the collection 
 *   using BFS from the new node, and calculate the total roads used (including the return trip).
 * - The answer is the minimum roads traversed over all starting nodes. If it’s impossible to collect 
 *   all packages, return -1.
 *
 * Note: This code assumes that traversing a road multiple times is counted separately each time.
 */

import java.util.*; // For collections, ArrayList, HashMap, etc.

public class Question4b {

    // Method to compute the minimum number of roads required to collect all
    // packages and return
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length; // Number of nodes in the graph

        // Build adjacency list for the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) { // For each node
            graph.add(new ArrayList<>()); // Initialize its list of adjacent nodes
        }
        for (int[] road : roads) { // For each road
            graph.get(road[0]).add(road[1]); // Add neighbor for node at road[0]
            graph.get(road[1]).add(road[0]); // Add neighbor for node at road[1] (Undirected graph)
        }

        int minRoads = Integer.MAX_VALUE; // Initialize minimum roads to a large value

        // Try each node as a possible starting point
        for (int start = 0; start < n; start++) {
            // Get distances from the starting node using BFS
            int[] distances = bfs(graph, start, n);
            Set<Integer> collected = new HashSet<>(); // Set to track collected packages from within distance 2
            for (int i = 0; i < n; i++) {
                // If the node is within a distance of 2 and has a package, mark it as collected
                if (distances[i] <= 2 && packages[i] == 1) {
                    collected.add(i);
                }
            }

            // List to track the package nodes that remain uncollected from the current
            // start
            List<Integer> remaining = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (packages[i] == 1 && !collected.contains(i)) { // If package exists and wasn't collected
                    remaining.add(i);
                }
            }

            if (remaining.isEmpty()) {
                // If all packages are collected from the starting node, no additional roads are
                // needed
                minRoads = Math.min(minRoads, 0);
            } else {
                // Otherwise, try moving to each adjacent node from the starting point
                for (int next : graph.get(start)) {
                    int roadsUsed = 1; // Moving to the next adjacent node uses 1 road
                    Set<Integer> newCollected = new HashSet<>(collected); // Copy previously collected packages
                    // Perform BFS from the adjacent node to see what additional packages can be
                    // collected
                    int[] nextDistances = bfs(graph, next, n);
                    for (int i = 0; i < n; i++) {
                        // If within distance 2 from the new node and there's a package, add to
                        // collection
                        if (nextDistances[i] <= 2 && packages[i] == 1) {
                            newCollected.add(i);
                        }
                    }

                    // Check if all package locations have now been collected
                    boolean allCollected = true;
                    for (int i = 0; i < n; i++) {
                        if (packages[i] == 1 && !newCollected.contains(i)) {
                            allCollected = false;
                            break;
                        }
                    }

                    if (allCollected) {
                        // Add the distance from the adjacent node back to the starting node as return
                        // trip
                        roadsUsed += nextDistances[start];
                        // Update the minimum roads if the current path uses fewer roads
                        minRoads = Math.min(minRoads, roadsUsed);
                    }
                }
            }
        }

        // If no valid path was found, return -1; otherwise, return the minimum number
        // of roads found
        return minRoads == Integer.MAX_VALUE ? -1 : minRoads;
    }

    // BFS method to compute shortest distances from a starting node to all other
    // nodes
    private static int[] bfs(List<List<Integer>> graph, int start, int n) {
        int[] distances = new int[n]; // Array to store distances from the start node
        Arrays.fill(distances, -1); // Initialize all distances to -1 (unvisited)
        Queue<Integer> queue = new LinkedList<>(); // Queue for BFS
        queue.add(start); // Start from the given node
        distances[start] = 0; // Distance from start to itself is 0

        // Perform BFS
        while (!queue.isEmpty()) {
            int curr = queue.poll(); // Get the current node from the queue
            for (int neighbor : graph.get(curr)) { // For each neighbor of the current node
                if (distances[neighbor] == -1) { // If the neighbor has not been visited yet
                    distances[neighbor] = distances[curr] + 1; // Update distance to neighbor
                    queue.add(neighbor); // Add neighbor to the queue for further exploration
                }
            }
        }
        return distances; // Return the computed distances
    }

    // Main method to run test cases
    public static void main(String[] args) {
        // Test Case 1:
        // packages1: location 0 and 5 have packages; others don't.
        // roads1: Linear road from 0 to 5.
        int[] packages1 = { 1, 0, 0, 0, 0, 1 };
        int[][] roads1 = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } };
        // Expected Output: 2
        System.out.println("Test 1: " + minRoads(packages1, roads1)); // Print result for Test Case 1

        // Test Case 2:
        // packages2: packages at locations 3, 4, and 7.
        // roads2: Tree-like structure connecting the nodes.
        int[] packages2 = { 0, 0, 0, 1, 1, 0, 0, 1 };
        int[][] roads2 = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 4 }, { 2, 5 }, { 5, 6 }, { 5, 7 } };
        // Expected Output: 2
        System.out.println("Test 2: " + minRoads(packages2, roads2)); // Print result for Test Case 2
    }
}

/*
 * Test Results:
 * -------------
 * Test Case 1:
 * Input: packages = [1, 0, 0, 0, 0, 1], roads = [[0, 1], [1, 2], [2, 3], [3,
 * 4], [4, 5]]
 * Output: 2
 * Explanation:
 * - Starting at an optimal node, the algorithm collects the package at location
 * 0 and location 5
 * within a distance of 2. The calculated minimum number of roads to traverse
 * and return to the
 * starting point is 2.
 * 
 * Test Case 2:
 * Input: packages = [0,0,0,1,1,0,0,1], roads =
 * [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]]
 * Output: 2
 * Explanation:
 * - With an optimal starting point, the algorithm collects packages at
 * locations 3, 4, and 7 by
 * moving to an adjacent node and using BFS to collect packages within a
 * distance of 2, then
 * returning to the starting location. The minimum number of roads traversed is
 * 2.
 */
