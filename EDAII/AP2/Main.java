import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    // method to print the result quantia[i]: [nMoeda]
    public static void printResult(int quantia, int rs){
        System.out.println(quantia + ": [" + rs + "]");
    }

    // method to do the exchange iteratively with memoization
    public static int change(int[] moedas, int quantia) {
        int[] tabela = new int[quantia + 1];
        for(int i = 1; i <= quantia; i++){
            tabela[i] = Integer.MAX_VALUE;
        }
        
        for(int i = 1; i <= quantia; i++){
            for(int j = 0; j < moedas.length; j++){
                if(i - moedas[j] >= 0 && tabela[i - moedas[j]] != Integer.MAX_VALUE){
                    tabela[i] = Math.min(tabela[i], tabela[i - moedas[j]] + 1);
                }
            }
        }
        return tabela[quantia];
    }

    public static void main(String[] args) throws Exception {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        int tiposMoedas = Integer.parseInt(input.readLine());

        String[] moedas = input.readLine().split(" "); 
    
        int[] newMoedas = new int[tiposMoedas];

        for(int i = 0; i < tiposMoedas; i++){
            newMoedas[i] = Integer.parseInt(moedas[i]);
        }
    
        int perguntas = Integer.parseInt(input.readLine());

        for(int i = perguntas; i > 0; i--){
            int quantia = Integer.parseInt(input.readLine());
            int resultado = change(newMoedas, quantia);
            printResult(quantia, resultado);
        }
        input.close();
    }

}