package me.evora.servicos;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Classificacao;
import me.evora.modelo.Donativo;
import me.evora.modelo.Localizacao;
import me.evora.modelo.TipoDeArte;
import me.evora.modelo.Utilizador;

public class ServicoDeRecolhaDeDados {
    public static Artista recolherArtista(ResultSet resultado) throws SQLException {
        Artista artista = new Artista();
        artista.setId(resultado.getInt("id"));
        artista.setNome(resultado.getString("nome"));
        artista.setDescricao(resultado.getString("descricao"));
        artista.setFoto(resultado.getString("foto"));
        artista.setEstaApurado(resultado.getInt("esta_apurado") == 1);
        artista.setDataDeCriacao(resultado.getDate("data"));
        return artista;
    }

    public static TipoDeArte recolherTipoDeArte(ResultSet resultado) throws SQLException {
        TipoDeArte tipoDeArte = new TipoDeArte();
        tipoDeArte.setId(resultado.getInt("id"));
        tipoDeArte.setDataDeCriacao(resultado.getDate("data_criacao"));
        tipoDeArte.setDescricao(resultado.getString("descricao"));
        return tipoDeArte;
    }

    public static Atuacao recolherAtuacao(ResultSet resultado) throws SQLException {
        Atuacao atuacao = new Atuacao();
        atuacao.setId(resultado.getInt("id"));
        atuacao.setDataDeAtuacao(resultado.getDate("data"));
        atuacao.seteAtual(resultado.getInt("atuando") == 1);
        return atuacao;
    }

    public static Localizacao recolherLocalizacao(ResultSet resultado) throws SQLException {
        Localizacao localizacao = new Localizacao();
        localizacao.setId(resultado.getInt("id"));
        localizacao.setLatitude(resultado.getInt("latitude"));
        localizacao.setLongitude(resultado.getInt("longitude"));
        return localizacao;
    }

    public static Donativo recolherDonativo(ResultSet resultado) throws SQLException {
        Donativo donativo = new Donativo();
        donativo.setId( resultado.getInt("id") );
        donativo.setValor( resultado.getDouble("valor") );
        donativo.setDataDeDoacao( resultado.getDate("data") );
        return donativo;
    }

    public static Classificacao recolherClassificacao(ResultSet resultado) throws SQLException {
        Classificacao classificacao = new Classificacao();
        classificacao.setId( resultado.getInt("id") );
        classificacao.setClasse( resultado.getDouble("classe") );
        classificacao.setComentario( resultado.getString("comentario") );
        return classificacao;
    }

    public static Utilizador recolherUtilizador(ResultSet resultado) throws SQLException {
        Utilizador utilizador = new Utilizador();
        utilizador.setId(resultado.getInt("id"));
        utilizador.setEmail(resultado.getString("email"));
        utilizador.setUsername(resultado.getString("username"));
        utilizador.setPassword(resultado.getString("password"));
        utilizador.setTipo(TipoDeUtilizador.valueOf(resultado.getString("tipo")));
        utilizador.setDataDeCriacao(resultado.getDate("data"));
        return utilizador;
    }
}
