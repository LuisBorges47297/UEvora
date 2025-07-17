package me.evora.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Artista {
    private int id;
    private String nome;
    private String descricao;
    private String foto;
    private TipoDeArte tipoDeArte;
    private List<Atuacao> atuacoes;
    private Atuacao atuacaoAtual;
    private boolean estaApurado;
    private double classificacao;
    private List<Donativo> donativos;
    private Date dataDeCriacao = new Date();

    public Artista() { 
        this.atuacoes = new ArrayList<>(); 
    }

    public Artista(String nome, TipoDeArte tipoDeArte, String descricao, String foto) {
        this.nome = nome;
        this.tipoDeArte = tipoDeArte;
        this.atuacoes = new ArrayList<>();
        this.estaApurado = false;
        this.donativos = new ArrayList<>();
        this.descricao = descricao;
        this.foto = foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setTipoDeArte(TipoDeArte tipoDeArte) {
        this.tipoDeArte = tipoDeArte;
    }

    public void setEstaApurado(boolean apurado) {
        this.estaApurado = apurado;
    }

    public void setAtuacaoAtual(Atuacao atuacaoAtual) {
        this.atuacaoAtual = atuacaoAtual;
    }

    public void setClassificacao(double mediaDasClassificacoes) {
        this.classificacao = mediaDasClassificacoes;
    }

    public void setDataDeCriacao(Date dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFoto() {
        return foto;
    }

    public TipoDeArte getTipoDeArte() {
        return tipoDeArte;
    }

    public Date getDataDeCriacao() {
        return dataDeCriacao;
    }

    public boolean getEstaApurado() {
        return this.estaApurado;
    }

    public List<Atuacao> getAtuacoes() {
        return atuacoes;
    }

    public List<Donativo> getDonativos() {
        return donativos;
    }

    public double getClassificacao() {
        return classificacao;
    }

    public Atuacao getAtuacaoAtual() {
        return atuacaoAtual;
    }

    @Override
    public String toString() {
        return String.format("#%02d %s {%.02f}", id, nome, classificacao);
    }
}
