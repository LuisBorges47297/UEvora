package com.tw.runningevents.domain.tempos_participantes;

import com.tw.runningevents.domain.inscricao.Inscricao;
import lombok.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tempos_participantes {
    private String nome_evento;
    private String local;
    private Long numero_participante;
    private String ponto;
    private Date data_hora;
    public static class Tempos_participantesRowMapper implements RowMapper<Tempos_participantes> {
        @Override
        public Tempos_participantes mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tempos_participantes tempos_participantes = new Tempos_participantes();
            tempos_participantes.setNome_evento(rs.getString("nome_evento"));
            tempos_participantes.setLocal(rs.getString("Local"));
            tempos_participantes.setNumero_participante(rs.getLong("numero_participante"));
            tempos_participantes.setPonto(rs.getString("ponto"));
            tempos_participantes.setData_hora(rs.getTimestamp("data_hora"));

            return tempos_participantes;
        }
    }
}
