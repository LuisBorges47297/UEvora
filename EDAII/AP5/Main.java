import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// grafo orientado com lista de adjacência
class Graph {
        
    int nodes; // número de nós
    List<Integer>[] adjacents; // listas de adjacência

    @SuppressWarnings("unchecked") // devido ao novo List[]
    public Graph(int nodes){
        this.nodes = nodes;
        adjacents = new List[nodes];
        for (int i = 0; i < nodes; ++i){
            adjacents[i] = new LinkedList<>();
        }
    }

    /* Adiciona a aresta (U,V) direcionada ao grafo. */
    public void addEdge(int u, int v){
        adjacents[u].add(v);
    }

    // imprime o grafo
    public void printGraph(){
        for(int i = 0; i < this.nodes; i++){
            // imprime os adjacents[i]
            System.out.println(adjacents[i].toString());
        }
    }
}

public class Main {

    // obtém a ordenação topológica usando Kahn
    public static List<Integer> topologicalSortKahn(Graph g) {
        // obtém o número de nós
        int n = g.nodes;
        // cria o array de grau de entrada
        int[] indegree = new int[n];
        // cria a fila
        Queue<Integer> q = new LinkedList<>();
        // cria a lista
        List<Integer> list = new ArrayList<>();

        // obtém o grau de entrada de cada nó
        for(int i = 0; i < n; i++){
            for(int j = 0; j < g.adjacents[i].size(); j++){
                indegree[g.adjacents[i].get(j)]++;
            }
        }

        // adiciona os nós com grau de entrada 0 à fila
        for(int i = 0; i < n; i++){
            if(indegree[i] == 0){
                q.add(i);
            }
        }

        // enquanto a fila não estiver vazia
        while(!q.isEmpty()){
            // obtém o nó
            int u = q.poll();
            // adiciona à lista
            list.add(u);
            // para cada adjacente de u
            for(int i = 0; i < g.adjacents[u].size(); i++){
                // obtém o adjacente
                int v = g.adjacents[u].get(i);
                // decrementa o grau de entrada de v
                indegree[v]--;
                // se o grau de entrada de v for 0, adiciona à fila
                if(indegree[v] == 0){
                    q.add(v);
                }
            }
        }

        // se o tamanho da lista não for igual ao número de nós, há um ciclo
        if(list.size() != n){
            return null;
        }

        // Retorna a lista, mas reorganiza-a para colocar os números mais baixos primeiro
        List<Integer> reorderedList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!list.contains(i)) {
                reorderedList.add(i);
            }
        }
        reorderedList.addAll(list);

        return reorderedList;
    }
    
    public static void main(String[] args) throws Exception{

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // número de tarefas e número de regras
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);

        // cria o grafo
        Graph g = new Graph(n);

        // lê as regras
        for(int i = 0; i < m; i++){
            // obtém a linha em um array
            line = br.readLine().split(" ");
            // obtém a tarefa e a regra (u -> v)
            int u = Integer.parseInt(line[0]);

            // número de regras
            int k = Integer.parseInt(line[1]);
            for(int j = 0; j < k; j++){
                int v = Integer.parseInt(line[j + 2]);
                g.addEdge(u - 1, v - 1);
            }

        }

        // obtém a ordenação topológica
        List<Integer> topologicalSort = topologicalSortKahn(g);

        // imprime a ordenação topológica
        for (Integer task : topologicalSort) {
            // Adiciona 1 para corresponder aos números de tarefa (que começam de 1)
            System.out.print((task + 1) + " ");
        }
        System.out.println();
        g.printGraph();
    }
 
}
