package me.evora.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import me.evora.contants.Constantes;
import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Donativo;
import me.evora.modelo.Localizacao;
import me.evora.modelo.TipoDeArte;
import me.evora.modelo.Utilizador;
import me.evora.modelo.dto.FiltrosDeArtistas;
import me.evora.servicos.ServicosDeImpressao;
import me.evora.servicos.ServicosMensagens;

public class Cliente {
    private String SERVER_HOSTNAME = "localhost";
    private int PORT = 49183;
    private Socket cliente;
    private BufferedReader teclado;
    private ServicosDeImpressao servicosDeImpressao;
    private ServicosMensagens servicosMensagens;
    private boolean estaAutenticado;
    private Utilizador dadosUtilizador;

    public Cliente() {
        teclado = new BufferedReader( new InputStreamReader(System.in) );
        this.servicosDeImpressao = new ServicosDeImpressao(teclado);
    }

    public Cliente(String endereco, int porta) {
        PORT = porta;
        SERVER_HOSTNAME = endereco;
        teclado = new BufferedReader( new InputStreamReader(System.in) );
        this.servicosDeImpressao = new ServicosDeImpressao(teclado);
    }

    public Cliente(String endereco) {
        SERVER_HOSTNAME = endereco;
        teclado = new BufferedReader( new InputStreamReader(System.in) );
        this.servicosDeImpressao = new ServicosDeImpressao(teclado);
    }

    public void iniciarCliente() {
        try {
            this.cliente = new Socket( InetAddress.getByName(SERVER_HOSTNAME), PORT);
            this.servicosMensagens = new ServicosMensagens(cliente.getInputStream(), cliente.getOutputStream());
            this.servicosMensagens.enviarMensagemInteiro( Constantes.UTILIZADOR_CLIENTE );

            servicosDeImpressao.impirmirAbertura();

            int exterResposta = 1;
            do {
                exterResposta = servicosDeImpressao.menuInicial();
                switch (exterResposta) {
                    case 1:
                        autenticar();
                        break;
                    case 2:
                        registarUtilizador();
                        break;
                    default:
                        break;
                }
                if(exterResposta == 1 && estaAutenticado) {
                    int resposta = 0;
                    if(dadosUtilizador.getTipo() == TipoDeUtilizador.UTILIZADOR_GERAL) {
                        resposta = clienteGeral();
                    } else {
                        resposta = administrador();
                    }
                    if(resposta == 0) {
                        exterResposta = 0;
                    }
                }
                servicosDeImpressao.imprimirNovaLinha();
            } while(exterResposta > 0);

            servicosMensagens.enviarMensagemInteiro(Constantes.TERMINAR_CONEXÃO);
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("ENECERRANDO...\n");
        } catch (IOException e) {
            servicosDeImpressao.impirmirFalha();
        }
    }

    private int clienteGeral() throws IOException {
        int resposta = 0;
        do {
            resposta = servicosDeImpressao.menuPrincipalClienteGeral();
            switch(resposta) {
                case 1:
                    registarArtista();
                    break;
                case 2:
                    listarArtistas();
                    break;
                case 3:
                    definirAtuacaoDeUmArtista();
                    break;
                case 4:
                    pesquisarArtistaPorArteOuLocalizacao();
                    break;
                case 5:
                    listarLocalizacoesComArtistasAAtuar();
                    break;
                case 6:
                    listarAtuacoesDeUmArtista();
                    break;
                case 7:
                    listarProximaAtuacao();
                    break;
                case 8:
                    enviarDonativoAUmArtista();
                    break;
                case 9:
                    listarDonativosRecebidosPorUmArtista();
                    break;
                case 10:
                    darClassificacaoAumArtista();
                    break;
            }
        } while(resposta > 0 && resposta < 11);
        if(resposta == 11) {
            servicosMensagens.enviarMensagemInteiro(Constantes.TERMINAR_SESSAO);
        }
        return resposta;
    }

