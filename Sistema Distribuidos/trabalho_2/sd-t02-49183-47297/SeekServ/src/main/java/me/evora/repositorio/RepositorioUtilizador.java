package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Artista;
import me.evora.modelo.Utilizador;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioUtilizador {
    private PreparedStatement salvarUtilizador;
    private PreparedStatement promoverAdmin;
    private PreparedStatement pesquisarUtilizadorPorUsernameOuEmail;
    private PreparedStatement pesquisarUtilizadorPorId;
    private PreparedStatement listarUtilizadoresGerais;

    public RepositorioUtilizador(Connection baseDeDados) throws SQLException {
        this.salvarUtilizador = 
            baseDeDados.prepareStatement("INSERT INTO utilizador(username, email, password, tipo) VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS);
        this.promoverAdmin = 
            baseDeDados.prepareStatement("UPDATE utilizador SET tipo=? WHERE id=?");
        this.pesquisarUtilizadorPorUsernameOuEmail = 
            baseDeDados.prepareStatement("SELECT * FROM utilizador WHERE email=? OR username=?");
        this.pesquisarUtilizadorPorId = 
            baseDeDados.prepareStatement("SELECT * FROM utilizador WHERE id=?");
        this.listarUtilizadoresGerais = baseDeDados.prepareStatement("SELECT * FROM utilizador WHERE tipo=?");
    }

    public Utilizador salvar(Utilizador utilizador) {
        try {
            salvarUtilizador.setString(1, utilizador.getUsername());
            salvarUtilizador.setString(2, utilizador.getEmail());
            salvarUtilizador.setString(3, utilizador.getPassword());
            salvarUtilizador.setString(4, utilizador.getTipo().toString());
            salvarUtilizador.executeUpdate();
            ResultSet rs = salvarUtilizador.getGeneratedKeys();
            if(rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Utilizador> listarUtilizadoresGerais() {
        List<Utilizador> lista = new ArrayList<>();
        try {
            listarUtilizadoresGerais.setString(1, TipoDeUtilizador.UTILIZADOR_GERAL.name());
            ResultSet resultado = listarUtilizadoresGerais.executeQuery();
            while (resultado.next()) {
                lista.add(ServicoDeRecolhaDeDados.recolherUtilizador(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Utilizador promoverUtilizadorAAdmin(int utilizadorID) {
        try {
            promoverAdmin.setString(1, TipoDeUtilizador.ADMINISTRADOR.name());
            promoverAdmin.setInt(2, utilizadorID);
            promoverAdmin.executeUpdate();
            ResultSet rs = promoverAdmin.getGeneratedKeys();
            if(rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilizador pesquisarPorUSernameOuEmail(String username, String email) {
        try {
            pesquisarUtilizadorPorUsernameOuEmail.setString(1, email);
            pesquisarUtilizadorPorUsernameOuEmail.setString(2, username);
            ResultSet resultado = pesquisarUtilizadorPorUsernameOuEmail.executeQuery();
            if(resultado.next()) {
                return ServicoDeRecolhaDeDados.recolherUtilizador(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Utilizador pesquisarPorId(int utilizadorID) {
        try {
            pesquisarUtilizadorPorId.setInt(1, utilizadorID);
            ResultSet resultado = pesquisarUtilizadorPorId.executeQuery();
            if(resultado.next()) {
                return ServicoDeRecolhaDeDados.recolherUtilizador(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
