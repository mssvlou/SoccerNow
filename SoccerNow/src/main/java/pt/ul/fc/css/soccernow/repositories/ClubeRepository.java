package pt.ul.fc.css.soccernow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.entities.Clube;

@Repository
public interface ClubeRepository extends JpaRepository<Clube,Long> {
    Optional<Clube> findByNome(String nome);
}
