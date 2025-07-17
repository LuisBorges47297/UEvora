package me.evora.modelo;

import java.util.Date;

public class Donativo {
    private int id;
    private double valor;
    private Artista artista;
    private Utilizador oferecedor;
    private Date dataDeDoacao = new Date();

    public Donativo() {}

    public Donativo(double valor, Artista artista, Utilizador oferecedor) {
        this.valor = valor;
        this.artista = artista;
        this.oferecedor = oferecedor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setOferecedor(Utilizador oferecedor) {
        this.oferecedor = oferecedor;
    }

    public void setDataDeDoacao(Date dataDeDoacao) {
        this.dataDeDoacao = dataDeDoacao;
    }

    public int getId() {
        return id;
    }

    public Artista getArtista() {
        return artista;
    }

    public Date getDataDeDoacao() {
        return dataDeDoacao;
    }

    public double getValor() {
        return valor;
    }

    public Utilizador getOferecedor() {
        return oferecedor;
    }
}
