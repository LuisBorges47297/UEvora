package me.evora.modelo;

import java.util.Date;

import me.evora.contants.TipoDeUtilizador;

public class Utilizador {
    private int id;
    private String username;
    private String email;
    private String password;
    private Date dataDeCriacao;
    private TipoDeUtilizador tipo;

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

    public void setTipo(TipoDeUtilizador tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
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

    public Date getDataDeCriacao() {
        return dataDeCriacao;
    }

    public TipoDeUtilizador getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return String.format("%s", username);
    }
}
