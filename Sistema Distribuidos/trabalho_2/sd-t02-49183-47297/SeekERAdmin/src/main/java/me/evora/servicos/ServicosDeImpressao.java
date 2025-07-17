package me.evora.servicos;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Donativo;
import me.evora.modelo.Localizacao;
import me.evora.modelo.TipoDeArte;
import me.evora.modelo.dto.FiltrosDeArtistas;

public class ServicosDeImpressao {
    private BufferedReader input;

    public ServicosDeImpressao(BufferedReader sc) {
        this.input = sc;
    }

    public void impirmirFalha() {
        System.out.println("\n*****************************************************************************");
        System.out.println("+                                                                           +");
        System.out.println("+            O SERVIDOR NÃO ESTÁ DISPONÍVEL PARA CONEXÃO                    +");
        System.out.println("+                                                                           +");
        System.out.println("******************************************************************************\n");
        System.out.println("> ENCERRANDO...\n");
    }

    public void imprimirNovaLinha() {
        System.out.println();
    }

    public void imprimirSeparador() {
        System.out.println("*****************************************************************************\n");
    }

    public void imprimirMensagem(String mensagem) {
        System.out.println("> " + mensagem);
    }

    public void impirmirAbertura() {
        System.out.println("\n*****************************************************************************");
        System.out.println("+                                                                           +");
        System.out.println("+            CONEXÃO COM O SERVIDOR ESTABELECIDA COM SUCESSO                +");
        System.out.println("+                       SEJA BEM VINDO AO TáNaForja                         +");
        System.out.println("+                                                                           +");
        System.out.println("+****************************************************************************");
        System.out.println("+                                                                           +");
        System.out.println("+  Esta aplicação permite se gerir artistas de rua, possiblitando que       +");
        System.out.println("+  pessoas tenham conhecimento de suas obras artisticas. Com esta aplicação +");
        System.out.println("+  pode-se localizar quando um artista está a atuar num espetáculo, o que   +");
        System.out.println("+  permite as pessoas deixarem suas classificações sobre o que acha         +");
        System.out.println("+                                                                           +");
    }

    public int menuInicial() {
        int opcao = 0;
        System.out.println("*****************************************************************************\n");
        System.out.println("ESCOLHA O QUE VOCÊ QUER FAZER");
        System.out.println();
        System.out.println("    1. Autenticar-me");
        System.out.println("    2. Criar conta");
        System.out.println("    0. Sair do sistema");
        System.out.println();
        do {
            opcao = lerInteiro("Resposta", 0);

            if (opcao < 0 || opcao > 2) {
                System.out.println(
                        "Resposta inválida, por favor insira um inteiro entre 0 e 7 para selecionar uma opção.\n");
            }
        } while (opcao < 0 || opcao > 2);

        return opcao;
    }

    public int menuPrincipalClienteGeral() {
        int opcao = 0;
        System.out.println("****************************************************************************\n");
        System.out.println("ESCOLHA O QUE VOCÊ QUER FAZER");
        System.out.println();
        System.out.println("    1. Enviar pedido de registo de um novo artista");
        System.out.println("    2. Listar artistas");
        System.out.println("    3. Definir data e localização de atuação de um artistas");
        System.out.println("    4. Pesquisar artista de acordo com a arte ou localização");
        System.out.println("    5. Listar localizações onde existem artistas a atuar");
        System.out.println("    6. Listar data e localizações onde um artista já atuou");
        System.out.println("    7. Listar data e localização da proxima atuação");
        System.out.println("    8. Enviar donativo a um artista");
        System.out.println("    9. Listar os donativos recebidos por um artista");
        System.out.println("    10. Dar classificação a um artista");
        System.out.println("    11. Terminar sessão");
        System.out.println("    0. Sair");
        System.out.println();
        do {
            opcao = lerInteiro("Resposta", 0);

            if (opcao < 0 || opcao > 11) {
                System.out.println(
                        "Resposta inválida, por favor insira um inteiro entre 0 e 11 para selecionar uma opção.\n");
            }
        } while (opcao < 0 || opcao > 11);

        return opcao;
    }

