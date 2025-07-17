package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.Localizacao;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioLocalizacao {
    private PreparedStatement salvarLocalizacao;
    private PreparedStatement listarLocalizacoes;
    private PreparedStatement listarLocalizacoesAtuais;
    private PreparedStatement pesquisarLocalizacaoPorId;

    public RepositorioLocalizacao(Connection baseDeDados) throws SQLException {
        this.salvarLocalizacao = baseDeDados.prepareStatement(
                "INSERT INTO localizacao (latitude, longitude) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        this.listarLocalizacoes = baseDeDados.prepareStatement("SELECT * FROM localizacao");
        this.listarLocalizacoesAtuais = baseDeDados.prepareStatement(
                "SELECT localizacao.id, latitude, longitude, data_criacao FROM localizacao, atuacao " +
                "WHERE localizacao.id=atuacao.id AND EXTRACT(DAY FROM atuacao.data) = EXTRACT(DAY FROM CURRENT_TIMESTAMP) " +
                "AND EXTRACT(MONTH FROM atuacao.data) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
                "AND EXTRACT(YEAR FROM atuacao.data) = EXTRACT(YEAR FROM CURRENT_TIMESTAMP)");
        this.pesquisarLocalizacaoPorId = baseDeDados.prepareStatement("SELECT * FROM localizacao WHERE id=?");
    }

    public Localizacao salvar(Localizacao localizacao) {
        try {
            salvarLocalizacao.setInt(1, localizacao.getLatitude());
            salvarLocalizacao.setInt(2, localizacao.getLongitude());
            salvarLocalizacao.executeUpdate();
            ResultSet rs = salvarLocalizacao.getGeneratedKeys();
            if (rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editar(Localizacao localizacao) {

    }

    public List<Localizacao> listar() {
        List<Localizacao> localizacoes = new ArrayList<>();
        try {
            ResultSet resultado = listarLocalizacoes.executeQuery();
            while (resultado.next()) {
                localizacoes.add(ServicoDeRecolhaDeDados.recolherLocalizacao(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localizacoes;
    }

    public List<Localizacao> listarLocalizacoesAtuais() {
        List<Localizacao> localizacoes = new ArrayList<>();
        try {
            ResultSet resultado = listarLocalizacoesAtuais.executeQuery();
            while (resultado.next()) {
                localizacoes.add(ServicoDeRecolhaDeDados.recolherLocalizacao(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localizacoes;
    }

    public Localizacao pesquisarPorId(int localizacaoId) {
        try {
            pesquisarLocalizacaoPorId.setInt(1, localizacaoId);
            ResultSet resultado = pesquisarLocalizacaoPorId.executeQuery();
            if (resultado.next()) {
                return ServicoDeRecolhaDeDados.recolherLocalizacao(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
