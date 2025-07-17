import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainRecursivo {

    // method to print the result quantia[i]: [nMoeda]
    public static void printResult(int quantia, int rs){
        System.out.println(quantia + ": [" + rs + "]");
    }

    // method to do the exchange recursively with memoization
    public static int change(int[] moedas, int quantia) {

        int[] tabela = new int[quantia + 1];
        for(int i = 0; i < tabela.length; i++){
            tabela[i] = Integer.MAX_VALUE;
        }
        tabela[0] = 0;
        return changeRecursivo(moedas, quantia, tabela);
    }

    public static int changeRecursivo(int[] moedas, int quantia, int[] tabela){  
        if(tabela[quantia] == Integer.MAX_VALUE){
            
            int qtdMoedas = Integer.MAX_VALUE;
            for(int i = 0; i < moedas.length; i++){
                if(quantia - moedas[i] >= 0){
                    qtdMoedas = Math.min(qtdMoedas, changeRecursivo(moedas, quantia - moedas[i], tabela) + 1);     
                }
            }
            tabela[quantia] = qtdMoedas;
        }
        return tabela[quantia];
    }

    public static void main(String[] args) throws Exception {

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
    int tiposMoedas = Integer.parseInt(input.readLine());

    String [] moedas = input.readLine().split(" "); 
    
    int[] newMoedas = new int[tiposMoedas];

    for(int i = 0; i < tiposMoedas; i++){
        newMoedas[i] = Integer.parseInt(moedas[i]);
    }
    
    int perguntas = Integer.parseInt(input.readLine());

    for(int i = perguntas; i > 0; i--){
        
        int quantia = Integer.parseInt(input.readLine());

        int resultado = change(newMoedas, quantia);

        printResult(quantia, resultado);

        //System.out.println(quantia);

    }
    input.close();
    //System.out.println(tiposMoedas);
    //System.out.println(perguntas);
    }
}
