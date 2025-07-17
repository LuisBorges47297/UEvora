package me.evora.app;

import me.evora.servidor.Servidor;


public class App
{
    public static void main( String[] args )
    {
        Servidor servidor = new Servidor();
        servidor.iniciarServidor();
    }
}
