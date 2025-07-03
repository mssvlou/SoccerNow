package pt.ul.fc.css.soccernow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.entities.Equipa;

@Repository
public interface EquipaRepository extends JpaRepository<Equipa,Long> {
    Optional<Equipa> findByNome(String nome);
}
