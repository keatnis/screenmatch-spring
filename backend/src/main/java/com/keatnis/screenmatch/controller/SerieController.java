package com.keatnis.screenmatch.controller;

import com.keatnis.screenmatch.dto.SerieDTO;
import com.keatnis.screenmatch.model.Serie;
import com.keatnis.screenmatch.repository.SerieRepository;
import com.keatnis.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService serieService;
    // endpoints

    @GetMapping()
    public List<SerieDTO> getSeries() {
        return serieService.getAllSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTop5() {
        return serieService.getTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> getLanzamientosRecientes(){
        return serieService.obtenerLanzamientosRecientes();
    }
    @GetMapping("/{id}")
    public SerieDTO getById(@PathVariable Long id){
        return serieService.getById(id);
    }
}
