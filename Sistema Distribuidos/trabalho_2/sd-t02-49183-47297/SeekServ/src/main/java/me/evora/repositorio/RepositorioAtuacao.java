package me.evora.repositorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioAtuacao {
    private PreparedStatement salvarAtuacao;
    private PreparedStatement editarAtuacao;
    private PreparedStatement listarAtuacoes;
    private PreparedStatement pesquisarArtista;
    private PreparedStatement pesquisarLocalizacao;
    private PreparedStatement listarPorArtista;
    private PreparedStatement listarAtuacoesAtuais;
    private PreparedStatement pesquisarAtuacaoPorId;
    private PreparedStatement listarProximaAtuacao;

    public RepositorioAtuacao(Connection baseDeDados) throws SQLException {
        this.salvarAtuacao = baseDeDados.prepareStatement(
                "INSERT INTO atuacao (data, localizacao, artista, atuando) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        this.editarAtuacao = baseDeDados
                .prepareStatement("UPDATE atuacao SET atuando=0 WHERE artista=?");
        this.listarAtuacoes = baseDeDados.prepareStatement("SELECT * FROM atuacao");
        this.pesquisarArtista = baseDeDados.prepareStatement("SELECT * FROM artista WHERE id=?");
        this.pesquisarLocalizacao = baseDeDados.prepareStatement("SELECT * FROM localizacao WHERE id=?");
        this.listarPorArtista = baseDeDados.prepareStatement("SELECT * FROM atuacao WHERE artista=? AND data < CURRENT_DATE");
        this.listarAtuacoesAtuais = baseDeDados.prepareStatement("SELECT * FROM atuacao WHERE atuando=1");
        this.pesquisarAtuacaoPorId = baseDeDados.prepareStatement("SELECT * FROM atuacao WHERE id=?");
        this.listarProximaAtuacao = 
            baseDeDados.prepareStatement("SELECT * FROM atuacao WHERE artista=? AND data > CURRENT_DATE ORDER BY data ASC LIMIT 1");
    }

    public Atuacao salvar(Atuacao atuacao) {
        try {
            salvarAtuacao.setDate(1, new Date(atuacao.getDataDeAtuacao().getTime()));
            salvarAtuacao.setInt(2, atuacao.getLocalizacao().getId());
            salvarAtuacao.setInt(3, atuacao.getArtista().getId());
            salvarAtuacao.setInt(4, atuacao.getEAtual() ? 1 : 0);
            salvarAtuacao.executeUpdate();
            ResultSet rs = salvarAtuacao.getGeneratedKeys();
            if(rs.next()) {
                return pesquisarAtuacaoPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editar(Atuacao atuacao) {
        try {
            editarAtuacao.setDate(1, new Date(atuacao.getDataDeAtuacao().getTime()));
            editarAtuacao.setInt(2, atuacao.getLocalizacao().getId());
            editarAtuacao.setInt(3, atuacao.getArtista().getId());
            editarAtuacao.setInt(4, atuacao.getEAtual() ? 1 : 0);
            editarAtuacao.setInt(5, atuacao.getId());
            editarAtuacao.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reiniciarAtuacoes(int artistaId) {
        try {
            editarAtuacao.setInt(1, artistaId);
            editarAtuacao.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Atuacao> listar() {
        List<Atuacao> atuacoes = new ArrayList<>();
        try {
            ResultSet resultado = listarAtuacoes.executeQuery();
            while (resultado.next()) {
                Atuacao atuacao = ServicoDeRecolhaDeDados.recolherAtuacao(resultado);
                int idArtista = resultado.getInt("artista");
                pesquisarArtista.setInt(1, idArtista);
                ResultSet resultadoArtista = pesquisarArtista.executeQuery();
                if (resultadoArtista.next()) {
                    atuacao.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                int idLocalizacao = resultado.getInt("localizacao");
                pesquisarLocalizacao.setInt(1, idLocalizacao);
                ResultSet resultadoLocalizacao = pesquisarLocalizacao.executeQuery();
                if (resultadoLocalizacao.next()) {
                    atuacao.setLocalizacao(ServicoDeRecolhaDeDados.recolherLocalizacao(resultadoLocalizacao));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atuacoes;
    }

    public List<Atuacao> listarAtuacoesPassadasPorArtista(Artista artista) {
        List<Atuacao> atuacoes = new ArrayList<>();
        try {
            listarPorArtista.setInt(1, artista.getId());
            ResultSet resultado = listarPorArtista.executeQuery();
            while (resultado.next()) {
                Atuacao atuacao = ServicoDeRecolhaDeDados.recolherAtuacao(resultado);
                int idArtista = resultado.getInt("artista");
                pesquisarArtista.setInt(1, idArtista);
                ResultSet resultadoArtista = pesquisarArtista.executeQuery();
                if (resultadoArtista.next()) {
                    atuacao.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                int idLocalizacao = resultado.getInt("localizacao");
                pesquisarLocalizacao.setInt(1, idLocalizacao);
                ResultSet resultadoLocalizacao = pesquisarLocalizacao.executeQuery();
                if (resultadoLocalizacao.next()) {
                    atuacao.setLocalizacao(ServicoDeRecolhaDeDados.recolherLocalizacao(resultadoLocalizacao));
                }
                atuacoes.add(atuacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atuacoes;
    }

    public Atuacao listarProximaAtuacaoPorArtista(Artista artista) {
        try {
            listarProximaAtuacao.setInt(1, artista.getId());
            ResultSet resultado = listarProximaAtuacao.executeQuery();
            if (resultado.next()) {
                Atuacao atuacao = ServicoDeRecolhaDeDados.recolherAtuacao(resultado);
                int idArtista = resultado.getInt("artista");
                pesquisarArtista.setInt(1, idArtista);
                ResultSet resultadoArtista = pesquisarArtista.executeQuery();
                if (resultadoArtista.next()) {
                    atuacao.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                int idLocalizacao = resultado.getInt("localizacao");
                pesquisarLocalizacao.setInt(1, idLocalizacao);
                ResultSet resultadoLocalizacao = pesquisarLocalizacao.executeQuery();
                if (resultadoLocalizacao.next()) {
                    atuacao.setLocalizacao(ServicoDeRecolhaDeDados.recolherLocalizacao(resultadoLocalizacao));
                }
                return atuacao;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Atuacao> listarAtuacoesAtuais() {
        List<Atuacao> atuacoes = new ArrayList<>();
        try {
            ResultSet resultado = listarAtuacoesAtuais.executeQuery();
            while (resultado.next()) {
                Atuacao atuacao = ServicoDeRecolhaDeDados.recolherAtuacao(resultado);
                int idArtista = resultado.getInt("artista");
                pesquisarArtista.setInt(1, idArtista);
                ResultSet resultadoArtista = pesquisarArtista.executeQuery();
                if (resultadoArtista.next()) {
                    atuacao.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                int idLocalizacao = resultado.getInt("localizacao");
                pesquisarLocalizacao.setInt(1, idLocalizacao);
                ResultSet resultadoLocalizacao = pesquisarLocalizacao.executeQuery();
                if (resultadoLocalizacao.next()) {
                    atuacao.setLocalizacao(ServicoDeRecolhaDeDados.recolherLocalizacao(resultadoLocalizacao));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atuacoes;
    }

    public Atuacao pesquisarAtuacaoPorId(int atuacaoId) {
        try {
            pesquisarAtuacaoPorId.setInt(1, atuacaoId);
            ResultSet resultado = pesquisarAtuacaoPorId.executeQuery();
            if (resultado.next()) {
                Atuacao atuacao = ServicoDeRecolhaDeDados.recolherAtuacao(resultado);
                return atuacao;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
