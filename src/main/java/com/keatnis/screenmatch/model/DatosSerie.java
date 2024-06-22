package com.keatnis.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//ponemos esta anotacion para que no lea las propiedades desconocidas
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        @JsonAlias("Title")
        String titulo,
        @JsonAlias("totalSeasons")
        String totalTemporadas,
        @JsonAlias("imdbRating")
        String evaluacion) {


}
