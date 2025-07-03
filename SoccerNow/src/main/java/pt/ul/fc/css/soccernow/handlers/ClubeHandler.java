package pt.ul.fc.css.soccernow.handlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.entities.Campeonato;
import pt.ul.fc.css.soccernow.entities.Clube;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.repositories.ClubeRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

@Service
public class ClubeHandler {
    
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private EquipaRepository equipaRepository;
    @Autowired
    private UtilizadorRepository utilizadorRepository;
    @Autowired
    private JogoRepository jogoRepository;

    // E.1. Criação de clubes
    public ClubeDTO createClube(ClubeDTO clubeDTO) {
        String nome = clubeDTO.getNome();
        checkNomeDisponivel(nome);
        
        Clube clube = new Clube(nome);
        clube.putConquista(Clube.Conquista.PRIMEIRO_LUGAR, false);
        clube.putConquista(Clube.Conquista.SEGUNDO_LUGAR, false);
        clube.putConquista(Clube.Conquista.TERCEIRO_LUGAR, false);

        Clube savedClube = clubeRepository.save(clube);
        return toDTO(savedClube);
    }

    // F.1. Verificar, remover e atualizar os clubes
    public ClubeDTO getClube(Long id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        if(clube.isEmpty())
            throw new IllegalArgumentException("Clube não existe");
        return toDTO(clube.get());
    }

    public List<ClubeDTO> getAllClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        return clubes.stream().map(clube -> toDTO(clube)).toList();
    }

    public void deleteClube(Long id) {
        if(clubeRepository.existsById(id)) {
            Clube clube = clubeRepository.findById(id).get();
            
            List<Jogo> jogos = jogoRepository.findAllByEquipasClubeId(id);
            if(jogos.stream().anyMatch(jogo -> jogo.getEstadoAtual() == Jogo.Estado.AGENDADO || jogo.getEstadoAtual() == Jogo.Estado.EM_CURSO))
                throw new IllegalArgumentException("Não é possível apagar um clube que tem equipas com jogos agendados ou em curso");

            List<Equipa> equipas = clube.getEquipas();
            for(Equipa equipa : equipas) {
                List<Jogador> jogadores = equipa.getJogadores();
                for(Jogador jogador : jogadores) {
                    jogador.removeEquipa(equipa);
                    jogador.setClube(null);
                }
                utilizadorRepository.saveAll(jogadores);
            }

            equipaRepository.deleteAll(equipas);
            clubeRepository.deleteById(id);
        }
        else
            throw new IllegalArgumentException("Clube não existe");
    }

    // o único update permitido é o nome, outros updates relativamente ao clube são managed pelos respetivos handlers 
    public ClubeDTO updateClube(ClubeDTO clubeDTO) {
        Clube clube = clubeRepository.findById(clubeDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Clube não existe"));
        
        String nome = clubeDTO.getNome();
        if(nome != null && !clube.getNome().equals(nome)) {
            checkNomeDisponivel(nome);
            clube.setNome(nome);
        }

        clubeRepository.save(clube);
        return toDTO(clube);
    }

    // G.1. Buscar por clubes
    public List<ClubeDTO> filterClubes(String nome, Integer numJogadores, List<String> conquistasStrings, List<String> posicoesAusentesStrings) {
        List<Clube.Conquista> conquistas = conquistasStrings != null 
            ? conquistasStrings.stream().map(Clube.Conquista::valueOf).toList() : null;
        List<Jogador.Posicao> posicoesAusentes = posicoesAusentesStrings != null
            ? posicoesAusentesStrings.stream().map(Jogador.Posicao::valueOf).toList() : null;

        return clubeRepository.findAll().stream()
            .filter(clube -> nome == null || nome.isBlank() || clube.getNome().toLowerCase().contains(nome.toLowerCase()))
            .filter(clube -> numJogadores == null || clube.getJogadores().size() == numJogadores)
            .filter(clube -> {
                if (conquistas == null)
                    return true;
                return conquistas.stream().allMatch(conquista -> Boolean.TRUE.equals(clube.getConquistas().get(conquista)));
            }).filter(clube -> {
                if (posicoesAusentes == null)
                    return true;
                return clube.getJogadores().stream().noneMatch(jogador -> posicoesAusentes.contains(jogador.getPosicao()));
            }).map(this::toDTO)
            .toList();
    }

    // ---------- MÉTODOS AUXILIARES ----------

    private void checkNomeDisponivel(String nome) {
        Optional<Clube> clube = clubeRepository.findByNome(nome);
        if(clube.isPresent())
            throw new IllegalArgumentException("Já existe um clube com esse nome");
    }

    private ClubeDTO toDTO(Clube clube) {
        ClubeDTO dto = new ClubeDTO();

        dto.setId(clube.getId());
        dto.setNome(clube.getNome());
        
        Map<String, Boolean> conquistasDto = clube.getConquistas().entrySet().stream()
            .collect(Collectors.toMap(entry -> entry.getKey().name(), Map.Entry::getValue));
        dto.setConquistas(conquistasDto);

        dto.setJogadorIDs(clube.getJogadores().stream().map(Jogador::getId).toList());
        dto.setEquipaIDs(clube.getEquipas().stream().map(Equipa::getId).toList());
        dto.setCampeonatoIDs(clube.getCampeonatos().stream().map(Campeonato::getId).toList());

        return dto;
    }
}
