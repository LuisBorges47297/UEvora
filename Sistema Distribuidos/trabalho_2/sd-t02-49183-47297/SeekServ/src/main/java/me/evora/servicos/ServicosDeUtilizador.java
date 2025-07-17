package me.evora.servicos;

import java.util.List;

import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Utilizador;
import me.evora.repositorio.RepositorioUtilizador;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;
import me.evora.servicos.erros.ErroEntidadeJaExiste;

public class ServicosDeUtilizador {
    private RepositorioUtilizador repositorioDeUtilizadores;

    public ServicosDeUtilizador(RepositorioUtilizador repositorioUtilizador) {
        this.repositorioDeUtilizadores = repositorioUtilizador;
    }

    public void registarUtilizador(String username, String email, String password, TipoDeUtilizador tipo) throws ErroEntidadeJaExiste {
        Utilizador utilizador = repositorioDeUtilizadores.pesquisarPorUSernameOuEmail(username, email);
        if(utilizador != null) {
            throw new ErroEntidadeJaExiste("Utilizador com mesmo email ou password já existe");
        }
        utilizador = new Utilizador();
        utilizador.setUsername(username);
        utilizador.setEmail(email);
        utilizador.setPassword(password);
        utilizador.setTipo(tipo);
        utilizador = repositorioDeUtilizadores.salvar(utilizador);
        System.out.println(utilizador.getId());
    }

    public List<Utilizador> listarTodosUtilizadoresGerais() {
        return repositorioDeUtilizadores.listarUtilizadoresGerais();
    }

    public void promoverUtilizadorAAdministrador(int utilizadorID) throws ErroDeEntidadeNaoEncontrada {
        Utilizador utilizador = pesquisarUtilizadorPorId(utilizadorID);
        repositorioDeUtilizadores.promoverUtilizadorAAdmin(utilizador.getId());
    }

    public Utilizador pesquisarUtilizadorPorId(int utilizadorID) throws ErroDeEntidadeNaoEncontrada {
        Utilizador utilizador = repositorioDeUtilizadores.pesquisarPorId(utilizadorID);
        if( utilizador == null ) {
            throw new ErroDeEntidadeNaoEncontrada("Atuação não encontrada.");
        }
        return utilizador;
    }

    public Utilizador autenticarUtilizador(String usernameOrEmail, String password) throws ErroDeEntidadeNaoEncontrada {
        Utilizador utilizador = 
            repositorioDeUtilizadores.pesquisarPorUSernameOuEmail(usernameOrEmail, usernameOrEmail);
        if(utilizador == null || !utilizador.getPassword().equals(password)) {
            throw new ErroDeEntidadeNaoEncontrada("Utilizador com o username ou email e senha não existe");
        }
        return utilizador;
    }
}
