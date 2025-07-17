package me.evora.servicos;

import java.util.List;

import me.evora.modelo.Localizacao;
import me.evora.repositorio.RepositorioLocalizacao;

public class ServicosLocalizacao {
    private RepositorioLocalizacao repositorioLocalizacao;

    public ServicosLocalizacao(RepositorioLocalizacao repositorioLocalizacao) {
        this.repositorioLocalizacao = repositorioLocalizacao;
    }

    public void criarLocalizacao(int latitude, int longitude) {
        Localizacao localizacao = new Localizacao(latitude, longitude);
        repositorioLocalizacao.salvar(localizacao);
    }

    public List<Localizacao> listarLocalizacoesComAtuacao() {
        return repositorioLocalizacao.listarLocalizacoesAtuais();
    }

    public List<Localizacao> listarTodasLocalizacoes() {
        return this.repositorioLocalizacao.listar();
    }

    public Localizacao pesquisarLocalizacaoPorId(int localizacaoId) {
        return repositorioLocalizacao.pesquisarPorId(localizacaoId);
    }
}
