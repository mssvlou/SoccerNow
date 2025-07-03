package pt.ul.fc.css.soccernow.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.mappers.UtilizadorMapper;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Utilizador;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

@Service
public class UtilizadorHandler {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    public UtilizadorDTO autenticar(String email, String password) {
        Optional<Utilizador> opt = utilizadorRepository.findByEmail(email);

        if (opt.isEmpty()) {
            throw new RuntimeException("Email não encontrado.");
        }

        Utilizador u = opt.get();
        if (u instanceof Jogador)
            return UtilizadorMapper.jogadorToDTO((Jogador) u);
        else
            return UtilizadorMapper.arbitroToDTO((Arbitro) u);
    }

    // Método para obter um utilizador pelo ID
    public UtilizadorDTO obterUtilizador(Long id) {
        Utilizador u = utilizadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        if (u instanceof Jogador jogador) 
            return UtilizadorMapper.jogadorToDTO(jogador);
        else 
            return UtilizadorMapper.arbitroToDTO((Arbitro) u);
    }

    public List<UtilizadorDTO> getAllUtilizadores() {
        List<UtilizadorDTO> utilizadores = new ArrayList<>();
        for(Utilizador u : utilizadorRepository.findAll()) {
            if (u instanceof Jogador jogador) 
                utilizadores.add(UtilizadorMapper.jogadorToDTO(jogador));
            else 
                utilizadores.add(UtilizadorMapper.arbitroToDTO((Arbitro) u));
        }
        return utilizadores;
    }
}
