package me.evora.modelo;

import java.util.Date;

import me.evora.contants.TipoDeUtilizador;

public class Utilizador {
    private int id;
    private String username;
    private String email;
    private String password;
    private TipoDeUtilizador tipo;
    private Date dataDeCriacao;

    public Utilizador() {}

    public Utilizador(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setTipo(TipoDeUtilizador tipo) {
        this.tipo = tipo;
    }

    public void setDataDeCriacao(Date dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }
    
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public TipoDeUtilizador getTipo() {
        return tipo;
    }

    public Date getDataDeCriacao() {
        return dataDeCriacao;
    }
}
