package me.evora.servicos;

import java.util.List;

import me.evora.modelo.TipoDeArte;
import me.evora.repositorio.RepositorioTipoDeArte;

public class ServicosTipoDeArte {
    private RepositorioTipoDeArte repositorioTipoDeArte;

    public ServicosTipoDeArte(RepositorioTipoDeArte repositorioTipoDeArte) {
        this.repositorioTipoDeArte = repositorioTipoDeArte;
    }

    public void criarTipoDeArte(String descricao) {
        TipoDeArte tipoDeArte = new TipoDeArte(descricao);
        repositorioTipoDeArte.salvar(tipoDeArte);
    }

    public TipoDeArte pesquisarLocalizacaoPorId(int tipoDeArteId) {
        return repositorioTipoDeArte.pesquisarPorId(tipoDeArteId);
    }

    public List<TipoDeArte> listarTodosTiposDeArte() {
        return repositorioTipoDeArte.listar();
    }
}
