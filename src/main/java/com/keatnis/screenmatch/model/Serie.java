package com.keatnis.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;
import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private Categoria genero;
    private String sinopsis;
    private String poster;
    private String actores;

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        // en caso de que no logre hacer la conversion de la evaluacion
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        // {Action, Crime,Comedy } usando split para separar por coma y solo el primer elemento[0]
        // usando trim() para que no  traiga ningun valor vacio
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
       // para usar chatGPT y traducir
        // this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis())
        this.sinopsis = datosSerie.sinopsis();
        this.poster = datosSerie.poster();
        this.actores = datosSerie.actores();

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    @Override
    public String toString() {
        return  " genero=" + genero +
                " titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", sinopsis='" + sinopsis + '\'' +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' ;
    }
}
