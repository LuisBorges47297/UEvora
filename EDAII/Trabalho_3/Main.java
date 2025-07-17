import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Location {
    int index;
    int energy;

    public Location(int index, int energy) {
        this.index = index;
        this.energy = energy;
    }
}

class Graph {
    int numLocations;
    List<List<Location>> adjacencyList;
    List<List<Location>> reverseAdjacencyList;

    public Graph(int numLocations) {
        this.numLocations = numLocations;
        this.adjacencyList = new ArrayList<>(numLocations);
        this.reverseAdjacencyList = new ArrayList<>(numLocations);
        for (int i = 0; i < numLocations; i++) {
            adjacencyList.add(new ArrayList<>());
            reverseAdjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int strength) {
        adjacencyList.get(from).add(new Location(to, strength));
        reverseAdjacencyList.get(to).add(new Location(from, strength));
    }

    public void printAdjacencyLists() {
        System.out.println("Adjacency List:");
        for (int i = 0; i < numLocations; i++) {
            System.out.print("From " + i + ": ");
            for (Location loc : adjacencyList.get(i)) {
                System.out.print("(" + loc.index + ", " + loc.energy + ") ");
            }
            System.out.println();
        }
        System.out.println("Reverse Adjacency List:");
        for (int i = 0; i < numLocations; i++) {
            System.out.print("From " + i + ": ");
            for (Location loc : reverseAdjacencyList.get(i)) {
                System.out.print("(" + loc.index + ", " + loc.energy + ") ");
            }
            System.out.println();
        }
    }
}

public class Main {

    public static int dijkstra(Graph graph, int start, int end) {
        int[] dist = new int[graph.numLocations];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        PriorityQueue<Location> pq = new PriorityQueue<>(Comparator.comparingInt(loc -> loc.energy));
        pq.add(new Location(start, 0));

        while (!pq.isEmpty()) {
            Location current = pq.poll();

            if (current.index == end) {
                return current.energy;
            }

            if (current.energy > dist[current.index]) {
                continue;
            }

            // Processar arestas na direção do movimento (custo 0)
            for (Location neighbor : graph.adjacencyList.get(current.index)) {
                if (dist[current.index] < dist[neighbor.index]) {
                    dist[neighbor.index] = dist[current.index];
                    pq.add(new Location(neighbor.index, dist[neighbor.index]));
                }
            }

            // Processar arestas contra a direção do movimento (custo igual à força da corrente)
            for (Location neighbor : graph.reverseAdjacencyList.get(current.index)) {
                int newDist = dist[current.index] + neighbor.energy;
                if (newDist < dist[neighbor.index]) {
                    dist[neighbor.index] = newDist;
                    pq.add(new Location(neighbor.index, newDist));
                }
            }
        }

        return dist[end];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] input = br.readLine().split(" ");
        int L = Integer.parseInt(input[0]); // Número de localizações
        int C = Integer.parseInt(input[1]); // Número de conexões
        int J = Integer.parseInt(input[2]); // Número de jornadas

        Graph graph = new Graph(L);

        // Adicionar arestas ao grafo
        for (int i = 0; i < C; i++) {
            input = br.readLine().split(" ");
            int l1 = Integer.parseInt(input[0]);
            int l2 = Integer.parseInt(input[1]);
            int s = Integer.parseInt(input[2]);
            graph.addEdge(l1, l2, s);
        }

        // Processar cada jornada
        for (int j = 0; j < J; j++) {
            input = br.readLine().split(" ");
            int dj = Integer.parseInt(input[0]); // Localização de partida
            int aj = Integer.parseInt(input[1]); // Localização de chegada

            int resultado = dijkstra(graph, dj, aj);
            System.out.println(resultado);
        }

        //graph.printAdjacencyLists();
    }
}
