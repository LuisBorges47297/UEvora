package me.evora.modelo;

import java.util.Date;

public class TipoDeArte {
    private int id;
    private String descricao;
    private Date dataDeCriacao = new Date();

    public TipoDeArte() {}

    public TipoDeArte(String descricao) {
        this.descricao = descricao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataDeCriacao(Date dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public int getId() {
        return id;
    }

    public Date getDataDeCriacao() {
        return dataDeCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return String.format("%s", descricao);
    }
}
