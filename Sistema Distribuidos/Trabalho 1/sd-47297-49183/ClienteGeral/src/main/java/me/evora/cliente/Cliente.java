package main.java.me.evora.cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import main.java.me.evora.servicos.ServicosDeImpressao;
import main.java.me.evora.servicos.ServicosMensagens;

public class Cliente {
    private final String SERVER_HOSTNAME = "localhost";
    private Socket cliente;
    private BufferedReader teclado;
    private ServicosDeImpressao servicosDeImpressao;
    private ServicosMensagens servicosMensagens;

      public Cliente() {
        teclado = new BufferedReader( new InputStreamReader(System.in) );
        this.servicosDeImpressao = new ServicosDeImpressao(teclado);
    }

    
}
