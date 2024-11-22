package ai;


// Represents a node in the grid for the A* pathfinding algorithm.
// Each node tracks its position, costs (g, h, f), and states
// (solid, open, checked) for the algorithm's traversal.
public class Node {
    Node parent;  // Reference to the parent node for path tracking
    public int col;  // Column position of the node
    public int row;  // Row position of the node
    int gCost;  // Cost from the start node to this node
    int hCost;  // Heuristic cost from this node to the goal node
    int fCost;  // Total cost (gCost + hCost)
    boolean solid;  // Indicates if the node is a solid (non-traversable) tile
    boolean open;  // Indicates if the node is currently in the open list
    boolean checked;  // Indicates if the node has already been processed

    // Constructor to initialize a node with its column and row position.
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}
