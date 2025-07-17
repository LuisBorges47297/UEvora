package com.tw.runningevents.domain.eventos;

import lombok.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Evento {
  //  private Long id;

    private String nome;
    private String local;
    private char genero;
    private String organizador;
    private int contacto;
    private Date data;
    private float valor_inscricao;

    public static class EventosRowMapper implements RowMapper<Evento> {
        @Override
        public Evento mapRow(ResultSet rs, int rowNum) throws SQLException{
            Evento evento = new Evento();
            evento.setNome(rs.getString("nome"));
            evento.setLocal(rs.getString("local"));
            evento.setGenero(rs.getString("genero").charAt(0));
            evento.setOrganizador(rs.getString("organizador"));
            evento.setContacto(rs.getInt("contacto"));
            evento.setData(rs.getDate("data"));
            evento.setValor_inscricao(rs.getInt("valor_inscricao"));
            return evento;

        }
    }
}