    public int menuPrincipalAdministrador() {
        int opcao = 0;
        System.out.println("****************************************************************************\n");
        System.out.println("ESCOLHA O QUE VOCÊ QUER FAZER");
        System.out.println();
        System.out.println("    1. Enviar pedido de registo de um novo artista");
        System.out.println("    2. Listar artistas");
        System.out.println("    3. Definir data e localização de atuação de um artistas");
        System.out.println("    4. Pesquisar artista de acordo com a arte ou localização");
        System.out.println("    5. Listar localizações onde existem artistas a atuar");
        System.out.println("    6. Listar data e localizações onde um artista já atuou");
        System.out.println("    7. Listar data e localização da proxima atuação");
        System.out.println("    8. Enviar donativo a um artista");
        System.out.println("    9. Listar os donativos recebidos por um artista");
        System.out.println("    10. Dar classificação a um artista");
        System.out.println("    11. Listar artistas aprovados");
        System.out.println("    12. Listar artistas não aprovados");
        System.out.println("    13. Aprovar artista");
        System.out.println("    14. Editar Artista");
        System.out.println("    15. Promover utilizador a administrador");
        System.out.println("    16. Terminar sessão");
        System.out.println("    0. Sair");
        System.out.println();
        do {
            opcao = lerInteiro("Resposta", 0);

            if (opcao < 0 || opcao > 16) {
                System.out.println(
                        "Resposta inválida, por favor insira um inteiro entre 0 e 15 para selecionar uma opção.\n");
            }
        } while (opcao < 0 || opcao > 16);

        return opcao;
    }

    public Object[] solicitarDadosDeRegistoDeUmArtista(List<Localizacao> localizacoes, List<TipoDeArte> tipoDeArtes) {
        imprimirNovaLinha();
        imprimirSeparador();
        System.out.println("ENVIO DE PEDIDO DE REGISTO DE UM ARTISTA");
        System.out.println("\n- Insira os dados do artista (Nome, Descrição, Foto, Tipo de arte e Localização)\n");

        String nome = lerTexto("    + Nome", 0);
        String descricao = lerTexto("    + Descrição (fale um pouco sobre o artista)", 0);
        String foto = lerTexto("    + URL da foto", 0);
        
        Localizacao localizacao = null;
        imprimirEspacos(4);
        System.out.println("- Preencha dados da localização");
        localizacao = new Localizacao();
        localizacao.setLatitude(lerInteiro("+ Latitude", 8));
        localizacao.setLongitude(lerInteiro("+ Longitude", 8));

        Object tipoDeArteObject = (Object) tipoDeArtes;
        List<Object> tipoDeArteObjects = (List<Object>) tipoDeArteObject;
        int posTA = selecao("+ Selecione o Tipo de arte", tipoDeArteObjects, 8);
        TipoDeArte tipoDeArte = null;
        if (posTA < 0) {
            imprimirEspacos(6);
            System.out.println("Preencha dados do tipo de arte");
            tipoDeArte = new TipoDeArte();
            tipoDeArte.setDescricao(lerTexto("Descrição", 10));
        } else {
            tipoDeArte = tipoDeArtes.get(posTA);
        }

        return new Object[] { nome, descricao, foto, localizacao, tipoDeArte };
    }

