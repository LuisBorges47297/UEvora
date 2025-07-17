package me.evora.servicos;

import java.util.Date;
import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Localizacao;
import me.evora.modelo.TipoDeArte;
import me.evora.modelo.dto.FiltrosDeArtistas;
import me.evora.repositorio.RepositorioArtista;
import me.evora.repositorio.RepositorioAtuacao;
import me.evora.repositorio.RepositorioClassificacao;
import me.evora.repositorio.RepositorioLocalizacao;
import me.evora.repositorio.RepositorioTipoDeArte;
import me.evora.servicos.erros.ErroArtistaJaEstaApurado;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;

public class ServicosArtista {

    private RepositorioArtista repositorioArtista;
    private RepositorioAtuacao repositorioAtuacao;
    private RepositorioTipoDeArte repositorioTipoDeArte;
    private RepositorioLocalizacao repositorioLocalizacao;
    private RepositorioClassificacao repositorioClassificacao;

    public ServicosArtista(RepositorioArtista repositorioArtista, RepositorioAtuacao repositorioAtuacao, 
        RepositorioTipoDeArte repositorioTipoDeArte, RepositorioLocalizacao repositorioLocalizacao,
        RepositorioClassificacao repositorioClassificacao) {
        this.repositorioArtista = repositorioArtista;
        this.repositorioAtuacao = repositorioAtuacao;
        this.repositorioTipoDeArte = repositorioTipoDeArte;
        this.repositorioLocalizacao = repositorioLocalizacao;
        this.repositorioClassificacao = repositorioClassificacao;
    }

    public void registarNovoArtista(String nome, String descricao, String foto, 
            Localizacao localizacao, TipoDeArte tipoDeArte) {
        Artista artista = repositorioArtista.pesquisarPorNome(nome);
        if(artista == null) {
            artista = new Artista(nome, tipoDeArte, descricao, foto);
            if( tipoDeArte.getId() <= 0 ) {
                tipoDeArte = this.repositorioTipoDeArte.salvar(tipoDeArte);
            }
            artista = repositorioArtista.salvar(artista);
        }
        if( localizacao.getId() <= 0 ) {
            localizacao = this.repositorioLocalizacao.salvar(localizacao);
        }
        Atuacao atuacao = new Atuacao(localizacao, artista);
        repositorioAtuacao.salvar(atuacao);
    }

    public void definirLocalDeAtuacaoDoArtista(int artistaID, Localizacao localizacao, Date date) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = pesquisarArtistaPorId(artistaID);
        if(localizacao.getId() <= 0) {
            localizacao = repositorioLocalizacao.salvar(localizacao);
        }
        Atuacao atuacao = new Atuacao(localizacao, date);
        atuacao.setArtista(artista);
        repositorioAtuacao.salvar(atuacao);
    }

    public void editarArtista(int artistaId, String nome, String descricao, String foto, 
        Localizacao localizacao, TipoDeArte tipoDeArte) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaId);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }

        artista.setNome(nome);
        artista.setDescricao(descricao);
        artista.setTipoDeArte(tipoDeArte);
        artista.setFoto(foto);
        repositorioArtista.editar(artista);
        repositorioAtuacao.reiniciarAtuacoes(artistaId);

        if(localizacao.getId() <= 0) {
            localizacao = repositorioLocalizacao.salvar(localizacao);
        }
        Atuacao novaAtuacao = new Atuacao(localizacao, artista);
        repositorioAtuacao.salvar(novaAtuacao);
    }

    public void apurarArtista(int artistaID) throws ErroDeEntidadeNaoEncontrada, ErroArtistaJaEstaApurado {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        if(artista.getEstaApurado()) {
            throw new ErroArtistaJaEstaApurado("Artista já está apurado");
        }

        artista.setEstaApurado(true);
        repositorioArtista.editar(artista);
    }

    public List<Artista> listarArtistas() {
        return repositorioArtista.listar();
    }

    public List<Artista> listarArtistas(FiltrosDeArtistas filtros) {
        return repositorioArtista.listar(filtros);
    }

    public List<Artista> listarArtistasPorEstado(boolean apurado) {
        if(apurado) {
            return repositorioArtista.listarArtistasApurados();
        }
        return repositorioArtista.listarArtistasNaoApurados();
    }

    public Artista pesquisarArtistaPorId(int artistaID) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        return artista;
    }
}
