package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Classificacao;
import me.evora.modelo.Donativo;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioClassificacao {
    private PreparedStatement salvarClassificacao;
    private PreparedStatement listarClassficacoes;
    private PreparedStatement listarClassficacoesPorArtista;
    private PreparedStatement pesquisarArtistaPorId;
    private PreparedStatement pesquisarClassficacaoPorId;
    private PreparedStatement encontrarTipoDeArte;
    private PreparedStatement mediaDasClassificacoes;

    public RepositorioClassificacao(Connection baseDeDados) throws SQLException {
        this.salvarClassificacao = baseDeDados.prepareStatement("INSERT INTO classificacao (artista, classe, comentario, utilizador) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        this.pesquisarArtistaPorId = baseDeDados.prepareStatement("SELECT * FROM artista WHERE id=?");
        this.mediaDasClassificacoes = baseDeDados.prepareStatement("SELECT AVG(classe) as media FROM classificacao WHERE id=?");
        this.listarClassficacoesPorArtista = baseDeDados.prepareStatement("SELECT * FROM classifcacao WHERE artista=?");
        this.listarClassficacoes = baseDeDados.prepareStatement("SELECT * FROM classificacao");
        pesquisarClassficacaoPorId = baseDeDados.prepareStatement("SELECT * FROM classificacao WHERE id=?");
        encontrarTipoDeArte = baseDeDados.prepareStatement("SELECT * FROM tipo_de_arte WHERE id = ?");
    }

    public Classificacao salvar(Classificacao classificacao) {
        try {
            salvarClassificacao.setInt(1, classificacao.getArtista().getId());
            salvarClassificacao.setDouble(2, classificacao.getClasse());
            salvarClassificacao.setString(3, classificacao.getComentario());
            salvarClassificacao.setInt(4, classificacao.getOferecedor().getId());
            salvarClassificacao.executeUpdate();
            ResultSet rs = salvarClassificacao.getGeneratedKeys();
            if (rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double calcularMediaDasClassificacoes(int artistaID) {
        try {
            mediaDasClassificacoes.setInt(1, artistaID);
            ResultSet resultado = mediaDasClassificacoes.executeQuery();
            while (resultado.next()) {
                return resultado.getDouble("media");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void editar(Donativo donativo) {
    }

    public List<Classificacao> listar() {
        List<Classificacao> classificacoes = new ArrayList<>();
        try {
            ResultSet resultado = listarClassficacoes.executeQuery();
            while (resultado.next()) {
                Classificacao classificacao = ServicoDeRecolhaDeDados.recolherClassificacao(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    classificacao.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                classificacoes.add(classificacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classificacoes;
    }

    public List<Classificacao> listarPorArtistaID(int artistaID) {
        List<Classificacao> classificacoes = new ArrayList<>();
        try {
            listarClassficacoesPorArtista.setInt(1, artistaID);
            ResultSet resultado = listarClassficacoesPorArtista.executeQuery();
            while (resultado.next()) {
                Classificacao classificacao = ServicoDeRecolhaDeDados.recolherClassificacao(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    classificacao.setArtista(getArtistaComArte(resultadoArtista));
                }
                classificacoes.add(classificacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classificacoes;
    }

    public Classificacao pesquisarPorId(int id) {
        try {
            pesquisarClassficacaoPorId.setInt(1, id);
            ResultSet resultado = pesquisarClassficacaoPorId.executeQuery();
            if (resultado.next()) {
                Classificacao classificaco = ServicoDeRecolhaDeDados.recolherClassificacao(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    classificaco.setArtista(getArtistaComArte(resultadoArtista));
                }
                return classificaco;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Artista getArtistaComArte(ResultSet resultado) throws SQLException {
        Artista artista = ServicoDeRecolhaDeDados.recolherArtista(resultado);
        encontrarTipoDeArte.setInt(1, resultado.getInt("tipo_de_arte"));
        ResultSet resultadoArte = encontrarTipoDeArte.executeQuery();
        if(resultadoArte.next()) {
            artista.setTipoDeArte(ServicoDeRecolhaDeDados.recolherTipoDeArte(resultadoArte));
        }
        return artista;
    }
}
