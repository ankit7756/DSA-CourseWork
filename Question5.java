/* Algorithm Explanation:
* 
* This application is a Java Swing-based GUI tool designed to help network administrators 
* visualize and optimize a network topology with multiple objectives:
* 
* 1. Add Node:
*    - A node represents a network device (e.g., a server or a client). Nodes are placed 
*      at random positions on the display panel.
* 
* 2. Add Edge:
*    - An edge represents a connection between two nodes, with associated attributes:
*         • Cost: Represents the monetary or resource cost of the connection.
*         • Bandwidth: Represents the capacity/speed of the connection.
*    - The user specifies these attributes, and the edge is added to the network.
* 
* 3. Calculate Shortest Path:
*    - Dijkstra's algorithm is used to determine the shortest path between two nodes.
*    - The algorithm considers the edge bandwidth as the weight (i.e., lower cumulative 
*      bandwidth implies a faster route).
* 
* 4. Optimize Network:
*    - Kruskal's algorithm (using a DisjointSet structure) is implemented to generate the 
*      Minimum Spanning Tree (MST) of the network. This provides an optimized network 
*      topology that minimizes the overall connection cost.
* 
* The GUI is built using standard Swing components (JFrame, JPanel, JButton, JTextArea) to 
* allow interactive creation, manipulation, and visualization of the network graph.
*/

import javax.swing.*; // Import Swing components for GUI
import java.awt.*; // Import AWT components for graphics and layout
import java.awt.event.*;
import java.util.*; // Import utility classes (e.g., ArrayList, HashMap)
import java.util.List; //  // Import List interface
import java.util.stream.Collectors; // Import Collectors for stream operations

// Main class for the Network Topology Application
public class Question5 {
    private JFrame frame; // JFrame for the main application window
    private JPanel panel; // // Main panel to hold GUI component
    private JButton addNodeButton, addEdgeButton, calculateButton, optimizeButton; // Buttons for various functions
    private JTextArea outputArea; // TextArea to display results (e.g., shortest path, MST info)
    private NetworkGraphPanel graphPanel; // Custom JPanel to display the network graph
    private List<Node> nodes; // List to store the network nodes
    private List<Edge> edges; // // List to store the network edges

