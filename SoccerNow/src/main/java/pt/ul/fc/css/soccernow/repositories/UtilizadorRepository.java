package pt.ul.fc.css.soccernow.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Utilizador;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador,Long> {
    Optional<Utilizador> findByEmail(String email);
    List<Jogador> findByClubeId(Long clubeId);
    @Query("SELECT j FROM Jogador j WHERE j.id IN :ids")
    List<Jogador> findJogadoresByIdIn(@Param("ids") List<Long> ids);
    @Query("SELECT j FROM Jogador j WHERE j.clube.id = :clubeID")
    List<Jogador> findJogadoresByClubeId(@Param("clubeID") Long clubeID);
}
