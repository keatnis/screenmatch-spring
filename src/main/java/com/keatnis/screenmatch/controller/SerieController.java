package com.keatnis.screenmatch.controller;

import com.keatnis.screenmatch.dto.SerieDTO;
import com.keatnis.screenmatch.model.Serie;
import com.keatnis.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    @Autowired
    private SerieRepository serieRepository;


    @GetMapping("/series")
    public List<SerieDTO> getSeries() {
        return serieRepository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getGenero(),
                        s.getSinopsis(), s.getPoster(), s.getActores()))
                .collect(Collectors.toList());
    }

}
