import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

class Cell {
    int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell other = (Cell) obj;
        return x == other.x && y == other.y;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(x) ^ Integer.hashCode(y);
    }
}

class Graph {
    int nodes; // number of nodes
    ArrayList<Integer>[] adjacents; // adjacency lists
    Map<Cell, Integer> coordinatesMap;

    @SuppressWarnings("unchecked")
    public Graph(int nodes, Map<Cell, Integer> coordinatesMap) {
        this.nodes = nodes;
        this.coordinatesMap = coordinatesMap;
        adjacents = new ArrayList[nodes];

        for (int i = 0; i < nodes; ++i)
            adjacents[i] = new ArrayList<>();
    }

    /* Adds the (undirected) edge (U,V) to the graph. */
    public void addEdge(Cell u, Cell v) {
        int uIndex = getIndex(u);
        int vIndex = getIndex(v);
        if (uIndex != -1 && vIndex != -1) {
            adjacents[uIndex].add(vIndex);
            adjacents[vIndex].add(uIndex); // For an undirected graph, add the edge in both directions
        }
    }

    /* Helper method to get the index of a vertex in the adjacency list. */
    private int getIndex(Cell vertex) {
        return coordinatesMap.getOrDefault(vertex, -1);
    }

    public int minEdgesBetween(Cell start, Cell end) {
        // Initialize a queue for BFS and a visited array to keep track of visited nodes
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[nodes];
        int[] distance = new int[nodes];

        // Enqueue the start node and mark it as visited
        int startIndex = getIndex(start);
        queue.offer(startIndex);
        visited[startIndex] = true;
        distance[startIndex] = 0;

        // Perform BFS
        while (!queue.isEmpty()) {
            int current = queue.poll();
            // Check if the current node is the destination
            if (current == getIndex(end)) {
                return distance[current];
            }
            
            // Visit all adjacent nodes
            for (int neighbor : adjacents[current]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distance[neighbor] = distance[current] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        // If the destination node is not reachable, return -1
        return -1;
    }
}

public class Main {
    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int totalEdges = 0; // Variable to store the sum of minimum edges for all lines
        int vertex = 0;
        Cell current = new Cell(0, 0);
        //Cell first_vertex = current;
        // Read the number of lines
        int lines = Integer.parseInt(input.readLine());

        // Create a list to store coordinates and a map to store coordinate-index pairs
        //ArrayList<Cell> coordinatesList = new ArrayList<>();
        Map<Cell, Integer> coordinatesMap = new HashMap<>();

        Graph graph = null;

        StringBuilder concatenatedLines = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            // Read the movements for the current line
            String line = input.readLine();
            concatenatedLines.append(line);
        }

            // Agora você tem todas as linhas concatenadas em uma única string
            String[] movements = concatenatedLines.toString().split("");

            // Inicialize o array de células com o tamanho máximo
            Cell[] cells = new Cell[movements.length + 1];
            // Add the starting cell to the map and list
            coordinatesMap.put(current, vertex);
            //coordinatesList.add(current);

            // Adicione a célula inicial ao array
            cells[0] = current;

            // Add subsequent cells to the map and list
            for (int i = 0; i < movements.length; i++){ //(String movement : movements) {
                Cell next = getNextCell(current, movements[i]);
                if (!coordinatesMap.containsKey(next)) {
                    vertex++;
                    coordinatesMap.put(next, vertex);
                }
                cells[i+1] = next; // Adiciona a próxima célula ao array
                //coordinatesList.add(next);
                current = next;
            }

            // Create the graph with the number of vertices and coordinates map
            graph = new Graph(movements.length, coordinatesMap);

            // Build the graph by adding edges
            for (int j = 0; j < movements.length; j++) {
                graph.addEdge(cells[j], cells[j +1]);
            }

            // Calculate the sum of minimum edges for all lines
            Cell startCell = cells[0];
            Cell endCell = cells[cells.length - 1];
            totalEdges = graph.minEdgesBetween(startCell, endCell);

            // Print the total sum of minimum edges for all lines
            System.out.println(totalEdges);

        }
    private static Cell getNextCell(Cell current, String movement) {
        int x = current.x, y = current.y;
        switch (movement) {
            case "N":
                y++;
                break;
            case "E":
                x++;
                break;
            case "S":
                y--;
                break;
            case "W":
                x--;
                break;
        }
        return new Cell(x, y);
    }
}