package me.evora.servicos;

public class ServicosDeImpressao {
    private String idCliente;
    private int num;

    public ServicosDeImpressao(int num, String idCliente) {
        this.idCliente = idCliente;
        this.num = num;
    }

    public void imprimirMensagemDeConexao() {
        System.out.println("> Conexão com o cliente de ID " + idCliente + ":" + num + " estabelecida com sucesso");
    }

    public void imprimirMensagemPedido(String mensagem) {
        System.out.println( "< Solicitação de " + idCliente + ": " + mensagem);
    }

    public void imprimirMensagemResposta(String mensagem) {
        System.out.println( "> " + mensagem);
    }
}
