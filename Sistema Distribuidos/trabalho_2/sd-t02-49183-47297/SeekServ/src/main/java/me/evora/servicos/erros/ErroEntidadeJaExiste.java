package me.evora.servicos.erros;

public class ErroEntidadeJaExiste extends Exception {
    public ErroEntidadeJaExiste(String mensagem) {
        super(mensagem);
    }
}