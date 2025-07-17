package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.Donativo;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioDonativo {
    private PreparedStatement salvarDonativo;
    private PreparedStatement listarDonativos;
    private PreparedStatement listarDonativosPorArtista;
    private PreparedStatement pesquisarArtistaPorId;
    private PreparedStatement pesquisarUtilizadorPorId;
    private PreparedStatement pesquisarDonativoPorId;
    private PreparedStatement encontrarTipoDeArte;

    public RepositorioDonativo(Connection baseDeDados) throws SQLException {
        this.salvarDonativo = baseDeDados.prepareStatement("INSERT INTO donativo (artista, valor, utilizador) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        this.pesquisarArtistaPorId = baseDeDados.prepareStatement("SELECT * FROM artista WHERE id=?");
        this.pesquisarUtilizadorPorId = baseDeDados.prepareStatement("SELECT * FROM utilizador WHERE id=?");
        this.listarDonativosPorArtista = baseDeDados.prepareStatement("SELECT * FROM donativo WHERE artista=?");
        this.listarDonativos = baseDeDados.prepareStatement("SELECT * FROM donativo");
        pesquisarDonativoPorId = baseDeDados.prepareStatement("SELECT * FROM donativo WHERE id=?");
        encontrarTipoDeArte = baseDeDados.prepareStatement("SELECT * FROM tipo_de_arte WHERE id = ?");
    }

    public Donativo salvar(Donativo donativo) {
        try {
            salvarDonativo.setInt(1, donativo.getArtista().getId());
            salvarDonativo.setFloat(2, (float) donativo.getValor());
            salvarDonativo.setInt(3, donativo.getOferecedor().getId());
            salvarDonativo.executeUpdate();
            ResultSet rs = salvarDonativo.getGeneratedKeys();
            if (rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editar(Donativo donativo) {
    }

    public List<Donativo> listar() {
        List<Donativo> donativos = new ArrayList<>();
        try {
            ResultSet resultado = listarDonativos.executeQuery();
            while (resultado.next()) {
                Donativo donativo = ServicoDeRecolhaDeDados.recolherDonativo(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    donativo.setArtista(ServicoDeRecolhaDeDados.recolherArtista(resultadoArtista));
                }
                donativos.add(donativo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donativos;
    }

    public List<Donativo> listarPorArtistaID(int artistaID) {
        List<Donativo> donativos = new ArrayList<>();
        try {
            listarDonativosPorArtista.setInt(1, artistaID);
            ResultSet resultado = listarDonativosPorArtista.executeQuery();
            while (resultado.next()) {
                Donativo donativo = ServicoDeRecolhaDeDados.recolherDonativo(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    donativo.setArtista(getArtistaComArte(resultadoArtista));
                }
                int utilizadorId = resultado.getInt("utilizador");
                pesquisarUtilizadorPorId.setInt(1, utilizadorId);
                ResultSet resultadoUtilizador = pesquisarUtilizadorPorId.executeQuery();
                if(resultadoUtilizador.next()) {
                    donativo.setOferecedor(ServicoDeRecolhaDeDados.recolherUtilizador(resultadoUtilizador));
                }
                donativos.add(donativo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donativos;
    }

    public Donativo pesquisarPorId(int id) {
        try {
            pesquisarDonativoPorId.setInt(1, id);
            ResultSet resultado = pesquisarDonativoPorId.executeQuery();
            if (resultado.next()) {
                Donativo donativo = ServicoDeRecolhaDeDados.recolherDonativo(resultado);
                int artistaId = resultado.getInt("artista");
                pesquisarArtistaPorId.setInt(1, artistaId);
                ResultSet resultadoArtista = pesquisarArtistaPorId.executeQuery();
                if (resultadoArtista.next()) {
                    donativo.setArtista(getArtistaComArte(resultadoArtista));
                }
                return donativo;
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
