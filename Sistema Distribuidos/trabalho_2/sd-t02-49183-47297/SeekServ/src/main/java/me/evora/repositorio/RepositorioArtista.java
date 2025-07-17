package me.evora.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.evora.modelo.Artista;
import me.evora.modelo.dto.FiltrosDeArtistas;
import me.evora.servicos.ServicoDeRecolhaDeDados;

public class RepositorioArtista {
    private PreparedStatement salvarArtista;
    private PreparedStatement encontrarArtista;
    private PreparedStatement encontrarArtistaPorNome;
    private PreparedStatement listarArtistas;
    private PreparedStatement encontrarTipoDeArte;
    private PreparedStatement editarArtista;
    private PreparedStatement listarArtistasFiltroLocalizacao;
    private PreparedStatement listarArtistasFiltroLocalizacaoTipoDeArte;
    private PreparedStatement listarArtistasFiltroTipoDeArte;
    private PreparedStatement listarArtistasApurados;
    private PreparedStatement listarAstistasNaoApurados;
    private PreparedStatement mediaDasClassificacoes;

    public RepositorioArtista(Connection baseDeDados) throws SQLException {
        salvarArtista = baseDeDados.prepareStatement(
                "INSERT INTO artista (nome, descricao, foto, tipo_de_arte, esta_apurado) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        encontrarArtista = baseDeDados.prepareStatement("SELECT * FROM artista WHERE id = ?");
        encontrarArtistaPorNome = baseDeDados.prepareStatement("SELECT * FROM artista WHERE nome = ?");
        listarArtistas = baseDeDados.prepareStatement("SELECT * FROM artista");
        encontrarTipoDeArte = baseDeDados.prepareStatement("SELECT * FROM tipo_de_arte WHERE id = ?");
        editarArtista = baseDeDados.prepareStatement(
                "UPDATE artista SET nome=?, descricao=?, foto=?, tipo_de_arte=?, esta_apurado=? WHERE id=?");
        listarArtistasFiltroLocalizacao = baseDeDados.prepareStatement(
                "SELECT artista.id, nome, descricao, foto, tipo_de_arte, esta_apurado, artista.data  FROM artista, atuacao WHERE artista.id=atuacao.artista AND localizacao=?");
        listarArtistasFiltroTipoDeArte = baseDeDados.prepareStatement("SELECT * FROM artista WHERE tipo_de_arte=?");
        listarArtistasFiltroLocalizacaoTipoDeArte = baseDeDados.prepareStatement(
                "SELECT artista.id, nome, descricao, foto, tipo_de_arte, esta_apurado, artista.data FROM artista, atuacao WHERE artista.id=atuacao.artista AND tipo_de_arte=? AND localizacao=?");
        listarArtistasApurados = baseDeDados.prepareStatement("SELECT * FROM artista WHERE esta_apurado=1");
        listarAstistasNaoApurados = baseDeDados.prepareStatement("SELECT * FROM artista WHERE esta_apurado=0");
        this.mediaDasClassificacoes = baseDeDados.prepareStatement("SELECT AVG(classe) as media FROM classificacao WHERE  artista=?");
    }

    public Artista salvar(Artista artista) {
        try {
            salvarArtista.setString(1, artista.getNome());
            salvarArtista.setString(2, artista.getDescricao());
            salvarArtista.setString(3, artista.getFoto());
            salvarArtista.setInt(4, artista.getTipoDeArte().getId());
            salvarArtista.setInt(5, artista.getEstaApurado() ? 1 : 0);
            salvarArtista.executeUpdate();
            ResultSet rs = salvarArtista.getGeneratedKeys();
            if(rs.next()) {
                return pesquisarPorId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remover(Artista artista) {

    }

    public Artista pesquisarPorId(int artistaID) {
        try {
            encontrarArtista.setInt(1, artistaID);
            ResultSet resultado = encontrarArtista.executeQuery();

            if(resultado.next()) {
                return getArtistaComArte(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Artista pesquisarPorNome(String nome) {
        try {
            encontrarArtistaPorNome.setString(1, nome);
            ResultSet resultado = encontrarArtistaPorNome.executeQuery();
            if(resultado.next()) {
                return getArtistaComArte(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Artista editar(Artista artista) {
        try {
            editarArtista.setString(1, artista.getNome());
            editarArtista.setString(2, artista.getDescricao());
            editarArtista.setString(3, artista.getFoto());
            editarArtista.setInt(4, artista.getTipoDeArte().getId());
            editarArtista.setInt(5, artista.getEstaApurado() ? 1 : 0);
            editarArtista.setInt(6, artista.getId());
            int id = editarArtista.executeUpdate();
            return pesquisarPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Artista> listar() {
        List<Artista> lista = new ArrayList<>();
        try {
            ResultSet resultado = listarArtistas.executeQuery();
            while (resultado.next()) {
                lista.add(getArtistaComArte(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Artista> listar(FiltrosDeArtistas filtros) { 
        ResultSet resultado = null;
        List<Artista> listaDeArtistas = new ArrayList<>();
        try {
            if(filtros.localizacao == null || filtros.localizacao.getId() == 0) {
                if(filtros.tipoDeArte == null || filtros.tipoDeArte.getId() == 0) {
                    return listar();
                } else {
                    listarArtistasFiltroTipoDeArte.setInt(1, filtros.tipoDeArte.getId());
                    resultado = listarArtistasFiltroLocalizacaoTipoDeArte.executeQuery();
                }
            } else {
               if(filtros.tipoDeArte == null || filtros.tipoDeArte.getId() == 0) {
                    listarArtistasFiltroLocalizacao.setInt(1, filtros.localizacao.getId());
                    resultado = listarArtistasFiltroLocalizacao.executeQuery();
                } else {
                    listarArtistasFiltroLocalizacaoTipoDeArte.setInt(1, filtros.tipoDeArte.getId());
                    listarArtistasFiltroLocalizacaoTipoDeArte.setInt(2, filtros.localizacao.getId());
                    resultado = listarArtistasFiltroLocalizacaoTipoDeArte.executeQuery();
                } 
            }
            while( resultado.next() ) {
                Artista artista = getArtistaComArte(resultado);
                listaDeArtistas.add(artista);
                // artista.setId(resultado.getInt("artistaId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDeArtistas;
    }

    public List<Artista> listarArtistasApurados() {
        List<Artista> artistas = new ArrayList<>();
        try {
            ResultSet resultado = listarArtistasApurados.executeQuery();
            while(resultado.next()) {
                artistas.add( getArtistaComArte(resultado) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistas;
    }

    public List<Artista> listarArtistasNaoApurados() {
        List<Artista> artistas = new ArrayList<>();
        try {
            ResultSet resultado = listarAstistasNaoApurados.executeQuery();
            while(resultado.next()) {
                artistas.add(getArtistaComArte(resultado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistas;
    }

    private Artista getArtistaComArte(ResultSet resultado) throws SQLException {
        Artista artista = ServicoDeRecolhaDeDados.recolherArtista(resultado);
        encontrarTipoDeArte.setInt(1, resultado.getInt("tipo_de_arte"));
        ResultSet resultadoArte = encontrarTipoDeArte.executeQuery();
        if(resultadoArte.next()) {
            artista.setTipoDeArte(ServicoDeRecolhaDeDados.recolherTipoDeArte(resultadoArte));
        }
        this.mediaDasClassificacoes.setInt(1, artista.getId());
        ResultSet resultadoClassificacao = this.mediaDasClassificacoes.executeQuery();
        if(resultadoClassificacao.next()) {
            artista.setMediaDasClassificacoes(resultadoClassificacao.getDouble("media"));
        }
        return artista;
    }
}
