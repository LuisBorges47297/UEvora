package me.evora.servicos;

import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Classificacao;
import me.evora.modelo.Utilizador;
import me.evora.repositorio.RepositorioArtista;
import me.evora.repositorio.RepositorioClassificacao;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;

public class ServicoClassificacao {
    private RepositorioClassificacao repositorioClassificacao;
    private RepositorioArtista repositorioArtista;

    public ServicoClassificacao(RepositorioArtista repositorioArtista, RepositorioClassificacao repositorioClassificacao) {
        this.repositorioClassificacao = repositorioClassificacao;
        this.repositorioArtista = repositorioArtista;
    }

    public void criarClassificacao(int artistaID, Utilizador utilizador, double classe, String comentario) {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        Classificacao classificacao = new Classificacao(classe, artista, utilizador);
        classificacao.setComentario(comentario);
        repositorioClassificacao.salvar(classificacao);
    }

    public List<Classificacao> listarClassificacoesDoArtista(int artistaID) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        return repositorioClassificacao.listarPorArtistaID(artista.getId());
    }

    public Classificacao pesquisarClassificacaoPorId(int classificacaoID) throws ErroDeEntidadeNaoEncontrada {
        Classificacao classificacao = repositorioClassificacao.pesquisarPorId(classificacaoID);
        if(classificacao == null) {
            throw new ErroDeEntidadeNaoEncontrada("Classificação não encontrada.");
        }
        return classificacao;
    }
}
