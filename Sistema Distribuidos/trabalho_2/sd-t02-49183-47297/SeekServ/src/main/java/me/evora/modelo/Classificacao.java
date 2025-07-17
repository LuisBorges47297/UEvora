package me.evora.modelo;

import java.util.Date;

public class Classificacao {
    private int id;
    private double classe;
    private Artista artista;
    private String comentario;
    private Utilizador oferecedor;
    private Date dataDeDoacao = new Date();

    public Classificacao() {}

    public Classificacao(double classe, Artista artista, Utilizador oferecedor) {
        this.classe = classe;
        this.artista = artista;
        this.oferecedor = oferecedor;
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public void setClasse(double classe) {
        this.classe = classe;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public double getClasse() {
        return classe;
    }

    public String getComentario() {
        return comentario;
    }

    public Date getDataDeDoacao() {
        return dataDeDoacao;
    }

    public Utilizador getOferecedor() {
        return oferecedor;
    }

}
