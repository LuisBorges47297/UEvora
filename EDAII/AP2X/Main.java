//package AP2X;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    // method to print the result quantia[i]: [nMoeda]
    public static void printResult(int quantia, int rs[]){
        // printArray(rs);

        System.out.print(quantia + ": [" + rs[quantia + 1] + "]");
        System.out.print(" " + rs[quantia]);

        int x = quantia - rs[quantia];
        for(int i = 1; i < rs[quantia + 1]; i++){
            System.out.print(" " + rs[x]);
            x = x - rs[x];

        }
        System.out.println();
    }

    // method to do the exchange recursively with memoization
    public static int[] change(int[] moedas, int quantia) {

        int[] m = new int[quantia + 1];
        int[] c = new int[quantia + 2];
        m[0] = 0;
        c[0] = 0;

        for(int q = 1; q <= quantia; q++){
            int min = Integer.MAX_VALUE;
            for(int i = 0; i < moedas.length; i++){
                if(moedas[i] <= q && (m[q - moedas[i]] + 1) < min){
                    min = m[q - moedas[i]] + 1;
                    c[q] = moedas[i];
                }
                m[q] = min;
            }
        }
        c[quantia + 1] = m[quantia];
        return c;
    }

    public static void main(String[] args) throws Exception {

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
    int tiposMoedas = Integer.parseInt(input.readLine());

    String [] moedas = input.readLine().split(" ");

    int[] newMoedass = new int[tiposMoedas];

    for(int i = 0; i < tiposMoedas; i++){
        newMoedass[i] = Integer.parseInt(moedas[i]);
    }

    int perguntas = Integer.parseInt(input.readLine());

    for(int i = perguntas; i > 0; i--){
        
        int quantia = Integer.parseInt(input.readLine());

        int[] resultado = change(newMoedass, quantia);

        printResult(quantia, resultado);

        //System.out.println(quantia);

    }
    input.close();
    //System.out.println(tiposMoedas);
    //System.out.println(perguntas);
    }
}
