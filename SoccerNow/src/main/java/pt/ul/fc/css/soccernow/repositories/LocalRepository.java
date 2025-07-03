package pt.ul.fc.css.soccernow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.entities.Local;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByNome(String nome);
    Optional<Local> findByMorada(String morada);
    Optional<Local> findByCodigoPostal(String codigoPostal);
}