    private int administrador() throws IOException {
        int resposta = 0;
        do {
            resposta = servicosDeImpressao.menuPrincipalAdministrador();
            switch(resposta) {
                case 1:
                    registarArtista();
                    break;
                case 2:
                    listarArtistas();
                    break;
                case 3:
                    definirAtuacaoDeUmArtista();
                    break;
                case 4:
                    pesquisarArtistaPorArteOuLocalizacao();
                    break;
                case 5:
                    listarLocalizacoesComArtistasAAtuar();
                    break;
                case 6:
                    listarAtuacoesDeUmArtista();
                    break;
                case 7:
                    listarProximaAtuacao();
                    break;
                case 8:
                    enviarDonativoAUmArtista();
                    break;
                case 9:
                    listarDonativosRecebidosPorUmArtista();
                    break;
                case 10:
                    darClassificacaoAumArtista();
                    break;
                case 11:
                    listarArtistasAprovados();
                    break;
                case 12:
                    listarArtistasNaoAprovados();
                    break;
                case 13:
                    aprovarUmArtista();
                    break;
                case 14:
                    editarArtista();
                    break;
                case 15:
                    promoverUmUtilizadorAAdmin();
            }
        } while(resposta > 0 && resposta < 16);
        if(resposta == 16) {
            servicosMensagens.enviarMensagemInteiro(Constantes.TERMINAR_SESSAO);
        }
        return resposta;
    }

    private void autenticar() {
        Object[] dados = servicosDeImpressao.solicitarDadosDeAutenticacao();
        try {
            servicosMensagens.enviarDadosDeAutenticacao((String) dados[0], (String) dados[1]);
            int resposta = servicosMensagens.receberMensagemInteiro();
            if(resposta == Constantes.SUCESSO) {
                this.dadosUtilizador = servicosMensagens.reberUtilizador();
                servicosDeImpressao.imprimirMensagem("** AUTENTICAÇÃO EFETUADA COM SUCESSO**\n");
                estaAutenticado = true;
            } else {
                servicosDeImpressao.imprimirMensagem("** USERNAME OU PASSWORD INVÁLIDO **\n");
            }
        } catch(Exception e) {
            servicosDeImpressao.impirmirFalha();
        }
    }

