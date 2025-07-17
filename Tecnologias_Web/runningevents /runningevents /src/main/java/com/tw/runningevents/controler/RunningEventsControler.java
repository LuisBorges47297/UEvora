package com.tw.runningevents.controler;

import com.tw.runningevents.domain.eventos.Evento;
import com.tw.runningevents.domain.eventos.Evento.EventosRowMapper;
import com.tw.runningevents.domain.inscricao.Inscricao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Controller
public class RunningEventsControler {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Home
    @GetMapping("/")
    public String pageHome(Model model){
        return "index";
    }

    @GetMapping("/eventos/time")
    public String pageTime(Model model){
        return "time";
    }

    // Search Events
    @GetMapping("/eventos/search")
    public String pageSearch(){
        return "search";
    }

//    // Search Events
//    @GetMapping("/eventos/{id}")
//    public String pageEvents(){
//        return "participantes";
//    }

    @GetMapping("/eventos/eventsearch")
    public String enventSearch(@RequestParam(name="nomeEvento", required = false) String nome,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date data,
                                Model model){
        if(data != null){
            String query = "SELECT * FROM Eventos WHERE LOWER(nome) LIKE '%" + nome.toLowerCase() + "%' AND data = '" + data + "' ORDER BY data ASC";
            List<Evento> eventos = jdbcTemplate.query(query, new EventosRowMapper());
            model.addAttribute("eventList", eventos);
        }else{
            String query = "SELECT * FROM Eventos WHERE LOWER(nome) LIKE '%" + nome.toLowerCase() + "%'ORDER BY data ASC";
            List<Evento> eventos = jdbcTemplate.query(query, new EventosRowMapper());
            model.addAttribute("eventList", eventos);
        }
        return "search";
    }

    //criar evento
    @GetMapping("/eventos/create")
    public String pageCreate (){
        return "create";
    }

    //fazer inscricoes
    @GetMapping("/eventos/registrations")
    public String pageCreateRegistrations(){
        return "registrations";
    }

    @PostMapping("/eventos/createEvents")
    public String createEvents(@RequestParam(name = "evento", required = true) String evento,
                               @RequestParam(name = "local", required = true) String local,
                               @RequestParam(name = "descricao", required = true) String descricao,
                               @RequestParam(name = "genero", required = true) char genero,
                               @RequestParam(name = "organizador", required = true) String organizador,
                               @RequestParam(name = "contacto", required = true) int contacto,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data,
                               @RequestParam("hora") LocalTime horario,
                               @RequestParam(name = "valor_inscricao", required = true) int valor_inscricao,
                               Model model) throws Exception{

        String query = "INSERT INTO eventos(" +
                "nome, local, descricao, genero, organizador, contacto, data, horario, valor_inscricao)" +
                "VALUES ('"+ evento +"', '"+ local +"','"+ descricao+"', '"+ genero +"', '"+ organizador +"', '"+contacto+"','"+data+"', '"+horario+"', '"+valor_inscricao+"');";
        jdbcTemplate.update(query);

        return "redirect:/";
    }


    @PostMapping("/eventos/createRegistrations")
    public String createInscricao(@RequestParam(name = "nome", required = true) String nome,
                                  @RequestParam(name = "genero", required = true) char genero,
                                  @RequestParam(name = "escalao", required = true) String escalao,
                                  @RequestParam(name = "nome_evento", required = true) String evento_nome,
                                  @RequestParam(name = "outros_dados", required = true) String outros_dados,
                                  Model model) throws Exception{

            Date data_inscricao = new Date();
            
            String query = "INSERT INTO Inscricao(" +
                "nome, genero, escalao, evento_nome, outros_dados, data_inscricao)" +
                "VALUES ('"+ nome+"', '"+ genero +"','"+ escalao+"', '"+ evento_nome +"', '"+ outros_dados +"', '"+data_inscricao+"');";
        jdbcTemplate.update(query);

        return "redirect:/";
    }

    @PostMapping("/eventos/registrationTime")
    public String registrationTime(@RequestParam(name = "nome_evento", required = true) String nome,
                                   @RequestParam(name = "local", required = true) String local,
                                   @RequestParam(name = "numeroParticipante", required = true) long numero,
                                   @RequestParam(name = "ponto", required = true) String ponto,
                                   Model model) throws Exception{

        Date data_hora = new Date();

        String query = "INSERT INTO tempos_participantes(" +
                "nome_evento, local, numero_participante, ponto, data_hora)" +
                "VALUES ('"+ nome+"', '"+ local +"','"+ numero+"', '"+ ponto +"', '"+ data_hora +"');";
        jdbcTemplate.update(query);

        return "redirect:/eventos/time";
    }

    // mostrar participantes
    @GetMapping("/eventos/{nome}")
    public String pageParticipants(@PathVariable String nome, Model model) {
        String query = "SELECT * FROM Inscricao WHERE evento_nome LIKE ?";
        List<Inscricao> participantes = jdbcTemplate.query(query, new Object[]{nome}, new Inscricao.InscricaoRowMapper());
        model.addAttribute("participantes", participantes);

        return "participantes";
    }
}
