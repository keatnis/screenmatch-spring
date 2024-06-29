package com.keatnis.screenmatch.main;

import com.keatnis.screenmatch.model.*;
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
    private Optional<Serie> serieBuscada;

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
                4. Buscar serie por titulo
                5. Top 5 de mejores series
                6. Buscar por categoria
                7. Buscar por Temporadas y Evaluacion
                8. Buscar episodios por titulo
                9. Top 5 de episodios por serie
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
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

    private void buscarSeriePorTitulo() {
        System.out.println("Ingrese el nombre de la serie que desee buscar: ");
        var nombreSerie = scanner.nextLine();
        serieBuscada = serieRepository.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()) {
            System.out.println(serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }
    }

    private void mostrarSeriesBuscadas() {

        // Traemos todas las series guardadas desde la base de datos
        series = serieRepository.findAll();
        //ordenamos por categoria
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = serieRepository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Ingrese la categoria/genero que desee buscar");
        var genero = scanner.nextLine();
        var categoria = Categoria.fromEspaniol(genero);
        List<Serie> seriesPorCategoria = serieRepository.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaYEvaluacion() {
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = scanner.nextDouble();
        scanner.nextLine();
        List<Serie> filtroSeries = serieRepository.buscarPorTemporadasYEvaluacion(totalTemporadas, evaluacion);
        // serieRepository.findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que desees buscar: ");
        var tituloEpisodio = scanner.nextLine();

        List<Episodio> episodios = serieRepository.episodiosPorNombre(tituloEpisodio);
        episodios.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s \n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getSerie().getEvaluacion()));
    }

    private void buscarTop5Episodios() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = serieRepository.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }
}
