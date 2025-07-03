package pt.ul.fc.css.soccernow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.entities.Jogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findAllByEquipasClubeId(Long clubeId);
}
