package service.data.repositories;

import service.data.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Kenny Bastani
 *
 * The Movie repository exposes a collection of movie records with their genres
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
