package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.TipoDeArte;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioTipoDeArte {
    private PreparedStatement salavarTipoDeArte;
    private PreparedStatement listarTiposDeArte;
    private PreparedStatement pesquisarPorArtista;
    private PreparedStatement pesquisarPorId;

    public RepositorioTipoDeArte(Connection baseDeDados) throws SQLException {
        this.salavarTipoDeArte = baseDeDados.prepareStatement("INSERT INTO tipo_de_arte (descricao) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        this.listarTiposDeArte = baseDeDados.prepareStatement("SELECT * FROM tipo_de_arte");
        this.pesquisarPorArtista = baseDeDados.prepareStatement(
                "SELECT tipo_de_arte.id, descricao, data_criacao FROM tipo_de_arte, artista WHERE tipo_de_arte.id=tipo_de_arte AND artista.id=?");
        this.pesquisarPorId = baseDeDados.prepareStatement("SELECT * FROM tipo_de_arte WHERE id=?");
    }

    public TipoDeArte salvar(TipoDeArte tipoDeArte) {
        try {
            salavarTipoDeArte.setString(1, tipoDeArte.getDescricao());
            salavarTipoDeArte.executeUpdate();
            ResultSet rs = salavarTipoDeArte.getGeneratedKeys();
            if (rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editar(TipoDeArte tipoDeArte) {
    }

    public List<TipoDeArte> listar() {
        List<TipoDeArte> tiposDeArte = new ArrayList<>();
        try {
            ResultSet resultado = listarTiposDeArte.executeQuery();
            while (resultado.next()) {
                tiposDeArte.add(ServicoDeRecolhaDeDados.recolherTipoDeArte(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiposDeArte;
    }

    public TipoDeArte pesquisarPorAtista(int artistaId) {
        try {
            pesquisarPorArtista.setInt(1, artistaId);
            ResultSet resultado = pesquisarPorArtista.executeQuery();
            if (resultado.next()) {
                return ServicoDeRecolhaDeDados.recolherTipoDeArte(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TipoDeArte pesquisarPorId(int arteId) {
        try {
            pesquisarPorId.setInt(1, arteId);
            ResultSet resultado = pesquisarPorId.executeQuery();
            if (resultado.next()) {
                return ServicoDeRecolhaDeDados.recolherTipoDeArte(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
