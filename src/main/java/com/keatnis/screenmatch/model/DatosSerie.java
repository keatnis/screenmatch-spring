package com.keatnis.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//ponemos esta anotacion para que no lea las propiedades desconocidas
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        @JsonAlias("Title")
        String titulo,
        @JsonAlias("totalSeasons")
        Integer totalTemporadas,
        @JsonAlias("imdbRating")
        String evaluacion,
        @JsonAlias("Genre")
        String genero,
        @JsonAlias("Plot")
        String sinopsis,
        @JsonAlias("Poster")
        String poster,
        @JsonAlias("Actors")
        String actores

) {


}
