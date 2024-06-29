package com.keatnis.screenmatch;

import com.keatnis.screenmatch.main.Menu;
import com.keatnis.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@SpringBootApplication
//public class ScreenmatchApplicationConsole implements CommandLineRunner {
//
//    @Autowired
//    private SerieRepository serieRepository;
//    public static void main(String[] args) {
//
//        SpringApplication.run(ScreenmatchApplicationConsole.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
////        Main main = new Main();
////        main.mostrarMenu();
//        Menu menu = new Menu(serieRepository);
//        menu.mostrarMenu();
////        EjemploStream ejemploStream = new EjemploStream();
////        ejemploStream.mostrarEjemplos();
//
//    }
//}
