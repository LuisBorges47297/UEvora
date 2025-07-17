package me.evora.servidor;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
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
import me.evora.repositorio.RepositorioArtista;
import me.evora.repositorio.RepositorioAtuacao;
import me.evora.repositorio.RepositorioClassificacao;
import me.evora.repositorio.RepositorioDonativo;
import me.evora.repositorio.RepositorioLocalizacao;
import me.evora.repositorio.RepositorioTipoDeArte;
import me.evora.repositorio.RepositorioUtilizador;
import me.evora.servicos.ServicoClassificacao;
import me.evora.servicos.ServicosArtista;
import me.evora.servicos.ServicosAtuacao;
import me.evora.servicos.ServicosDeImpressao;
import me.evora.servicos.ServicosDeUtilizador;
import me.evora.servicos.ServicosDonativo;
import me.evora.servicos.ServicosLocalizacao;
import me.evora.servicos.ServicosMensagens;
import me.evora.servicos.ServicosTipoDeArte;
import me.evora.servicos.erros.ErroArtistaJaEstaApurado;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;
import me.evora.servicos.erros.ErroEntidadeJaExiste;

public class AtendenteCliente implements Runnable {
    private ServicosAtuacao servicosAtuacao;
    private ServicosLocalizacao servicosLocalizacao;
    private ServicosArtista servicosArtista;
    private ServicosDonativo servicosDonativo;
    private ServicosTipoDeArte servicosTipoDeArte;
    private ServicosMensagens servicoDeMensagens;
    private ServicosDeImpressao servicosDeImpressao;
    private ServicosDeUtilizador servicosUtilizador;
    private ServicoClassificacao servicoClassificacao;
    private boolean estaAtenticado;

    public AtendenteCliente(int num, Socket conexão, Connection baseDeDados) throws IOException, SQLException {
        
        this.servicosDeImpressao = new ServicosDeImpressao(num, conexão.getInetAddress().toString());

        RepositorioArtista repositorioArtista = new RepositorioArtista(baseDeDados);
        RepositorioAtuacao repositorioAtuacao = new RepositorioAtuacao(baseDeDados);
        RepositorioLocalizacao repositorioLocalizacao = new RepositorioLocalizacao(baseDeDados);
        RepositorioTipoDeArte repositorioTipoDeArte = new RepositorioTipoDeArte(baseDeDados);
        RepositorioDonativo repositorioDonativo = new RepositorioDonativo(baseDeDados);
        RepositorioUtilizador repositorioUtilizador = new RepositorioUtilizador(baseDeDados);
        RepositorioClassificacao repositorioClassificacao = new RepositorioClassificacao(baseDeDados);

        this.servicosAtuacao = new ServicosAtuacao(repositorioArtista, repositorioAtuacao, repositorioLocalizacao);
        this.servicosLocalizacao = new ServicosLocalizacao(repositorioLocalizacao);
        this.servicosArtista = new ServicosArtista(repositorioArtista, repositorioAtuacao, repositorioTipoDeArte,
                repositorioLocalizacao, repositorioClassificacao);
        this.servicosDonativo = new ServicosDonativo(repositorioArtista, repositorioDonativo);
        this.servicosTipoDeArte = new ServicosTipoDeArte(repositorioTipoDeArte);
        this.servicosUtilizador = new ServicosDeUtilizador(repositorioUtilizador);
        this.servicoClassificacao = new ServicoClassificacao(repositorioArtista, repositorioClassificacao);

        this.servicoDeMensagens = new ServicosMensagens(conexão.getInputStream(), conexão.getOutputStream(),
                servicosLocalizacao, servicosTipoDeArte, servicosAtuacao, servicosArtista, servicosDonativo,
                servicosUtilizador);
    }

    @Override
    public void run() {
        servicosDeImpressao.imprimirMensagemDeConexao();
        servicosDeImpressao.imprimirMensagemResposta("CLIENTE CONECTADO COM SUCESSO");
        try {
            servicoDeMensagens.receberMensagemInteiro();
            int resposta = 0;
            do {
                resposta = atenderCliente();
            }while(resposta >= 0);
        } catch (IOException e) {
            servicosDeImpressao.imprimirMensagemResposta("ENCERRANDO A CONEXÃO COM");
        }
    }

