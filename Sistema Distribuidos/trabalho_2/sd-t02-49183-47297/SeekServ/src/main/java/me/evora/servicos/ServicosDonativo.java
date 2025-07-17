package me.evora.servicos;

import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Donativo;
import me.evora.modelo.Utilizador;
import me.evora.repositorio.RepositorioArtista;
import me.evora.repositorio.RepositorioDonativo;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;

public class ServicosDonativo {
    private RepositorioDonativo repositorioDonativo;
    private RepositorioArtista repositorioArtista;

    public ServicosDonativo(RepositorioArtista repositorioArtista, RepositorioDonativo repositorioDonativo) {
        this.repositorioDonativo = repositorioDonativo;
        this.repositorioArtista = repositorioArtista;
    }

    public void criarDonativo(int artistaID, Utilizador utilizador, double valor) {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        Donativo donativo = new Donativo(valor, artista, utilizador);
        repositorioDonativo.salvar(donativo);
    }

    public List<Donativo> listarDonativosDoArtista(int artistaID) throws ErroDeEntidadeNaoEncontrada {
        Artista artista = repositorioArtista.pesquisarPorId(artistaID);
        if(artista == null) {
            throw new ErroDeEntidadeNaoEncontrada("O Artista não foi encontrado.");
        }
        return repositorioDonativo.listarPorArtistaID(artista.getId());
    }

    public Donativo pesquisarDonativoPorId(int donativoId) throws ErroDeEntidadeNaoEncontrada {
        Donativo donativo = repositorioDonativo.pesquisarPorId(donativoId);
        if(donativo == null) {
            throw new ErroDeEntidadeNaoEncontrada("Donativo não encontrado.");
        }
        return donativo;
    }
}