    // Main method: Entry point of the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Ensure GUI is created on the Event Dispatch Thread
            try {
                Question5 window = new Question5(); // Create an instance of the app
                window.frame.setVisible(true); // Make the main frame visible
            } catch (Exception e) {
                e.printStackTrace(); // Print stack trace if an exception occurs
            }
        });
    }

    // Constructor: Initializes the lists and sets up the GUI components
    public Question5() {
        nodes = new ArrayList<>(); // Initialize the list for nodes
        edges = new ArrayList<>(); // Initialize the list for edges
        initialize(); // Call the method to set up the GUI components
    }

    // Method to initialize and layout the GUI components
    private void initialize() {
        // Set up the main frame
        frame = new JFrame(); // Create a new JFrame
        frame.setBounds(100, 100, 800, 600); // Set the frame size and position
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the frame is closed
        frame.getContentPane().setLayout(new BorderLayout()); // Use BorderLayout for the frame's content pane

        // Create the panel to hold the components
        panel = new JPanel(); // Create a new JPanel
        frame.getContentPane().add(panel, BorderLayout.CENTER); // Add the panel to the center of the frame
        panel.setLayout(null); // Set layout to null for absolute positioning of components

        // Create the graph panel that will display the network graph
        graphPanel = new NetworkGraphPanel(); // Instantiate the custom graph panel
        graphPanel.setBounds(10, 50, 760, 400); // Set the position and size of the graph panel
        panel.add(graphPanel); // Add the graph panel to the main panel

        // Create buttons for user actions and set their positions
        addNodeButton = new JButton("Add Node"); // Create a button to add a node
        addNodeButton.setBounds(10, 10, 120, 30); // Set button position and size
        panel.add(addNodeButton); // Add the button to the panel

        addEdgeButton = new JButton("Add Edge"); // Create a button to add an edge
        addEdgeButton.setBounds(140, 10, 120, 30); // Set button position and size
        panel.add(addEdgeButton); // Add the button to the panel

        calculateButton = new JButton("Calculate Shortest Path"); // Create a button to calculate the shortest path
        calculateButton.setBounds(270, 10, 180, 30); // Set button position and size
        panel.add(calculateButton); // Add the button to the panel

        optimizeButton = new JButton("Optimize Network"); // Create a button to optimize the network
        optimizeButton.setBounds(460, 10, 150, 30); // Set button position and size
        panel.add(optimizeButton); // Add the button to the panel

        // Create the output area to display results such as cost and latency
        // information
        outputArea = new JTextArea(); // Instantiate the JTextArea
        outputArea.setBounds(10, 460, 760, 100); // Set the output area's position and size
        outputArea.setEditable(false); // Make the output area non-editable by the user
        panel.add(outputArea); // Add the output area to the panel

        // Set up action listeners for the buttons
        addNodeButton.addActionListener(e -> addNode()); // When clicked, call addNode() to add a new node
        addEdgeButton.addActionListener(e -> addEdge()); // When clicked, call addEdge() to add a new edge between nodes
        calculateButton.addActionListener(e -> calculateShortestPath()); // When clicked, calculate the shortest path //
                                                                         // using Dijkstra's algorithm
        optimizeButton.addActionListener(e -> optimizeNetwork()); // When clicked, optimize the network using Kruskal's
                                                                  // algorithm
    }

    // Method to add a node to the network
    private void addNode() {
        // Prompt the user for a node name
        String nodeName = JOptionPane.showInputDialog("Enter node name:");
        // If the node name is valid, proceed to add the node
        if (nodeName != null && !nodeName.trim().isEmpty()) {
            // Randomly generate x and y coordinates for the node
            int x = (int) (Math.random() * 700) + 50;
            int y = (int) (Math.random() * 400) + 50;
            // Create a new Node object and add it to the list of nodes
            Node newNode = new Node(nodeName, x, y);
            nodes.add(newNode);
            // Update the graph panel to display the new node
            graphPanel.addNode(newNode);
        }
    }

    // Method to add an edge between two nodes
    private void addEdge() {
        // Ensure there are at least two nodes to connect
        if (nodes.size() < 2) {
            JOptionPane.showMessageDialog(frame, "You need at least two nodes to add an edge.");
            return;
        }
        // Prompt the user for the names of the from and to nodes
        String fromNodeName = JOptionPane.showInputDialog("Enter from node name:");
        String toNodeName = JOptionPane.showInputDialog("Enter to node name:");
        // Find the nodes by name
        Node fromNode = findNodeByName(fromNodeName);
        Node toNode = findNodeByName(toNodeName);
        // If both nodes are valid, proceed to add the edge
        if (fromNode != null && toNode != null) {
            // Prompt the user for the edge's cost and bandwidth
            String costStr = JOptionPane.showInputDialog("Enter edge cost:");
            String bandwidthStr = JOptionPane.showInputDialog("Enter edge bandwidth:");
            try {
                // Parse the cost and bandwidth input values
                double cost = Double.parseDouble(costStr);
                double bandwidth = Double.parseDouble(bandwidthStr);
                // Create a new Edge object and add it to the list of edges
                Edge edge = new Edge(fromNode, toNode, cost, bandwidth);
                edges.add(edge);
                // Update the graph panel to display the new edge
                graphPanel.addEdge(edge);
            } catch (NumberFormatException ex) {
                // Handle invalid input for cost or bandwidth
                JOptionPane.showMessageDialog(frame, "Invalid input for cost or bandwidth.");
            }
        } else {
            // Handle invalid node names
            JOptionPane.showMessageDialog(frame, "Invalid node names.");
        }
    }

    // Method to find a node by its name
    private Node findNodeByName(String nodeName) {
        return nodes.stream().filter(node -> node.name.equals(nodeName)).findFirst().orElse(null);
    }

    // Method to calculate the shortest path between two nodes using Dijkstra's
    // algorithm
    private void calculateShortestPath() {
        // Ensure there are at least two nodes to calculate a path
        if (nodes.size() < 2) {
            JOptionPane.showMessageDialog(frame, "You need at least two nodes to calculate the shortest path.");
            return;
        }

        // Prompt the user for the start and end node names
        String startNodeName = JOptionPane.showInputDialog("Enter start node name:");
        String endNodeName = JOptionPane.showInputDialog("Enter end node name:");
        // Find the start and end nodes by name
        Node startNode = findNodeByName(startNodeName);
        Node endNode = findNodeByName(endNodeName);

        // If both nodes are valid, calculate and display the shortest path
        if (startNode != null && endNode != null) {
            List<Node> path = dijkstra(nodes, edges, startNode, endNode);
            StringBuilder pathOutput = new StringBuilder("Shortest path: ");
            path.forEach(node -> pathOutput.append(node.name).append(" -> "));
            outputArea.setText(pathOutput.toString());
        } else {
            // Handle invalid node names
            JOptionPane.showMessageDialog(frame, "Invalid node names.");
        }
    }

    // Method to optimize the network by finding the Minimum Spanning Tree (MST)
    // using Kruskal's algorithm
    private void optimizeNetwork() {
        // Ensure there are nodes and edges to optimize
        if (nodes.isEmpty() || edges.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please add nodes and edges before optimizing.");
            return;
        }
        // Compute the Minimum Spanning Tree (MST) of the network
        List<Edge> mst = kruskal(nodes, edges);
        // Format the MST output and display it
        StringBuilder mstOutput = new StringBuilder("Optimized Network (MST): ");
        mst.forEach(edge -> mstOutput.append(edge.node1.name).append(" -> ").append(edge.node2.name).append(" | Cost: ")
                .append(edge.cost).append(", Bandwidth: ").append(edge.bandwidth).append("\n"));
        outputArea.setText(mstOutput.toString());
    }

    // Dijkstra's algorithm for finding the shortest path between two nodes
    private List<Node> dijkstra(List<Node> nodes, List<Edge> edges, Node startNode, Node endNode) {
        // Maps for storing distance and previous node information
        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        // Initialize distances and previous nodes
        for (Node node : nodes) {
            dist.put(node, Double.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(startNode, 0.0);
        queue.add(startNode);

        // Process the nodes in the priority queue
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode.equals(endNode))
                break;

            // Check the neighbors of the current node and update distances
            for (Edge edge : edges) {
                if (edge.node1.equals(currentNode) || edge.node2.equals(currentNode)) {
                    Node neighbor = edge.node1.equals(currentNode) ? edge.node2 : edge.node1;
                    double newDist = dist.get(currentNode) + edge.bandwidth;

                    // If a shorter path is found, update the distance and previous node
                    if (newDist < dist.get(neighbor)) {
                        dist.put(neighbor, newDist);
                        prev.put(neighbor, currentNode);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Reconstruct the shortest path from end node to start node
        List<Node> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = prev.get(node)) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    // Kruskal's algorithm to find the Minimum Spanning Tree (MST) of the network
    private List<Edge> kruskal(List<Node> nodes, List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        DisjointSet ds = new DisjointSet(nodes.size());

        // Sort the edges by cost
        edges.sort(Comparator.comparingDouble(e -> e.cost));

        // Process the edges and add them to the MST if they do not form a cycle
        for (Edge edge : edges) {
            int node1Index = nodes.indexOf(edge.node1);
            int node2Index = nodes.indexOf(edge.node2);

            if (ds.find(node1Index) != ds.find(node2Index)) {
                ds.union(node1Index, node2Index);
                mst.add(edge);
            }
        }
        return mst;
    }

    // Helper class for Node, Edge, DisjointSet, and NetworkGraphPanel
    class Node {
        String name;
        int x, y;

        // Constructor to initialize a node with a name and coordinates
        public Node(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    class Edge {
        Node node1, node2;
        double cost, bandwidth;

        // Constructor to initialize an edge between two nodes with cost and bandwidth
        public Edge(Node node1, Node node2, double cost, double bandwidth) {
            this.node1 = node1;
            this.node2 = node2;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Helper class implementing the Disjoint Set (Union-Find) data structure for
    // Kruskal's algorithm
    class DisjointSet {
        private final int[] parent, rank; // Arrays to hold parent links and ranks for union by rank

        // Constructor: Initializes the disjoint set with 'n' elements
        public DisjointSet(int n) {
            parent = new int[n]; // Initialize parent array
            rank = new int[n]; // Initialize rank array
            for (int i = 0; i < n; i++) { // Loop through each element
                parent[i] = i; // Initially, each element is its own parent
                rank[i] = 0; // Rank is initialized to 0 for all elements
            }
        }

        // Method to find the representative (root) of a set that element 'i' belongs to
        public int find(int i) {
            if (parent[i] != i) { // If 'i' is not its own parent, then it's not the root
                parent[i] = find(parent[i]); // Recursively find the root and apply path compression
            }
            return parent[i]; // Return the root of the set
        }

        // Method to union the sets containing elements 'i' and 'j'
        public void union(int i, int j) {
            int rootI = find(i); // Find the root of element 'i'
            int rootJ = find(j); // Find the root of element 'j'
            if (rootI != rootJ) { // If the roots are different, union the sets
                if (rank[rootI] > rank[rootJ]) { // Attach the tree with the smaller rank under the one with larger rank
                    parent[rootJ] = rootI;
                } else if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++; // Increase the rank if both have the same rank
                }
            }
        }
    }

    // Custom JPanel class for visualizing the network graph (nodes and edges)
    class NetworkGraphPanel extends JPanel {
        private List<Node> displayedNodes = new ArrayList<>(); // List to hold nodes to be displayed
        private List<Edge> displayedEdges = new ArrayList<>(); // List to hold edges to be displayed

        // Method to add a node to the display list and repaint the panel
        public void addNode(Node node) {
            displayedNodes.add(node);
            repaint(); // Repaint the panel to show the new node
        }

        // Method to add an edge to the display list and repaint the panel
        public void addEdge(Edge edge) {
            displayedEdges.add(edge);
            repaint(); // Repaint the panel to show the new edge
        }

        // Overridden paintComponent method to draw the network graph
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Call the superclass method to ensure proper painting
            // Draw each edge from the displayedEdges list
            for (Edge edge : displayedEdges) {
                g.drawLine(edge.node1.x, edge.node1.y, edge.node2.x, edge.node2.y);
            }
            // Draw each node from the displayedNodes list
            for (Node node : displayedNodes) {
                g.fillOval(node.x - 10, node.y - 10, 20, 20); // Draw a circle representing the node
                g.drawString(node.name, node.x + 15, node.y); // Draw the node's name next to it
            }
        }
    }
}

/******************************************************************************************
 * Testing Results:
 * 
 * 1. Adding Nodes:
 * - When the "Add Node" button was clicked, a prompt appeared requesting the
 * node name.
 * - Upon entering a valid name, a node was added at a random position on the
 * graph panel.
 * - Multiple nodes were successfully displayed with their respective names.
 * 
 * 2. Adding Edges:
 * - When the "Add Edge" button was clicked, prompts appeared for "from node"
 * and "to node" names,
 * along with cost and bandwidth.
 * - Valid inputs resulted in the correct drawing of an edge between the
 * specified nodes.
 * - The edge displayed its cost and bandwidth in the output when optimized.
 * 
 * 3. Shortest Path Calculation:
 * - Using Dijkstra's algorithm, the "Calculate Shortest Path" button computed
 * the shortest path
 * based on bandwidth between two specified nodes.
 * - The resulting path was displayed correctly in the output area.
 * 
 * 4. Network Optimization (MST):
 * - Kruskal's algorithm successfully computed the Minimum Spanning Tree (MST)
 * when the
 * "Optimize Network" button was clicked.
 * - The optimized network (MST) was displayed in the output area with details
 * of each edge,
 * including cost and bandwidth.
 * 
 * All functionalities worked as expected without any modifications to the core
 * code.
 ******************************************************************************************/