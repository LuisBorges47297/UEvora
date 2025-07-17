package me.evora.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private ServerSocket servidor;
    private ExecutorService executorDeServicos;
    private Connection connection;

    public Servidor() {
        this.executorDeServicos = Executors.newFixedThreadPool(999);
    }

    public void iniciarServidor() {
        try {
            Properties appProps = new Properties();
            appProps.load( getClass().getResourceAsStream("app.properties") );
            connection = DriverManager.getConnection(appProps.getProperty("URL"), appProps.getProperty("USER"), 
                appProps.getProperty("PASSWORD"));

            this.servidor = new ServerSocket(49183, 999);
            System.out.println("\n*************************************************************************");
            System.out.println("+ INICIANDO O SERVIDOR...                                               +");
            System.out.println("+                                                                       +");
            System.out.println("+            O SERVIDOR ESTÁ EM EXECUÇÃO PELA PORTA: 4918               +");
            System.out.println("+                                                                       +");
            System.out.println("+                                                 AGUARDANDO CONEXÕES   +");
            System.out.println("*************************************************************************\n");
            
            int counter = 1;
            while( true ) {
                this.aguardarEAtenderConexão(counter++, connection);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void aguardarEAtenderConexão(int num, Connection conexaoBaseDeDados) throws IOException, SQLException {
        Socket conexão = this.servidor.accept();
        AtendenteCliente atendente = new AtendenteCliente(num, conexão, conexaoBaseDeDados);
        this.executorDeServicos.execute(atendente); 
    }
}
