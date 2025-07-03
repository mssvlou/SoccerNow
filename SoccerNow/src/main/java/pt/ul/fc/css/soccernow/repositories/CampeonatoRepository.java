package pt.ul.fc.css.soccernow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.entities.Campeonato;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato,Long> {
    Optional<Campeonato> findByNome(String nome);
}
