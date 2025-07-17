import java.util.LinkedList;

public class Grafo{
    Vertice[] verticesGrafo;

    // construtores
    public Grafo(Vertice[] verticesGrafo){
        this.verticesGrafo = verticesGrafo;
    }

    public Grafo(int numeroVertices){
        this.verticesGrafo = new Vertice[numeroVertices];
    }
}

class Vertice{
    String color;
    int distance;
    Vertice precedente;
    LinkedList<Vertice> verticesAdjacentes;

    // construtores
    public Vertice(String color, int distance, Vertice precedente){
        this.color = color;
        this.distance = distance;
        this.precedente = precedente;
    }

    public Vertice(String color, int distance, Vertice precedente, LinkedList<Vertice> verticesAdjacentes){
        this.color = color;
        this.distance = distance;
        this.precedente = precedente;
        this.verticesAdjacentes = verticesAdjacentes;
    }

    public Vertice(){
        this("None", -1, null);
        this.verticesAdjacentes = new LinkedList<>();
    }

    public Vertice(LinkedList<Vertice> verticesAdjacentes){
        this("None", -1, null, verticesAdjacentes);
    }

    // getters and setters
    public String color(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public int distance(){
        return distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public Vertice precedente(){
        return precedente;
    }

    public void setPrecedente(Vertice precedente){
        this.precedente = precedente;
    }

    public LinkedList<Vertice> verticesAdjacentes(){
        return verticesAdjacentes;
    }

    public void setVerticesAdjacentes(LinkedList<Vertice> verticesAdjacentes){
        this.verticesAdjacentes = verticesAdjacentes;
    }
}
