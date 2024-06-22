package com.keatnis.screenmatch.main;

import com.keatnis.screenmatch.model.DatosEpisodio;
import com.keatnis.screenmatch.model.DatosSerie;
import com.keatnis.screenmatch.service.ConsumoAPI;
import com.keatnis.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args)  {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String apikey = "57e2a7ad";
		String url = "https://www.omdbapi.com/?t=" + URLEncoder.encode("Game of thrones ") + "&Season"+"&episode=1"+
				"&apikey=" + apikey;
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obtenerDatos(url);

		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);
		var datosEpisodio = conversor.obtenerDatos(json, DatosEpisodio.class);
		System.out.println(datosEpisodio);

	}
}
