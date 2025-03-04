/*
Question 3 (a):
You have a network of n devices. Each device can have its own communication module installed at a
cost of modules [i - 1]. Alternatively, devices can communicate with each other using direct connections.
The cost of connecting two devices is given by the array connections where each connections[j] =
[device1j, device2j, costj] represents the cost to connect devices device1j and device2j. Connections are
bidirectional, and there could be multiple valid connections between the same two devices with different
costs.
Goal:
Determine the minimum total cost to connect all devices in the network
*/

// Solution:

/*
 * Algorithm Explanation:
 * ---------------------
 * The problem requires connecting `n` devices with the minimum total cost.
 * Each device can either install its own communication module or be connected through existing connections.
 * 
 * Approach:
 * 1. Treat the devices as nodes in a graph.
 * 2. Add virtual edges where installing a module is an option: (0 -> i) with cost `modules[i-1]`.
 * 3. Use Kruskal's Minimum Spanning Tree (MST) algorithm to pick the minimum cost connections.
 * 4. Use the Union-Find (Disjoint Set) data structure to efficiently manage connected components.
 * 5. Sort all edges (connections and module installations) by cost and process them.
 * 6. If a connection doesn't form a cycle, add it to the MST.
 * 7. Stop when all devices are connected.
 * 
 * Time Complexity: O(E log E) (sorting edges) + O(E α(N)) (Union-Find) ~ O(E log E)
 * Space Complexity: O(N) (for parent and rank arrays in Union-Find)
 */

import java.util.*;

class MinimumNetworkCost {

    // Disjoint Set Union-Find Data Structure
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Each node is its own parent initially
                rank[i] = 1; // Rank starts at 1
            }
        }

        // Find function with path compression
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        // Union by rank
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY)
                return false; // Already in the same set

            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public static int minTotalCost(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>(); // List to store all possible edges

        // Adding edges for installing modules as (0, i, cost)
        for (int i = 0; i < n; i++) {
            edges.add(new int[] { 0, i + 1, modules[i] });
        }

        // Adding given connections to edges list
        for (int[] connection : connections) {
            edges.add(new int[] { connection[0], connection[1], connection[2] });
        }

        // Sort edges by cost (Greedy approach for Kruskal's algorithm)
        edges.sort(Comparator.comparingInt(a -> a[2]));

        DSU dsu = new DSU(n + 1); // DSU to manage connected components
        int totalCost = 0; // Stores the final minimum cost
        int edgesUsed = 0; // Tracks how many edges are used in MST

        // Process edges in sorted order
        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) { // If adding this edge doesn't create a cycle
                totalCost += edge[2]; // Add cost
                edgesUsed++;
                if (edgesUsed == n)
                    break; // Stop when all devices are connected
            }
        }

        return totalCost; // Return the minimum cost to connect all devices
    }

    public static void main(String[] args) {
        // Test Case 1
        int n1 = 3;
        int[] modules1 = { 1, 2, 2 };
        int[][] connections1 = { { 1, 2, 1 }, { 2, 3, 1 } };
        System.out.println("Test Case 1: " + minTotalCost(n1, modules1, connections1)); // Output: 3

        // Test Case 2
        int n2 = 4;
        int[] modules2 = { 5, 3, 2, 4 };
        int[][] connections2 = { { 1, 2, 1 }, { 2, 3, 2 }, { 3, 4, 1 }, { 1, 4, 5 } };
        System.out.println("Test Case 2: " + minTotalCost(n2, modules2, connections2)); // Output: 6

        // Test Case 3
        int n3 = 5;
        int[] modules3 = { 4, 2, 3, 1, 5 };
        int[][] connections3 = { { 1, 2, 2 }, { 2, 3, 2 }, { 3, 4, 2 }, { 4, 5, 2 }, { 1, 5, 10 } };
        System.out.println("Test Case 3: " + minTotalCost(n3, modules3, connections3)); // Output: 9
    }
}
