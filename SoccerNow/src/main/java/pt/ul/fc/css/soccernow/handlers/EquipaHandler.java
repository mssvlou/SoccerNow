package pt.ul.fc.css.soccernow.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.entities.Clube;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.repositories.ClubeRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

@Service
public class EquipaHandler {

    private static final int NUMBER_OF_PLAYERS = 5;

    @Autowired
    private EquipaRepository equipaRepository;
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private UtilizadorRepository utilizadorRepository;

    // E.2. Criação de equipas
    public EquipaDTO createEquipa(EquipaDTO equipaDTO) {
        Clube clube = clubeRepository.findById(equipaDTO.getClubeID())
            .orElseThrow(() -> new IllegalArgumentException("Clube não existe"));

        String nome = equipaDTO.getNome();
        checkNomeDisponivel(nome);
        
        List<Jogador> jogadores = utilizadorRepository.findJogadoresByIdIn(equipaDTO.getJogadorIDs());
        
        try {
            isValidJogadores(jogadores, clube);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        Equipa equipa = equipaRepository.save(new Equipa(equipaDTO.getNome(), clube, jogadores));
        equipaDTO.setId(equipa.getId());

        for (Jogador jogador : jogadores)
            jogador.addEquipa(equipa);
        utilizadorRepository.saveAll(jogadores);
        
        clube.addEquipa(equipa);
        clubeRepository.save(clube);

        return toDTO(equipa);
    }

    // F.2. Verificar, remover e atualizar as equipas
    public EquipaDTO getEquipa(Long id) {
        Optional<Equipa> equipa = equipaRepository.findById(id);
        if(equipa.isEmpty())
            throw new IllegalArgumentException("Equipa não existe");
        return toDTO(equipa.get());
    }

    public List<EquipaDTO> getAllEquipas() {
        List<Equipa> equipas = equipaRepository.findAll();
        return equipas.stream().map(equipa -> toDTO(equipa)).toList();
    }

    public void deleteEquipa(Long id) {
        if(equipaRepository.existsById(id)) {
            Equipa equipa = equipaRepository.findById(id).get();

            if(equipa.getJogos().stream().anyMatch(jogo -> jogo.getEstadoAtual() == Jogo.Estado.AGENDADO || jogo.getEstadoAtual() == Jogo.Estado.EM_CURSO))
                throw new IllegalArgumentException("Não é possível apagar uma equipa com jogos agendados ou em curso");
            
            List<Jogador> jogadores = equipa.getJogadores();
            for(Jogador jogador : jogadores) 
                jogador.removeEquipa(equipa);
            utilizadorRepository.saveAll(jogadores);

            Clube clube = equipa.getClube();
            clube.removeEquipa(equipa);
            clubeRepository.save(clube);

            equipaRepository.deleteById(id);
        }
        else
            throw new IllegalArgumentException("Equipa não existe");
        
    }

    public EquipaDTO updateEquipa(EquipaDTO equipaDTO) {
        Equipa equipa = equipaRepository.findById(equipaDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Equipa não existe"));

        // se DTO tiver nome, update
        String nome = equipaDTO.getNome();
        if(nome != null && !equipa.getNome().equals(nome)) {
            checkNomeDisponivel(nome);
            equipa.setNome(equipaDTO.getNome());
        }

        // se DTO tiver jogadores, update
        if(equipaDTO.getJogadorIDs() != null) {
            List<Jogador> newJogadores = utilizadorRepository.findJogadoresByIdIn(equipaDTO.getJogadorIDs());
            try {
                updateEquipaJogadores(equipa, newJogadores);
                equipa.setJogadores(newJogadores);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        // update jogos nas equipas será feito no caso de uso H
        equipaDTO.setJogoIDs(equipa.getJogos().stream().map(Jogo::getId).toList());

        equipaRepository.save(equipa);
        return toDTO(equipa);
    }

    // G.2. Buscar por equipas
    public List<EquipaDTO> filterEquipas(String nome, Integer numVitorias, Integer numEmpates, Integer numDerrotas, List<String> posicoesAusentesStrings) {
        List<Jogador.Posicao> posicoesAusentes = posicoesAusentesStrings != null
            ? posicoesAusentesStrings.stream().map(Jogador.Posicao::valueOf).toList() : null;

        return equipaRepository.findAll().stream()
            .filter(equipa -> nome == null || nome.isBlank() || equipa.getNome().toLowerCase().contains(nome.toLowerCase()))
            .filter(equipa -> numVitorias == null || equipa.getNumVitorias() == numVitorias)
            .filter(equipa -> numEmpates == null || equipa.getNumEmpates() == numEmpates)
            .filter(equipa -> numDerrotas == null || equipa.getNumDerrotas() == numDerrotas)
            .filter(equipa -> {
                if (posicoesAusentes == null)
                    return true;
                return equipa.getJogadores().stream().noneMatch(jogador -> posicoesAusentes.contains(jogador.getPosicao()));
            }).map(this::toDTO)
            .toList();
    }

    // ---------- MÉTODOS AUXILIARES ----------

    private void checkNomeDisponivel(String nome) {
        if(equipaRepository.findByNome(nome).isPresent())
            throw new IllegalArgumentException("Já existe uma equipa com esse nome");
    }

    private void isValidJogadores(List<Jogador> jogadores, Clube clube) {
        // equipa tem o nº correto de jogadores?
        if(jogadores.size() != NUMBER_OF_PLAYERS)
            throw new IllegalArgumentException(String.format("Equipa deve ter %d jogadores", NUMBER_OF_PLAYERS));
        
        // equipa tem jogadores repetidos?
        if (new HashSet<>(jogadores).size() != NUMBER_OF_PLAYERS)
            throw new IllegalArgumentException("Equipa não pode ter jogadores repetidos");

        // equipa tem um guarda-redes?
        if(!jogadores.stream().anyMatch(jogador -> jogador.getPosicao().equals(Jogador.Posicao.GUARDA_REDES)))
            throw new IllegalArgumentException("Equipa deve ter um guarda-redes");

        // todos os jogadores pertencem ao clube da equipa?
        if(jogadores.stream().filter(jogador -> jogador.getClube() != clube).count() > 0)
            throw new IllegalArgumentException("Todos os jogadores devem pertencer ao clube da equipa");
    }

    private void updateEquipaJogadores(Equipa equipa, List<Jogador> newJogadores) {
        isValidJogadores(newJogadores, equipa.getClube()); // exceptions apanhadas em updateEquipa

        // há algum jogo em curso?
        if(equipa.getJogos().stream().anyMatch(jogo -> jogo.getEstadoAtual() == Jogo.Estado.EM_CURSO))
            throw new IllegalArgumentException("Não é possível substituir jogadores em campo");

        for(Jogador oldJogador : equipa.getJogadores())
            if(!newJogadores.contains(oldJogador)) { // foi substituido, remover esta equipa da sua lista
                oldJogador.removeEquipa(equipa);
                utilizadorRepository.save(oldJogador);
            }

        for(Jogador newJogador : newJogadores)
            if(!newJogador.getEquipas().contains(equipa)) { // jogador ainda não tem esta equipa guardada
                newJogador.addEquipa(equipa);
                utilizadorRepository.save(newJogador);
            }
    }

    private EquipaDTO toDTO(Equipa equipa) {
        EquipaDTO dto = new EquipaDTO();

        dto.setId(equipa.getId());
        dto.setNome(equipa.getNome());
        dto.setNumVitorias(equipa.getNumVitorias());
        dto.setNumEmpates(equipa.getNumEmpates());
        dto.setNumDerrotas(equipa.getNumDerrotas());
        dto.setClubeID(equipa.getClube().getId());
        dto.setJogadorIDs(equipa.getJogadores().stream().map(Jogador::getId).toList());
        dto.setJogoIDs(equipa.getJogos().stream().map(Jogo::getId).toList());

        return dto;
    }
}
