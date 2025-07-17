package com.tw.runningevents.domain.inscricao;


import com.tw.runningevents.domain.eventos.Evento;
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
public class Inscricao {
    private Long id;
    private String nome;
    private char genero;
    private String escalao;
    private String evento_nome;
    private String outros_dados;
    private Date data_inscricao;

    public static class InscricaoRowMapper implements RowMapper<Inscricao> {
        @Override
        public Inscricao mapRow(ResultSet rs, int rowNum) throws SQLException {
            Inscricao inscricao = new Inscricao();
            inscricao.setId(rs.getLong("id"));
            inscricao.setNome(rs.getString("nome"));
            inscricao.setGenero(rs.getString("genero").charAt(0));
            inscricao.setEscalao(rs.getString("escalao"));
            inscricao.setEvento_nome(rs.getString("evento_nome"));
            inscricao.setOutros_dados(rs.getString("outros_dados"));
            inscricao.setData_inscricao(rs.getDate("data_inscricao"));
            return inscricao;
        }
    }
}
