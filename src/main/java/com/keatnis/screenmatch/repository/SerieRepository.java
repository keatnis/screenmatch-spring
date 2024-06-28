package com.keatnis.screenmatch.repository;

import com.keatnis.screenmatch.model.Categoria;
import com.keatnis.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    // buscar por titulo, este debe ser el mismo que el nombre de la variable de la calase serie
    // querys deribadas
    Optional<Serie> findByTituloContainsIgnoreCase(String nombre);
    //con las consultas derivadas creamos el siguiente metodo para obtener el top 5 en orden decreciente
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    // usamos el mismo atributo de la clase serie con uppelcase
    List<Serie> findByGenero(Categoria categoria);
    //filtrando series por varios requisitos
    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer totalTemporadas,Double evaluacion);
}