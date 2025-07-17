package me.evora.modelo;

import java.util.Date;

public class Localizacao {
    private int id;
    private int latitude;
    private int longitude;
    private Date dataDeCricao = new Date();

    public Localizacao() {}

    public Localizacao(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setDataDeCricao(Date dataDeCricao) {
        this.dataDeCricao = dataDeCricao;
    }


    public int getId() {
        return id;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }


    public Date getDataDeCricao() {
        return dataDeCricao;
    }

    @Override
    public String toString() {
        return String.format("{ Lat: %d, Long: %d }", latitude, longitude);
    }
}