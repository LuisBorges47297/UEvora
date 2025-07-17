package me.evora.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.evora.cliente.Cliente;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args ) throws FileNotFoundException, IOException
    {
        Cliente cliente;

        if( args.length > 1){
             String endereco = args[0];
             if(args.length == 2){
                int porta = Integer.parseInt(args[1]);
                cliente = new Cliente(endereco, porta);
             }else{
                cliente = new Cliente(endereco);
             }
        }else{
            cliente = new Cliente();

        }

        cliente.iniciarCliente();
    }
}
