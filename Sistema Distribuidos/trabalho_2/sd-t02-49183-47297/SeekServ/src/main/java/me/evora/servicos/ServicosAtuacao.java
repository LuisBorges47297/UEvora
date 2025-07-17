package me.evora.servicos;

import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Localizacao;
import me.evora.repositorio.RepositorioArtista;
import me.evora.repositorio.RepositorioAtuacao;
import me.evora.repositorio.RepositorioLocalizacao;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;

public class ServicosAtuacao {
    private RepositorioAtuacao repositorioAtuacao;
    private RepositorioArtista repositorioArtista;
    private RepositorioLocalizacao repositorioLocalizacao;

    public ServicosAtuacao(RepositorioArtista repositorioArtista, RepositorioAtuacao repositorioAtuacao,
        RepositorioLocalizacao repositorioLocalizacao) {
        this.repositorioAtuacao = repositorioAtuacao;
        this.repositorioArtista = repositorioArtista;
        this.repositorioLocalizacao = repositorioLocalizacao;
    }

    public void criarAtuacao(int artistaID, Localizacao localizacao) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        if(localizacao.getId() <= 0) {
            localizacao = this.repositorioLocalizacao.salvar(localizacao);
        }
        Atuacao atuacao = new Atuacao(localizacao, artista);
        repositorioAtuacao.salvar(atuacao);
    }

    public List<Atuacao> listarAtuacoesPassadasDoArtista(int artistaID) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        return repositorioAtuacao.listarAtuacoesPassadasPorArtista(artista);
    }

    public Atuacao listarProximaAtuacao(int artistaID) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        Atuacao atuacao = repositorioAtuacao.listarProximaAtuacaoPorArtista(artista);
        if(atuacao == null) {
            atuacao = new Atuacao();
            atuacao.setId(0); // Nenhum dado
        }
        return atuacao;
    }

    public Atuacao pesquisarAtuacaoPorId(int atuacaoId) throws ErroDeEntidadeNaoEncontrada {
        Atuacao atuacao = repositorioAtuacao.pesquisarAtuacaoPorId(atuacaoId);
        if( atuacao == null ) {
            throw new ErroDeEntidadeNaoEncontrada("Atuação não encontrada.");
        }
        return atuacao;
    }
}