    public int atenderCliente() throws IOException {
        int operacao = 0;
        do {
            operacao = servicoDeMensagens.receberMensagemInteiro();
            if(operacao == Constantes.AUTENTICAR_UTILIZADOR) {
                estaAtenticado = autenticarUtilizador();
            } else if(operacao == Constantes.REGISTAR_UTILIZADOR) {
                registarUtilizador();
            } else {
                servicosDeImpressao.imprimirMensagemResposta("SEM PERMISSÃO PARA REALIZAR A OPERAÇÃO, POR FAVOR AUTENTIQUE-SE");
                servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                servicoDeMensagens.enviarMensagemDeTexto("SEM PERMISSÃO PARA REALIZAR A OPERAÇÃO, POR FAVOR AUTENTIQUE-SE");
            }
        } while(!estaAtenticado);

        do {
            servicosDeImpressao.imprimirMensagemResposta("AGUARDANDO POR SOLCITAÇÕES");
            operacao = servicoDeMensagens.receberMensagemInteiro();
            if (operacao == Constantes.PEDIR_REGITAR_UM_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Registar artista.");
                try {
                    Object[] dadosDoArtista = servicoDeMensagens.receberDadosDeNovoArtista();
                    servicosArtista.registarNovoArtista((String) dadosDoArtista[0], (String) dadosDoArtista[1],
                            (String) dadosDoArtista[2], (Localizacao) dadosDoArtista[3],
                            (TipoDeArte) dadosDoArtista[4]);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                    servicosDeImpressao.imprimirMensagemResposta("Artista registado com sucesso.");
                } catch (ErroDeEntidadeNaoEncontrada ex) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicosDeImpressao.imprimirMensagemResposta("Não foi possível registar o artista.");
                }
            } else if (operacao == Constantes.LISTAR_ARTISTAS_COM_OPCAO_FILTROS) {
                servicosDeImpressao.imprimirMensagemPedido("Listar artistas.");
                FiltrosDeArtistas filtrosDeArtistas = servicoDeMensagens.receberFiltros();
                List<Artista> listaDeArtistas = servicosArtista.listarArtistas(filtrosDeArtistas);
                servicoDeMensagens.enviarListaDeArtistas(listaDeArtistas);
                servicosDeImpressao.imprimirMensagemResposta("Lista de artistas enviada");
            } else if (operacao == Constantes.LISTAR_LOCALIZACOES_COM_ARTISTAS_A_ATUAR) {
                servicosDeImpressao.imprimirMensagemPedido("Listar localizações com artistas a atuar.");
                List<Localizacao> localizacoes = servicosLocalizacao.listarLocalizacoesComAtuacao();
                servicoDeMensagens.enviarListaDeLocalizacoes(localizacoes);
                servicosDeImpressao.imprimirMensagemResposta("Lista de localizações enviada");
            } else if (operacao == Constantes.LISTAR_DATAS_E_LOCALIZACÕES_ONDE_ARTISTA_ATUOU) {
                servicosDeImpressao.imprimirMensagemPedido("Listar datas e localizações onde um artista atuou.");
                try {
                    int artistaID = servicoDeMensagens.receberArtistaID();
                    List<Atuacao> atuacoes = servicosAtuacao.listarAtuacoesPassadasDoArtista(artistaID);
                    servicoDeMensagens.enviarListaDeAtuacoes(atuacoes);
                } catch (ErroDeEntidadeNaoEncontrada e) {
                    e.printStackTrace();
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.ENVIAR_UM_DONATIVO_A_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Enviar donativo a um artista.");
                try {
                    Object[] dados = servicoDeMensagens.receberDadosDonativo();
                    servicosDonativo.criarDonativo((int) dados[0], (Utilizador) dados[2], (double) dados[1]);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                    servicosDeImpressao.imprimirMensagemResposta("Donativo eviado com sucesso");
                } catch (Exception e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicosDeImpressao.imprimirMensagemResposta("Não foi possível enviar o donativo");
                }
            } else if (operacao == Constantes.LISTAR_DONATIVOS_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Listar donativos de um artista.");
                try {
                    int artistaID = servicoDeMensagens.receberArtistaID();
                    List<Donativo> donativos = servicosDonativo.listarDonativosDoArtista(artistaID);
                    servicoDeMensagens.enviarListaDeDonativos(donativos);
                    servicosDeImpressao.imprimirMensagemResposta("Donativos eviados com sucesso");
                } catch (ErroDeEntidadeNaoEncontrada e) {
                    e.printStackTrace();
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.LISTA_DE_TODAS_LOCALIZACOES) {
                servicosDeImpressao.imprimirMensagemPedido("Listar todas localizações.");
                List<Localizacao> listaDeLocalizacaos = servicosLocalizacao.listarTodasLocalizacoes();
                servicoDeMensagens.enviarListaDeLocalizacoes(listaDeLocalizacaos);
                servicosDeImpressao.imprimirMensagemResposta("Lista de todas localizações enviada. ");
            } else if (operacao == Constantes.LISTA_DE_TODOS_TIPOS_DE_ARTES) {
                servicosDeImpressao.imprimirMensagemPedido("Listar todos tipos de arte.");
                List<TipoDeArte> listaDeTiposDeArte = servicosTipoDeArte.listarTodosTiposDeArte();
                servicoDeMensagens.enviarListaDeTipoDeArtes(listaDeTiposDeArte);
                servicosDeImpressao.imprimirMensagemResposta("Lista de todos tipos de arte enviada");
            } else if (operacao == Constantes.DEFINIR_LOCAL_DE_ATUACAO_DE_UM_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Definir local de atuação de um artista");
                int artistaID = servicoDeMensagens.receberArtistaID();
                try {
                    Atuacao atuacao = servicoDeMensagens.receberDadosDeAtuacao();
                    servicosArtista.definirLocalDeAtuacaoDoArtista(artistaID, atuacao.getLocalizacao(),
                            atuacao.getDataDeAtuacao());
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                    servicosDeImpressao.imprimirMensagemResposta("Atuação definida com sucesso");
                } catch (IOException | ErroDeEntidadeNaoEncontrada e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.LISTA_DATA_E_LOCALIZACAO_DA_PROXIMA_ATUACAO) {
                servicosDeImpressao.imprimirMensagemPedido("Listar data e localização da proxima atuacão do artista.");
                try {
                    int artistaID = servicoDeMensagens.receberArtistaID();
                    Atuacao atuacao = servicosAtuacao.listarProximaAtuacao(artistaID);
                    servicoDeMensagens.enviarAtuacao(atuacao);
                } catch (ErroDeEntidadeNaoEncontrada e) {
                    e.printStackTrace();
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.DAR_CLASSIFICACAO_A_UM_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Dar classificacao a um artista.");
                try {
                    Object[] dados = servicoDeMensagens.receberDadosClassificacao();
                    servicoClassificacao.criarClassificacao((int) dados[0], (Utilizador) dados[1], (double) dados[2], (String) dados[3]);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                    servicosDeImpressao.imprimirMensagemResposta("Classificação dada com sucesso");
                } catch (Exception e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicosDeImpressao.imprimirMensagemResposta(e.getMessage());
                }
            } else if (operacao == Constantes.LISTAR_ARTISTAS_POR_ESTADO) {
                servicosDeImpressao.imprimirMensagemPedido("Listar artistas por estado");
                int estado = servicoDeMensagens.receberMensagemInteiro();
                List<Artista> artistas = servicosArtista.listarArtistasPorEstado(estado == 1);
                servicoDeMensagens.enviarListaDeArtistas(artistas);
            } else if (operacao == Constantes.APROVAR_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Aprovar artista");
                int artistaID = servicoDeMensagens.receberArtistaID();
                try {
                    servicosArtista.apurarArtista(artistaID);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                } catch (ErroArtistaJaEstaApurado | ErroDeEntidadeNaoEncontrada e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.EDITAR_ARTISTA) {
                servicosDeImpressao.imprimirMensagemPedido("Editar artista");
                int artistaID = servicoDeMensagens.receberArtistaID();
                try {
                    Object[] dadosDoArtista = servicoDeMensagens.receberDadosDeNovoArtista();
                    servicosArtista.editarArtista(artistaID, (String) dadosDoArtista[0], (String) dadosDoArtista[1],
                            (String) dadosDoArtista[2], (Localizacao) dadosDoArtista[3],
                            (TipoDeArte) dadosDoArtista[4]);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                } catch (ErroDeEntidadeNaoEncontrada e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
            } else if (operacao == Constantes.LISTAR_ARTISTAS_COM_OPCAO_FILTROS) {
                servicosDeImpressao.imprimirMensagemPedido("Listar artistas.");
                FiltrosDeArtistas filtrosDeArtistas = servicoDeMensagens.receberFiltros();
                List<Artista> listaDeArtistas = servicosArtista.listarArtistas(filtrosDeArtistas);
                servicoDeMensagens.enviarListaDeArtistas(listaDeArtistas);
                servicosDeImpressao.imprimirMensagemResposta("Lista de artistas enviada");
            } else if (operacao == Constantes.LISTA_DE_TODAS_LOCALIZACOES) {
                servicosDeImpressao.imprimirMensagemPedido("Listar todas localizações.");
                List<Localizacao> listaDeLocalizacaos = servicosLocalizacao.listarTodasLocalizacoes();
                servicoDeMensagens.enviarListaDeLocalizacoes(listaDeLocalizacaos);
                servicosDeImpressao.imprimirMensagemResposta("Lista de todas localizações enviada. ");
            } else if (operacao == Constantes.LISTA_DE_TODOS_TIPOS_DE_ARTES) {
                servicosDeImpressao.imprimirMensagemPedido("Listar todos tipos de arte.");
                List<TipoDeArte> listaDeTiposDeArte = servicosTipoDeArte.listarTodosTiposDeArte();
                servicoDeMensagens.enviarListaDeTipoDeArtes(listaDeTiposDeArte);
                servicosDeImpressao.imprimirMensagemResposta("Lista de todos tipos de arte enviada");
            } else if (operacao == Constantes.PROMOVER_UTILIZADOR_A_ADMINISTRADOR) {
                servicosDeImpressao.imprimirMensagemPedido("Promover utilizador a administrador");
                int utilizadorID = servicoDeMensagens.receberMensagemInteiro();
                try {
                    servicosUtilizador.promoverUtilizadorAAdministrador(utilizadorID);
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
                } catch (ErroDeEntidadeNaoEncontrada e) {
                    servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
                    servicoDeMensagens.enviarMensagemDeTexto(e.getMessage());
                }
                servicosDeImpressao.imprimirMensagemResposta("Promoção de utilizador efetuada com sucesso");
            } else if (operacao == Constantes.LISTAR_UTILIZADORES_GERAIS) {
                servicosDeImpressao.imprimirMensagemPedido("Listar utilizadores gerais.");
                List<Utilizador> listaDeUtilizadores = servicosUtilizador.listarTodosUtilizadoresGerais();
                servicoDeMensagens.enviarListarDeUtilizadores(listaDeUtilizadores);
                servicosDeImpressao.imprimirMensagemResposta("Lista de todas localizações enviada. ");
            } else if (operacao == Constantes.TERMINAR_SESSAO) {
                servicosDeImpressao.imprimirMensagemPedido("Terminar sessão.");
                operacao = 0;
                servicosDeImpressao.imprimirMensagemResposta("Sessão terminada com sucesso");
            }else if (operacao == Constantes.TERMINAR_CONEXÃO) {
                servicosDeImpressao.imprimirMensagemPedido("Terminar conexão.");
                operacao = -1;
                servicosDeImpressao.imprimirMensagemResposta("Conexão terminada com sucesso");
            } else {
                servicosDeImpressao.imprimirMensagemPedido("PEDIDO INVÁLIDO");
            }
        } while (operacao > 0);
        return operacao;
    }

    public void registarUtilizador() throws IOException {
        servicosDeImpressao.imprimirMensagemPedido("Registar utilizador");
        try {
            Object[] dadosDeUtilizador = servicoDeMensagens.receberDadosDeNovoUtilizador();
            servicosUtilizador.registarUtilizador((String) dadosDeUtilizador[0], (String) dadosDeUtilizador[1],
                    (String) dadosDeUtilizador[2], (TipoDeUtilizador) dadosDeUtilizador[3]);
            servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
            servicosDeImpressao.imprimirMensagemResposta("Registo da conta efetuado com sucesso.");
        } catch (ErroDeEntidadeNaoEncontrada | ErroEntidadeJaExiste e) {
            servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
            servicosDeImpressao.imprimirMensagemResposta("Não foi possível registar o utilizador.");
        }
    }

    public boolean autenticarUtilizador() throws IOException {
        servicosDeImpressao.imprimirMensagemPedido("Autenticar utilizador");
        Object[] dadosDeAutenticacao = servicoDeMensagens.receberDadosDeAutenticacao();
        try {
            Utilizador utilizador = servicosUtilizador.autenticarUtilizador((String) dadosDeAutenticacao[0], 
                (String) dadosDeAutenticacao[1]);
            servicoDeMensagens.enviarMensagemInteiro(Constantes.SUCESSO);
            servicoDeMensagens.enviarUtilizador(utilizador);
            servicosDeImpressao.imprimirMensagemResposta("Autenticação efetuada com sucesso.");
            return true;
        } catch (ErroDeEntidadeNaoEncontrada e) {
            servicoDeMensagens.enviarMensagemInteiro(Constantes.ERRO);
            servicosDeImpressao.imprimirMensagemResposta(e.getMessage());
        }
        return false;
    }
}
