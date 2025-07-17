import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        int numberOfEmployees = Integer.parseInt(input.readLine());

        Grafo grafo = new Grafo(numberOfEmployees);

        for(int i = 0; i < numberOfEmployees; i++){
            String[] string = input.readLine().split(" "); 
            int numberOffriends = Integer.parseInt(string[0]);

            for(int j = 1; j <= numberOffriends; j++){
            
                int friends = Integer.parseInt(string[j]);                
                //System.out.println(friends);
            }
        //  verificar se grafo.verticesGrafo esta vazio
        if(grafo.verticesGrafo.length == 0){
            Vertice empregado = new Vertice();
            grafo.verticesGrafo[i] = empregado;
        }else{
            // verificar se o empregado já existe
            if(grafo.verticesGrafo[i] == null){
                Vertice empregado = new Vertice();
                grafo.verticesGrafo[i] = empregado;
            }

        }
        input.close();
    }

}