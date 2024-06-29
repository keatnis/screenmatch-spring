package com.keatnis.screenmatch.service;

import com.keatnis.screenmatch.dto.SerieDTO;
import com.keatnis.screenmatch.model.Serie;
import com.keatnis.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> getAllSeries() {
        return convertirDatos(serieRepository.findAll());
    }

    public List<SerieDTO> getTop5() {
        return convertirDatos(serieRepository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerLanzamientosRecientes() {
        return convertirDatos(serieRepository.lanazamientosRecientes());
    }

    //metodo para reutilizar codigo para tratar los datos
    public List<SerieDTO> convertirDatos(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getGenero(),
                        s.getSinopsis(), s.getPoster(), s.getActores()))
                .collect(Collectors.toList());
    }

    public SerieDTO getById(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getGenero(),
                    s.getSinopsis(), s.getPoster(), s.getActores());
        }
        return null;
    }
}