    public Object[] editarArtista(Artista artista, List<Localizacao> localizacoes, List<TipoDeArte> tipoDeArtes) {
        imprimirNovaLinha();
        imprimirSeparador();
        System.out.println("EDIÇÃO DOS DADOS DE UM ARTISTA");
        System.out.println("\n- Insira os novos dados do artista (Nome, Descrição, Foto, Tipo de arte e Localização)\n");

        String nome = lerTexto("    + Nome(" + artista.getNome() + ")", 0);
        String descricao = lerTexto("    + Descrição(" + artista.getDescricao() + ")", 0);
        String foto = lerTexto("    + URL da foto(" + artista.getFoto() + ")", 0);

        Localizacao localizacao = null;
        imprimirEspacos(4);
        System.out.println("- Preencha dados da localização");
        localizacao = new Localizacao();
        localizacao.setLatitude(lerInteiro("+ Latitude", 8));
        localizacao.setLongitude(lerInteiro("+ Longitude", 8));

        Object tipoDeArteObject = (Object) tipoDeArtes;
        List<Object> tipoDeArteObjects = (List<Object>) tipoDeArteObject;
        int posTA = selecao("- Selecione o Tipo de arte", tipoDeArteObjects, 8);
        TipoDeArte tipoDeArte = null;
        if (posTA < 0) {
            imprimirEspacos(6);
            System.out.println("Preencha dados do tipo de arte");
            tipoDeArte = new TipoDeArte();
            tipoDeArte.setDescricao(lerTexto("Descrição", 10));
        } else {
            tipoDeArte = tipoDeArtes.get(posTA);
        }

        return new Object[] { nome, descricao, foto, localizacao, tipoDeArte };
    }

    public Object[] solicitarDadosDeRegistoDeUtilizador() {
        System.out.println();
        imprimirSeparador();
        System.out.println("REGISTAR CONTA DE UTILIZADOR - UTILIZADOR GERAL");
        System.out.println("\n- Insira os dados do utilizador (username, email, password, Tipo de utilizador)\n");
        String userame = lerTexto("    + Username", 0);
        String email = lerTexto("    + Email", 0);
        String password = lerTexto("    + Password", 0);
        
        return new Object[] { userame, email, password, TipoDeUtilizador.UTILIZADOR_GERAL.name() };
    }

    public Object[] solicitarDadosDeAutenticacao() {
        System.out.println();
        System.out.println("*****************************************************************************");
        System.out.println("\nFORMULÁRIO DE AUTENTICAÇÃO");
        System.out.println();
        System.out.println("- Preencha os dados de autenticação\n\n");
        String username = lerTexto("Username ou email", 10);
        String password = lerTexto("Password", 10);
        System.out.println("\n");
        return new Object[] { username, password };
    }

    public Object[] definirAtuacaoDeUmArtista(List<Artista> artistas) {
        System.out.println();
        System.out.println("*****************************************************************************");
        System.out.println("\nREGISTO DE ATUACAO DE UM ARTISTA");
        System.out.println();
        Object object = (Object) artistas;
        int artistaId = selecaoSemAdicao("- Selecione o artista a enviar donativo", 
            (List<Object>) object, 4, "Cancelar");
        if(artistaId < 0) {
            return null;
        }
        System.out.println("- Preencha os dados da atuacao do artista (latitude, longitude, data)\n");
        int latitude = lerInteiro("+ Latitude", 4);
        int longitude = lerInteiro("+ Longitude", 4);
        Date data = null;
        do {
            String dataStr = lerTexto("+ Data (dia/mes/ano ou hoje)", 4);
            if(dataStr.equals("hoje") || dataStr.equals("agora")) {
                data = new Date();
            } else {
                try {
                    data = DateFormat.getDateInstance().parse(dataStr);
                } catch (ParseException e) {
                    System.out.println("    ERRO: Formato da data inválido, por favor escreva a data no formato dia/mês/ano ou escreva hoje para a data de hoje.\n");
                }
            }
        } while(data == null);
        System.out.println("");
        return new Object[] { artistaId, latitude, longitude, data };
    }

