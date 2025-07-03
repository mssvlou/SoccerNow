package pt.ul.fc.css.soccernow.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.entities.Campeonato;
import pt.ul.fc.css.soccernow.entities.Clube;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.repositories.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositories.ClubeRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;

@Service
public class CampeonatoHandler {

    private static final int MINIMUM_CLUBES = 8;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private JogoRepository jogoRepository;

    // J. Criação de campeonatos
    public CampeonatoDTO createCampeonato(CampeonatoDTO campeonatoDTO) {
        String nome = campeonatoDTO.getNome();
        if (campeonatoRepository.findByNome(nome).isPresent())
            throw new IllegalArgumentException("Já existe um campeonato com esse nome");

        Campeonato.Formato formato;
        try {
            formato = Campeonato.Formato.valueOf(campeonatoDTO.getFormato());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato inválido");
        }

        List<Clube> clubes = new ArrayList<>();
        for (Long clubeID : campeonatoDTO.getClubes()) {
            Clube clube = clubeRepository.findById(clubeID)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("O clube %d não existe", clubeID)));
            clubes.add(clube);
        }

        if (clubes.size() < MINIMUM_CLUBES)
            throw new IllegalArgumentException(
                    String.format("O campeonato deve ter pelo menos %d clubes", MINIMUM_CLUBES));

        Campeonato campeonato = new Campeonato(nome, formato, clubes);
        for (Clube clube : clubes) {
            clube.addCampeonato(campeonato);
            clubeRepository.save(clube);

            campeonato.putPontuacao(clube, 0);
            campeonato.putClassificacao(clube, 0);
        }
        campeonatoRepository.save(campeonato);

        return toDTO(campeonato);
    }

    // L. Verificar, remover e atualizar os campeonatos
    public CampeonatoDTO getCampeonato(Long id) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campeonato não existe"));
        return toDTO(campeonato);
    }

    public List<CampeonatoDTO> getAllCampeonatos() {
        List<Campeonato> campeonatos = campeonatoRepository.findAll();
        return campeonatos.stream().map(campeonato -> toDTO(campeonato)).toList();
    }

    public void deleteCampeonato(Long id) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campeonato não existe"));

        List<Jogo> jogos = campeonato.getJogos();
        if (jogos.stream().anyMatch(
                jogo -> jogo.getEstadoAtual() == Jogo.Estado.AGENDADO || jogo.getEstadoAtual() == Jogo.Estado.EM_CURSO))
            throw new IllegalArgumentException("Não é possível apagar um campeonato com jogos agendados ou em curso");
        for (Jogo jogo : jogos) {
            jogo.removeCampeonato();
            jogoRepository.save(jogo);
        }

        for (Clube clube : campeonato.getClubes()) {
            clube.removeCampeonato(campeonato);
            clubeRepository.save(clube);
        }

        campeonatoRepository.deleteById(id);
    }

    public CampeonatoDTO updateCampeonato(CampeonatoDTO campeonatoDTO) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Campeonato não existe"));

        String nome = campeonatoDTO.getNome();
        if (nome != null && !campeonato.getNome().equals(nome)) {
            if (campeonatoRepository.findByNome(nome).isPresent())
                throw new IllegalArgumentException("Já existe um campeonato com esse nome");
            campeonato.setNome(nome);
        }

        String formato = campeonatoDTO.getFormato();
        if (formato != null)
            campeonato.setFormato(Campeonato.Formato.valueOf(formato));

        boolean concluido = campeonatoDTO.isConcluido();
        List<Jogo> jogos = campeonato.getJogos();
        if (concluido && jogos.stream().anyMatch(
                jogo -> jogo.getEstadoAtual() == Jogo.Estado.AGENDADO || jogo.getEstadoAtual() == Jogo.Estado.EM_CURSO))
            throw new IllegalArgumentException("Não é possível concluir um campeonato com jogos agendados ou em curso");

        Map<Clube, Integer> pontuacao = campeonato.getPontuacao();
        Map<Clube, Integer> classificacao = campeonato.getClassificacao();

        List<Long> clubeIDs = campeonatoDTO.getClubes();
        List<Clube> clubes = campeonato.getClubes();
        if (clubeIDs.size() < MINIMUM_CLUBES)
            throw new IllegalArgumentException(
                    String.format("O campeonato deve ter pelo menos %d clubes", MINIMUM_CLUBES));
        if (clubeIDs != null) {
            for (Long clubeID : clubeIDs) {
                Clube clube = clubeRepository.findById(clubeID)
                        .orElseThrow(
                                () -> new IllegalArgumentException(String.format("O clube %d não existe", clubeID)));
                if (!clubes.contains(clube)) {
                    campeonato.addClube(clube);
                    clube.addCampeonato(campeonato);

                    pontuacao.put(clube, 0);
                    classificacao.put(clube, 0);

                    clubeRepository.save(clube);
                }
            }
            for (Clube clube : clubes) {
                if (!clubeIDs.contains(clube.getId())) {
                    campeonato.removeClube(clube);
                    clube.removeCampeonato(campeonato);

                    pontuacao.remove(clube);
                    removeClassificacao(classificacao, clube);

                    clubeRepository.save(clube);
                }
            }
        }

        if (concluido) {
            for (Clube clube : clubes) {
                if (classificacao.get(clube) == 1)
                    clube.putConquista(Clube.Conquista.PRIMEIRO_LUGAR, true);
                else if (classificacao.get(clube) == 2)
                    clube.putConquista(Clube.Conquista.SEGUNDO_LUGAR, true);
                else if (classificacao.get(clube) == 3)
                    clube.putConquista(Clube.Conquista.TERCEIRO_LUGAR, true);
            }
        }

        campeonato.setConcluido(concluido);
        campeonatoRepository.save(campeonato);
        return toDTO(campeonato);
    }

    // M. Cancelamento de um jogo de campeonato
    public void cancelJogo(Long campeonatoID, Long jogoID) {
        Campeonato campeonato = campeonatoRepository.findById(campeonatoID)
                .orElseThrow(() -> new IllegalArgumentException("Campeonato não existe"));
        Jogo jogo = jogoRepository.findById(jogoID)
                .orElseThrow(() -> new IllegalArgumentException("Jogo não existe"));

        if (!campeonato.getJogos().contains(jogo))
            throw new IllegalArgumentException("Jogo não pertence ao campeonato");

        if (jogo.getEstadoAtual().equals(Jogo.Estado.EM_CURSO) || jogo.getEstadoAtual().equals(Jogo.Estado.CONCLUIDO))
            throw new IllegalArgumentException("Não é possível cancelar o jogo em curso ou concluído");

        if (jogo.getEstadoAtual().equals(Jogo.Estado.CANCELADO))
            throw new IllegalArgumentException("O jogo já foi cancelado");

        jogo.setEstadoAtual(Jogo.Estado.CANCELADO);
        jogoRepository.save(jogo);
        // remover jogo da lista de jogos do campeonato?
    }

    private void removeClassificacao(Map<Clube, Integer> classificacao, Clube clubeRemoved) {
        int lugarRemoved = classificacao.get(clubeRemoved);
        classificacao.remove(clubeRemoved);

        for (Map.Entry<Clube, Integer> entry : classificacao.entrySet()) {
            int currentLugar = entry.getValue();
            if (currentLugar > lugarRemoved)
                entry.setValue(currentLugar - 1);
        }
    }

    public List<CampeonatoDTO> filterCampeonatos(String nome, List<Long> clubes, Integer numJogosConcluidos,
            Integer numJogosAgendados) {
        return campeonatoRepository.findAll().stream()
                .filter(campeonato -> nome == null || nome.isBlank()
                        || campeonato.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(campeonato -> clubes == null
                        || campeonato.getClubes().stream().map(Clube::getId).anyMatch(clubes::contains))
                .filter(campeonato -> {
                    if (numJogosConcluidos == null)
                        return true;
                    List<Jogo> jogosConcluidos = campeonato.getJogos().stream()
                            .filter(jogo -> jogo.getEstadoAtual().equals(Jogo.Estado.CONCLUIDO)).toList();
                    return jogosConcluidos.size() == numJogosConcluidos;
                }).filter(campeonato -> {
                    if (numJogosAgendados == null)
                        return true;
                    List<Jogo> jogosAgendados = campeonato.getJogos().stream()
                            .filter(jogo -> jogo.getEstadoAtual().equals(Jogo.Estado.AGENDADO)).toList();
                    return jogosAgendados.size() == numJogosAgendados;
                }).map(this::toDTO)
                .toList();
    }

    private CampeonatoDTO toDTO(Campeonato campeonato) {
        CampeonatoDTO dto = new CampeonatoDTO();

        dto.setId(campeonato.getId());
        dto.setNome(campeonato.getNome());
        dto.setFormato(campeonato.getFormato().toString());
        dto.setConcluido(campeonato.isConcluido());
        dto.setClubes(campeonato.getClubes().stream().map(Clube::getId).toList());
        dto.setJogos(campeonato.getJogos().stream().map(Jogo::getId).toList());

        Map<Long, Integer> pontuacao = new HashMap<>();
        for (Map.Entry<Clube, Integer> p : campeonato.getPontuacao().entrySet())
            pontuacao.put(p.getKey().getId(), p.getValue());
        dto.setPontuacao(pontuacao);

        Map<Long, Integer> classificao = new HashMap<>();
        for (Map.Entry<Clube, Integer> c : campeonato.getClassificacao().entrySet())
            classificao.put(c.getKey().getId(), c.getValue());
        dto.setClassificacao(classificao);

        return dto;
    }
}