    private void registarUtilizador() throws IOException {
        Object[] dados = servicosDeImpressao.solicitarDadosDeRegistoDeUtilizador();
        servicosMensagens.enviarDadosDeUmUtilizador((String) dados[0], (String) dados[1], (String) dados[2], (String) dados[3]);
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("** REGISTO DE ARTISTA EFETUADO COM SUCESSO **\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO REGISTAR O UTILIZADOR **\n");
        }
    }

    private void registarArtista() throws IOException {
        List<Localizacao> localizacoes = servicosMensagens.receberListaDeLocalizacoes();
        List<TipoDeArte> tipoDeArtes = servicosMensagens.receberListaDeTiposDeArte();
        Object[] dados = servicosDeImpressao.solicitarDadosDeRegistoDeUmArtista(localizacoes, tipoDeArtes);
        servicosMensagens.enviarDadosDeUmNovoArtista((String) dados[0], (String) dados[1], (String) dados[2], 
                (Localizacao) dados[3], (TipoDeArte) dados[4]);
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("** PEDIDO DE REGISTO DE ARTISTA ENVIADO COM SUCESSO **\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO REGISTAR O ARTISTA **\n");
        }
    }

    public void listarArtistas() throws IOException {
        FiltrosDeArtistas filtros = new FiltrosDeArtistas();
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(filtros);
        servicosDeImpressao.imprimirArtistas(artistas);
    }

    public void definirAtuacaoDeUmArtista() throws IOException {
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object[] dados = servicosDeImpressao.definirAtuacaoDeUmArtista(artistas);
        if(dados != null) {
            servicosMensagens.enviarDadosAtuacao(artistas.get((int) dados[0]).getId(), (int) dados[1], (int) dados[2], (Date) dados[3]);
            int reposta = servicosMensagens.receberMensagemInteiro();
            if(reposta == Constantes.SUCESSO) {
                servicosDeImpressao.imprimirMensagem("** ATUAÇÃO REGISTADA COM SUCESSO. **\n");
            }
        } else {
            servicosDeImpressao.imprimirMensagem("** OPERAÇÃO DE DEFINIÇÃO DE ATUAÇÃO CANCELADA **\n");
        }
    }

    public void pesquisarArtistaPorArteOuLocalizacao() throws IOException {
        List<Localizacao> localizacoes = servicosMensagens.receberListaDeLocalizacoes();
        List<TipoDeArte> tipoDeArtes = servicosMensagens.receberListaDeTiposDeArte();
        FiltrosDeArtistas filtros = servicosDeImpressao.definirFiltros(localizacoes, tipoDeArtes);
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(filtros);
        servicosDeImpressao.imprimirArtistas(artistas);
    }

    public void listarLocalizacoesComArtistasAAtuar() throws IOException {
        List<Localizacao> localizacoes = servicosMensagens.receberListaDeLocalizacoes(Constantes.LISTAR_LOCALIZACOES_COM_ARTISTAS_A_ATUAR);
        servicosDeImpressao.imprimirLocalizacoesComArtistasAAtuar(localizacoes);
    }

    public void listarAtuacoesDeUmArtista() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("LISTA DE DATAS E LOCALIZAÇÕES ONDE UM ARTISTA JÁ ATUOU\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a visualizar as datas e localização das atuacoes", 
            (List<Object>) object, 8, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        servicosMensagens.enviarMensagemInteiro(Constantes.LISTAR_DATAS_E_LOCALIZACÕES_ONDE_ARTISTA_ATUOU);
        servicosMensagens.enviarArtistaID(artistas.get(artistaId).getId());
        List<Atuacao> atuacoes = servicosMensagens.receberListaDeAtuacoes();
        servicosDeImpressao.imprimirAtuacoes(atuacoes, artistas.get(artistaId));
        servicosDeImpressao.imprimirNovaLinha();
    }

    public void listarProximaAtuacao() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("DATA E LOCALIZAÇÃO DA PROXIMA ATUAÇÃO DE UM ARTISTA\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a visualizar a datas e localização da proxima atuacão", 
            (List<Object>) object, 8, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        servicosMensagens.enviarMensagemInteiro(Constantes.LISTA_DATA_E_LOCALIZACAO_DA_PROXIMA_ATUACAO);
        servicosMensagens.enviarArtistaID(artistas.get(artistaId).getId());
        Atuacao atuacao = servicosMensagens.receberAtuacao();
        servicosDeImpressao.imprimirAtuacao(atuacao, artistas.get(artistaId));
        servicosDeImpressao.imprimirNovaLinha();
    }

    public void enviarDonativoAUmArtista() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("ENVIAR DONATIVO A UM ARTISTA\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a enviar donativo", 
            (List<Object>) object, 8, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        double valor = Double.parseDouble(servicosDeImpressao.lerTexto("+ Valor", 8));
        servicosMensagens.enviarMensagemInteiro(Constantes.ENVIAR_UM_DONATIVO_A_ARTISTA);
        servicosMensagens.enviarDonativo(new Donativo(valor, artistas.get(artistaId), dadosUtilizador));
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            System.out.println();
            servicosDeImpressao.imprimirMensagem("** DONATIVO ENVIADO COM SUCESSO **\n\n");
        }
    }

    public void listarDonativosRecebidosPorUmArtista() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("LISTA DE DONATIVOS DE UM ARTISTA\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a visualizar os donatvos", 
            (List<Object>) object, 4, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        servicosMensagens.enviarMensagemInteiro(Constantes.LISTAR_DONATIVOS_ARTISTA);
        servicosMensagens.enviarArtistaID(artistas.get(artistaId).getId());
        List<Donativo> atuacoes = servicosMensagens.receberListaDeDonativos();
        servicosDeImpressao.imprimirDonativos(atuacoes, artistas.get(artistaId));
        servicosDeImpressao.imprimirNovaLinha();
    }

    public void darClassificacaoAumArtista() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("DAR CLASSIFICAÇÃO A UM ARTISTA\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(new FiltrosDeArtistas());
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a dar classificação", 
            (List<Object>) object, 4, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        double valor = 0;
        do {
            valor = Double.parseDouble(servicosDeImpressao.lerTexto("+ Classificação(0-10)", 4));
        } while(valor < 0 || valor > 10);
        String comentario = servicosDeImpressao.lerTexto("+ Comentário", 4);
        servicosMensagens.enviarMensagemInteiro(Constantes.DAR_CLASSIFICACAO_A_UM_ARTISTA);
        servicosMensagens.enviarDadosClassificacao(artistas.get(artistaId).getId(), valor, comentario, dadosUtilizador.getId());
        int resultado = servicosMensagens.receberMensagemInteiro();
        System.out.println();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirMensagem("** CLASSIFICAÇÃO DADA COM SUCESSO **\n\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO DAR A CLASSIFICAÇÃO **\n\n");
        }
    }

    private void editarArtista() throws IOException {
        List<Localizacao> localizacoes = servicosMensagens.receberListaDeLocalizacoes();
        List<TipoDeArte> tipoDeArtes = servicosMensagens.receberListaDeTiposDeArte();
        FiltrosDeArtistas filtros = new FiltrosDeArtistas();
        List<Artista> artistas = servicosMensagens.receberListaDeArtistas(filtros);
        Object object = (Object) artistas;
        servicosDeImpressao.imprimirSeparador();
        System.out.println("\nEDIÇÃO DE UM ARTISTA\n");
         int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista que pretende editar", 
            (List<Object>) object, 4, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        Object[] dados = servicosDeImpressao.editarArtista(artistas.get(artistaId), localizacoes, tipoDeArtes);
        servicosMensagens.enviarNovoDadosDoArtista(artistas.get(artistaId).getId(), (String) dados[0], (String) dados[1], 
            (String) dados[2], (Localizacao) dados[3], (TipoDeArte) dados[4]);
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("** ARTISTA EDITADO COM SUCESSO **\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO EDITAR O ARTISTA **\n");
        }
    }

    public void listarArtistasAprovados() throws IOException {
        List<Artista> artistas = servicosMensagens.receberListaDeArtistasPorEstado(true);
        servicosDeImpressao.imprimirArtistas(artistas);
    }

     public void listarArtistasNaoAprovados() throws IOException {
        List<Artista> artistas = servicosMensagens.receberListaDeArtistasPorEstado(false);
        servicosDeImpressao.imprimirArtistas(artistas);
    }

    public void aprovarUmArtista() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("APROVAÇÃO DE UM ARTISTA\n");
        List<Artista> artistas = servicosMensagens.receberListaDeArtistasPorEstado(false);
        Object object = (Object) artistas;
        int artistaId = servicosDeImpressao.selecaoSemAdicao("- Selecione o artista a apurar", 
            (List<Object>) object, 4, "Cancelar");
        if(artistaId < 0) {
            return;
        }
        servicosMensagens.enviarMensagemInteiro(Constantes.APROVAR_ARTISTA);
        servicosMensagens.enviarArtistaID(artistas.get(artistaId).getId());
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("** APROVAÇÃO DO ARTISTA EFECTUADA COM SUCESSO **\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO APROVAR O ARTISTA **\n");
        }
    }

    public void promoverUmUtilizadorAAdmin() throws IOException {
        servicosDeImpressao.imprimirNovaLinha();
        servicosDeImpressao.imprimirSeparador();
        System.out.println("PROMOVER UM UTILIZADOR A ADMIN\n");
        List<Utilizador> utilizadores = servicosMensagens.receberListaDeUtilizadores();
        Object object = (Object) utilizadores;
        int utilizadorId = servicosDeImpressao.selecaoSemAdicao("- Selecione o utilizador a promover", 
            (List<Object>) object, 4, "Cancelar");
        if(utilizadorId < 0) {
            return;
        }
        servicosMensagens.enviarMensagemInteiro(Constantes.PROMOVER_UTILIZADOR_A_ADMINISTRADOR);
        servicosMensagens.enviarMensagemInteiro(utilizadores.get(utilizadorId).getId());
        int resultado = servicosMensagens.receberMensagemInteiro();
        if(resultado == Constantes.SUCESSO) {
            servicosDeImpressao.imprimirNovaLinha();
            servicosDeImpressao.imprimirMensagem("** PROMOÇÃO DO UTILIZADOR EFECTUADA COM SUCESSO **\n");
        } else {
            servicosDeImpressao.imprimirMensagem("** OCORREU UM ERRO AO PROMOVER O UTILIZADOR **\n");
        }
    }
}
