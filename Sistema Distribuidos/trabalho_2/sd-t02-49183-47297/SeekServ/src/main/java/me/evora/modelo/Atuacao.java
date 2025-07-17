package me.evora.modelo;

import java.util.Date;

public class Atuacao {
    private int id;
    private Localizacao localizacao;
    private boolean eAtual;
    private Date dataDeAtuacao = new Date();
    private Artista artista;

    public Atuacao() {}

    public Atuacao(Localizacao localizacao, Artista artista) {
        this.localizacao = localizacao;
        this.artista = artista;
        this.eAtual = true;
    }

    public Atuacao(Localizacao localizacao, Date dataDeAtuacao) {
        this.localizacao = localizacao;
        this.dataDeAtuacao = dataDeAtuacao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public void seteAtual(boolean atuacaoAtual) {
        this.eAtual = atuacaoAtual;
    }

    public void setDataDeAtuacao(Date dataDeAtuacao) {
        this.dataDeAtuacao = dataDeAtuacao;
    }

    public int getId() {
        return id;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public Date getDataDeAtuacao() {
        return dataDeAtuacao;
    }

    public boolean getEAtual() {
        return eAtual;
    } 

    public Artista getArtista() {
        return artista;
    }
}
