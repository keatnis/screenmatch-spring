package com.keatnis.screenmatch.main;

import com.keatnis.screenmatch.model.DatosSerie;
import com.keatnis.screenmatch.model.DatosTemporada;
import com.keatnis.screenmatch.model.Serie;
import com.keatnis.screenmatch.repository.SerieRepository;
import com.keatnis.screenmatch.service.ConsumoAPI;
import com.keatnis.screenmatch.service.ConvierteDatos;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=57e2a7ad";
    private List<DatosSerie> serieList = new ArrayList<>();

    private SerieRepository serieRepository;

    public Menu(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void mostrarMenu() {

        int opcion = -1;
        var menu = """
                Opciones: 
                1. Buscar serie
                2. Buscar episodio por serie
                3. Mostrar series buscadas
                0. Salir
                """;
        while (opcion != 0) {
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Cerrando aplicacion ... ");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        }


    }

    private DatosSerie getDatosSerie() {
        System.out.println("Ingrese el nombre de la serie que desee buscar: ");
        var nombreSerie = scanner.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i < datosSerie.totalTemporadas(); i++) {
            var json = consumoAPI.obtenerDatos(URL_BASE + URLEncoder.encode(datosSerie.titulo()) + "&Season=" + i +
                    API_KEY);

            DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);

    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //serieList.add(datos);
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
//        serieList.forEach(System.out::println);
//        List<Serie> series = new ArrayList<>();
//        // convertimos la List de Recors y los datos de este en una clase Java de tipo Serie
//        series = serieList.stream()
//                .map(s -> new Serie(s))
//                .collect(Collectors.toList());
//        //ordenamos por categoria
//        series.stream()
//                .sorted(Comparator.comparing(Serie::getGenero))
//                .forEach(System.out::println);

        // Traemos todas las series guardadas desde la base de datos
        List<Serie> series = serieRepository.findAll();
        //ordenamos por categoria
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

}
