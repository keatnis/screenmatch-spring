package com.keatnis.screenmatch.repository;

import com.keatnis.screenmatch.model.Categoria;
import com.keatnis.screenmatch.model.Episodio;
import com.keatnis.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double evaluacion);

    //  @Query(value = "SELECT * FROM serie WHERE serie.total_temporadas >=3 AND serie.evaluacion >=8", nativeQuery = true)
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> buscarPorTemporadasYEvaluacion(int totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios(Serie serie);
}