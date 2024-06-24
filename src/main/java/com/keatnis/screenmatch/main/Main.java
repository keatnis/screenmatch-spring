package com.keatnis.screenmatch.main;

import com.keatnis.screenmatch.model.DatosEpisodio;
import com.keatnis.screenmatch.model.DatosSerie;
import com.keatnis.screenmatch.model.DatosTemporada;
import com.keatnis.screenmatch.model.Episodio;
import com.keatnis.screenmatch.service.ConsumoAPI;
import com.keatnis.screenmatch.service.ConvierteDatos;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    //constante: valor que no va a cambiar
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=57e2a7ad";

    public void mostrarMenu() {
        System.out.println("Por favor escribe, el nombre de la serie que desees buscar");
        //busca los datos generales de la serie
        var nombreSerie = teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i < datos.totalTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(URL_BASE + URLEncoder.encode(nombreSerie) + "&Season=" + i +
                    API_KEY);

            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
       // temporadas.forEach(System.out::println);
        // usando funciones lambas, que simplifica el uso del los fori
        // (argumentos) -> { cuerpo-de-la-función }
        // (a, b) -> { return a + b; }
        /*
        otro ejemplo del uso de lambas
        List<Integer> lista = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        lista.stream().filter(i -> i % 2 == 0).forEach(System.out::println);


        imprime la informacionde cada episodio de todas las temporadas
        temporadas.forEach(t -> {
            if (t.episodios() != null) {
                t.episodios().forEach(e -> System.out.println(e.titulo()));
            }
        */

        // convertir todas las informaciones a una lista del tipo DatosEpisodio
        //se usa collect(Collectors.toList()); para la lista porque la lista
            List<DatosEpisodio> datosEpisodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream())
                    .collect(Collectors.toList());

        System.out.println("top 5 capitulos ");
        // ordenamos la lista delos datos del episodio
        //usando sortde para ordenar por evaluacion y reversed para ordenar de mayor a menor
        datosEpisodios.stream()
                //usamos filter para filtar e ignorar los capitulos que no tengan evaluacion "N/A"
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);

        // convertimos los datos de un episodio a un episodio nuevo,a una lista mutable
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);


}
}
