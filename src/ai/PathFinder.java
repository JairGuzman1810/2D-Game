package ai;

import main.GamePanel;

import java.util.ArrayList;

// PathFinder class for A* pathfinding in a 2D grid-based game
// Handles node initialization, cost calculation, and pathfinding logic
public class PathFinder {

    GamePanel gp; // Reference to the GamePanel for accessing game data
    Node[][] node; // 2D array representing the grid of nodes
    ArrayList<Node> openList = new ArrayList<>(); // List of nodes to explore
    public ArrayList<Node> pathList = new ArrayList<>(); // Final path from start to goal
    Node startNode, goalNode, currentNode; // Start, goal, and current nodes
    boolean goalReached = false; // Flag indicating if the goal has been reached
    int step = 0; // Step counter to limit iterations

    // Constructor to initialize the PathFinder with a reference to the game panel
    public PathFinder(GamePanel gp) {
        this.gp = gp;
        initNodes(); // Initialize all nodes in the grid
    }

    // Initialize the nodes in the grid
    public void initNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        // Loop through the grid to create nodes
        while (col < gp.maxWorldRow && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;

            // Move to the next row when the current column reaches the limit
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    // Reset the state of all nodes in the grid
    public void resetNodes() {
        int col = 0;
        int row = 0;

        // Loop through all nodes to reset their properties
        while (col < gp.maxWorldRow && row < gp.maxWorldRow) {

            // Reset open, checked, and solid states
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;

            // Move to the next row when the current column reaches the limit
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Clear the open list, path list, and reset flags
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    // Set the start and goal nodes, and configure the grid for pathfinding
    public void setNodes(int startCol, int starRow, int goalCol, int goalRow) {

        resetNodes(); // Reset the grid before setting new nodes

        // Define the start and goal nodes
        startNode = node[startCol][starRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];

        // Add the starting node to the open list
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        // Loop through all nodes to configure their properties
        while (col < gp.maxWorldRow && row < gp.maxWorldRow) {

            // Mark solid nodes based on tile collision data
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if (gp.tileM.tiles[tileNum].collision) {
                node[col][row].solid = true;
            }

            // Mark interactive tiles as solid
            for (int i = 0; i < gp.iTile[1].length; i++) {
                if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
                    node[itCol][itRow].solid = true;
                }
            }

            // Calculate the cost values for the node
            getCost(node[col][row]);

            col++;

            // Move to the next row when the current column reaches the limit
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    // Calculate the cost values (G, H, and F) for a node
    public void getCost(Node node) {

        // Calculate G cost (distance from start)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // Calculate H cost (distance to goal)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // Calculate F cost (G + H)
        node.fCost = node.gCost + node.hCost;
    }

    // Perform the A* search algorithm to find a path
    public boolean search() {
        // Continue searching until the goal is reached or step limit is exceeded
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Mark the current node as checked
            currentNode.checked = true;
            openList.remove(currentNode);

            // Open neighboring nodes
            if (row - 1 >= 0) openNode(node[col][row - 1]); // Up
            if (col - 1 >= 0) openNode(node[col - 1][row]); // Left
            if (row + 1 < gp.maxWorldRow) openNode(node[col][row + 1]); // Down
            if (col + 1 < gp.maxWorldCol) openNode(node[col + 1][row]); // Right

            // Find the best node to continue
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {

                // Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // If F costs are equal, prioritize lower G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // If there are no more nodes to explore, end the search
            if (openList.isEmpty()) {
                break;
            }

            // Set the best node as the current node
            currentNode = openList.get(bestNodeIndex);

            // Check if the goal node has been reached
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath(); // Trace the path from goal to start
            }

            step++;
        }

        return goalReached; // Return whether the goal was reached
    }

    // Open a node and add it to the open list if it is valid
    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode; // Set the current node as the parent
            openList.add(node); // Add the node to the open list
        }
    }

    // Trace the path from the goal node to the start node
    public void trackThePath() {

        Node current = goalNode;

        // Backtrack from the goal to the start node
        while (current != startNode) {
            pathList.add(0, current); // Add the node to the path list
            current = current.parent; // Move to the parent node
        }
    }
}