    public void imprimirArtistas(List<Artista> artistas) {
        int tamanho = artistas.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            imprimirNovaLinha();
            System.out.println("***************************************************************************\n");
            System.out.println("LISTA DE ARTISTAS ENCONTRADOS");
            System.out.println("\n- Selecione um artista");
            do {
                System.out.println();
                if (limiteAtual == 0)
                    System.err.println("        (Nenhum artista foi encontrado)\n");
                for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                    imprimirEspacos(4);
                    System.out.printf("%d. %s\n", i + 1, artistas.get(i));
                }
                imprimirEspacos(4);
                System.out.printf("0. Sair\n\n");
                if (inicioAtual > 0) {
                    System.out.printf("    -1. Anterior");
                }
                if (limiteAtual < tamanho) {
                    System.out.printf("    0. Proximo\n");
                }
                opcao = lerInteiro("Escolha", 0);
                if (opcao == -2 && inicioAtual > 0) {
                    inicioAtual -= quantidadeMaximaPorVez;
                } else if (opcao == -1 && limiteAtual > quantidadeMaximaPorVez) {
                    limiteAtual += quantidadeMaximaPorVez;
                }
            } while (opcao < inicioAtual || opcao > limiteAtual);

            if (opcao != 0) {
                mostrarDetalhesArtista(artistas.get(opcao - 1));
            }
        } while (opcao != 0);
        System.out.println("\n> VOLTANDO AO MENU PRINCIPAL\n");
    }

    private void mostrarDetalhesArtista(Artista artista) {
        System.out.println();
        imprimirSeparador();
        System.out.println("DETALHES DO ARTISTA\n");
        System.out.println("- Eis os detalhes sobre o artista\n");
        System.out.printf("    - Nome: %s\n", artista.getNome());
        System.out.printf("    - Descrição: %s\n", artista.getDescricao());
        System.out.printf("    - Foto de perfil: %s\n", artista.getFoto());
        System.out.printf("    - Tipo de arte: %s\n", artista.getTipoDeArte());
        System.out.printf("    - Classificação: %.2f\n", artista.getClassificacao());
        System.out.printf("    - Está apurado?: %s\n\n", artista.getEstaApurado() ? "Sim" : "Não");
        System.out.print("Pressione <ENTER> para voltar a lista de artistas.");
        try {
            input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void imprimirLocalizacoesComArtistasAAtuar(List<Localizacao> localizacoes) {
        imprimirNovaLinha();
        System.out.println("*********************************************************************************\n");
        System.out.println("LISTAS DE LOCALIZAÇÕES COM ARTISTAS A ATUAR\n");
        System.out.println("- Eis a lista de localizações com artistas a atuar");
        int tamanho = localizacoes.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            do {
                System.out.println();
                if (limiteAtual == 0) {
                    System.out.println("    (Nenhuma localização foi encontaada)\n");
                }
                for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                    imprimirEspacos(4);
                    System.out.printf("%d. %s\n", i + 1, localizacoes.get(i));
                }
                imprimirEspacos(4);
                System.out.printf("0. Sair\n\n");
                if (inicioAtual > 0) {
                    System.out.printf("    -1. Anterior");
                }
                if (limiteAtual < 0) {
                    System.out.printf("    0. Proximo\n");
                }
                do {
                    opcao = lerInteiro("Escolha", 0);
                } while(opcao < -2 || opcao > 0);

                if (opcao == -2 && inicioAtual > 0) {
                    inicioAtual -= quantidadeMaximaPorVez;
                } else if (opcao == -1 && limiteAtual > quantidadeMaximaPorVez) {
                    limiteAtual += quantidadeMaximaPorVez;
                }
            } while (opcao < inicioAtual || opcao > limiteAtual);
        } while (opcao != 0);
        System.out.println("\n> VOLTANDO AO MENU PRINCIPAL\n");
    }

    public void imprimirAtuacoes(List<Atuacao> atuacoes, Artista artista) {
        imprimirNovaLinha();
        imprimirSeparador();
        System.out.println("LISTA DE ATUAÇÕES DO ARTISTA " + artista.getNome());
        int tamanho = atuacoes.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            do {
                System.out.println();
                if (limiteAtual == 0)
                    System.err.println("    (Nenhuma atuação foi encontrada)\n");
                for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                    imprimirEspacos(8);
                    System.out.printf("%d. %s\n", i + 1, atuacoes.get(i));
                }
                imprimirEspacos(8);
                System.out.printf("0. Sair\n\n");
                if (inicioAtual > 0) {
                    System.out.printf("    -1. Anterior");
                }
                if (limiteAtual < 0) {
                    System.out.printf("    0. Proximo\n");
                }
                opcao = lerInteiro("Escolha", 4);
                if (opcao == -2 && inicioAtual > 0) {
                    inicioAtual -= quantidadeMaximaPorVez;
                } else if (opcao == -1 && limiteAtual > quantidadeMaximaPorVez) {
                    limiteAtual += quantidadeMaximaPorVez;
                }
            } while (opcao < inicioAtual || opcao > limiteAtual);
        } while (opcao != 0);
        System.out.println("\n> VOLTANDO AO MENU PRINCIPAL\n");
    }

    public void imprimirAtuacao(Atuacao atuacao, Artista artista) {
        imprimirNovaLinha();
        imprimirSeparador();
        System.out.println("PROXIMA ATUAÇÃO DO ARTISTA " + artista.getNome());
        System.out.println();

        if(atuacao == null) {
            System.err.println("    (Nenhuma atuação foi encontrada)\n");
        } else {
            System.out.printf("        - Data: %s\n", atuacao.getDataDeAtuacao().toLocaleString());
            System.out.printf("        - Localização: { Lat: %d, Long: %d }\n", atuacao.getLocalizacao().getLatitude(), atuacao.getLocalizacao().getLongitude());
        }

        System.out.print("\n    Pressione <ENTER> para continuar.");
        try {
            input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n> VOLTANDO AO MENU PRINCIPAL\n");
    }

    public void imprimirDonativos(List<Donativo> donativos, Artista artista) {
        imprimirNovaLinha();
        System.out.println("LISTA DE DONATIVOS DO ARTISTA " + artista.getNome());
        int tamanho = donativos.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            do {
                System.out.println();
                if (limiteAtual == 0)
                    System.err.println("    (Nenhum artista foi encontrado)\n");
                for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                    imprimirEspacos(4);
                    System.out.printf("%d. %s\n", i + 1, donativos.get(i));
                }
                imprimirEspacos(4);
                System.out.printf("0. Sair\n\n");
                if (inicioAtual > 0) {
                    System.out.printf("    -1. Anterior");
                }
                if (limiteAtual < 0) {
                    System.out.printf("    0. Proximo\n");
                }
                opcao = lerInteiro("Escolha", 0);
                if (opcao == -2 && inicioAtual > 0) {
                    inicioAtual -= quantidadeMaximaPorVez;
                } else if (opcao == -1 && limiteAtual > quantidadeMaximaPorVez) {
                    limiteAtual += quantidadeMaximaPorVez;
                }
            } while (opcao < inicioAtual || opcao > limiteAtual);
        } while (opcao != 0);
        System.out.println("\n> VOLTANDO AO MENU PRINCIPAL\n");
    }

    public FiltrosDeArtistas definirFiltros(List<Localizacao> localizacoes, List<TipoDeArte> tipoDeArtes) {
        System.out.println("\n********************************************************************************\n");
        System.out.println("DEFINIÇÃO DOS FILTROS DE PESQUISA\n");
        Object localizacObject = (Object) localizacoes;
        List<Object> localizacObjects = (List<Object>) localizacObject;
        int posLoc = selecaoSemAdicao("- Seleione a Localização", localizacObjects, 4, "Ignorar filtro");

        Object tipoDeArteObject = (Object) tipoDeArtes;
        List<Object> tipoDeArteObjects = (List<Object>) tipoDeArteObject;
        int posTA = selecaoSemAdicao("- Selecione o Tipo de arte", tipoDeArteObjects, 4, "Ignorar filtro");
        FiltrosDeArtistas fa = new FiltrosDeArtistas();
        if (posLoc >= 0)
            fa.localizacao = localizacoes.get(posLoc);

        if (posTA >= 0)
            fa.tipoDeArte = tipoDeArtes.get(posTA);

        return fa;
    }

    public int selecao(String rotulo, List<Object> dados, int espacos) {
        imprimirEspacos(espacos - 4);
        System.out.print(rotulo);
        int tamanho = dados.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            if (limiteAtual == 0) {
                System.out.println("\n");
                imprimirEspacos(espacos);
                System.out.println("(Lista vazia)\n");
            } else {
                System.out.println();
            }
            for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                imprimirEspacos(espacos);
                System.out.printf("%d. %s\n", i + 1, dados.get(i));
            }
            imprimirEspacos(espacos);
            System.out.printf("0. Criar novo\n\n");
            if (inicioAtual > 0) {
                System.out.printf("    -2. Anterior");
            }
            if (limiteAtual < 0) {
                System.out.printf("    -1. Proximo\n");
            }
            do {
                opcao = lerInteiro("Seleção", espacos - 2);
            } while (!((opcao > inicioAtual && opcao <= limiteAtual) || opcao == -2 || opcao == -1 || opcao == 0));

            if (opcao == -2 && inicioAtual > 0) {
                inicioAtual -= quantidadeMaximaPorVez;
            } else if (opcao == -1 && limiteAtual > quantidadeMaximaPorVez) {
                limiteAtual += quantidadeMaximaPorVez;
            }

        } while (opcao < inicioAtual || opcao > limiteAtual);
        System.out.println();

        if (opcao == limiteAtual + 1)
            return -1;
        return opcao - 1;
    }

    public int selecaoSemAdicao(String rotulo, List<Object> dados, int espacos, String mensagem) {
        imprimirEspacos(espacos - 4);
        System.out.print(rotulo);
        int tamanho = dados.size();
        int quantidadeMaximaPorVez = 8;
        int inicioAtual = 0;
        int limiteAtual = 8;
        int opcao = 0;

        if (limiteAtual > tamanho) {
            limiteAtual = tamanho;
        }

        do {
            if (limiteAtual == 0) {
                System.out.println("\n");
                imprimirEspacos(espacos);
                System.out.println("(Lista vazia)");
            }
            System.out.println("\n");
            for (int i = inicioAtual; i < limiteAtual && i < tamanho; ++i) {
                imprimirEspacos(espacos);
                System.out.printf("%d. %s\n", i + 1, dados.get(i));
            }
            imprimirEspacos(espacos);
            System.out.printf("0. %s\n", mensagem);
            if (inicioAtual > 0) {
                imprimirEspacos(espacos);
                System.out.printf("-2. Anterior\n");
            }
            if (limiteAtual < tamanho) {
                imprimirEspacos(espacos);
                System.out.printf("-1. Proximo\n");
            }
            System.out.println("");
            do {
                opcao = lerInteiro("Seleção", espacos - 2);
            } while (!((opcao > inicioAtual && opcao <= limiteAtual) || opcao == -2 || opcao == -1 || opcao == 0));

            if (opcao == -2 && inicioAtual > 0) {
                inicioAtual -= quantidadeMaximaPorVez;
            } else if (opcao == -1 && limiteAtual < tamanho) {
                inicioAtual = limiteAtual;
                limiteAtual += quantidadeMaximaPorVez;
            }

        } while (opcao < inicioAtual || opcao > limiteAtual);
        System.out.println();

        if (opcao == limiteAtual + 1)
            return -1;
        return opcao - 1;
    }

    public int lerInteiro(String rotulo, int n) {
        String texto = null;
        do {
            imprimirEspacos(n);
            System.out.print(rotulo + ": ");
            try {
                texto = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (texto == null || texto.isEmpty());
        return Integer.parseInt(texto);
    }

    public String lerTexto(String rotulo, int n) {
        String texto = null;
        do {
            imprimirEspacos(n);
            System.out.print(rotulo + ": ");
            try {
                texto = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (texto == null || texto.isEmpty());
        return texto;
    }

    public void imprimirEspacos(int n) {
        for (int i = 0; i < n; ++i) {
            System.out.print(" ");
        }
    }
}
