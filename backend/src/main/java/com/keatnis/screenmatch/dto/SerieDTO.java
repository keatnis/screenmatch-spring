package com.keatnis.screenmatch.dto;


import com.keatnis.screenmatch.model.Categoria;

public record SerieDTO(
        Long id,
        String titulo,

        Integer totalTemporadas,

        Double evaluacion,

        Categoria genero,

        String sinopsis,

        String poster,

        String actores
) {
}
