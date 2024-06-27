package com.keatnis.screenmatch.main;

import com.keatnis.screenmatch.model.DatosSerie;
import com.keatnis.screenmatch.model.DatosTemporada;
import com.keatnis.screenmatch.model.Episodio;
import com.keatnis.screenmatch.model.Serie;
import com.keatnis.screenmatch.repository.SerieRepository;
import com.keatnis.screenmatch.service.ConsumoAPI;
import com.keatnis.screenmatch.service.ConvierteDatos;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=57e2a7ad";
    private List<DatosSerie> serieList = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
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
        mostrarSeriesBuscadas();
        System.out.println("Escriba el nombre de la serie que desee buscar: ");
        var nombreSerie = scanner.nextLine();
        // buscamos la series guardadas en la base de datos

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i < serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(URL_BASE + URLEncoder.encode(serieEncontrada.getTitulo()) + "&Season=" + i +
                        API_KEY);   

                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            //vamos a convertir la lista de temporadas a lista de episodios

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        } else {
            System.out.println("serie no encontrada");
        }


    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //serieList.add(datos);
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {

        // Traemos todas las series guardadas desde la base de datos
        series = serieRepository.findAll();
        //ordenamos por categoria
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

}
