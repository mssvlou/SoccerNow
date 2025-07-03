package pt.ul.fc.css.soccernow.handlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalDTO;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.entities.Campeonato;
import pt.ul.fc.css.soccernow.entities.Clube;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Estatisticas;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.entities.Local;
import pt.ul.fc.css.soccernow.entities.Estatisticas.Cartao;
import pt.ul.fc.css.soccernow.entities.Jogo.Estado;
import pt.ul.fc.css.soccernow.repositories.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;
import pt.ul.fc.css.soccernow.repositories.LocalRepository;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

@Service
public class JogoHandler {

    @Autowired
    private JogoRepository jogoRepository;
    @Autowired
    private LocalRepository localRepository;
    @Autowired
    private CampeonatoRepository campeonatoRepository;
    @Autowired
    private EquipaRepository equipaRepository;
    @Autowired
    private UtilizadorRepository utilizadorRepository;

    // H. Criação de jogos
    public JogoDTO createJogo(JogoDTO jogoDTO) {

        LocalDate jogoData = jogoDTO.getData();
        if (jogoData.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("A data não pode ser anterior a hoje");

        LocalTime jogoHorario = jogoDTO.getHorario();
        if (jogoData.isEqual(LocalDate.now())) {
            if (jogoHorario.isBefore(LocalTime.now()))
                throw new IllegalArgumentException("A hora não pode ser anterior à hora atual");
        }

        Long localID = jogoDTO.getLocalID();
        Local local = localRepository.findById(localID)
                .orElseThrow(() -> new IllegalArgumentException("O local não existe"));

        for (Jogo jogo : local.getJogos()) {
            if (jogo.getData().isEqual(jogoData))
                throw new IllegalArgumentException("Já existe um jogo nesse dia no mesmo local");
        }

        Long campeonatoID = jogoDTO.getCampeonatoID();
        Campeonato campeonato;
        if (campeonatoID != null) {
            campeonato = campeonatoRepository.findById(campeonatoID)
                    .orElseThrow(() -> new IllegalArgumentException("O campeonato não existe"));
        } else
            campeonato = null;

        List<Long> equipaIDs = jogoDTO.getEquipaIDs();
        if (equipaIDs.size() != 2)
            throw new IllegalArgumentException("Devem existir duas equipas num jogo");

        Clube clube = null;
        List<Equipa> equipas = new ArrayList<>();
        for (Long equipaID : equipaIDs) {
            Equipa equipa = equipaRepository.findById(equipaID)
                    .orElseThrow(() -> new IllegalArgumentException("A equipa " + equipaID + " não existe"));

            if (campeonato != null) {
                List<Clube> campeonatoClubes = campeonato.getClubes();
                if (!campeonatoClubes.contains(equipa.getClube()))
                    throw new IllegalArgumentException(
                            "O clube da equipa " + equipa.getNome() + " não pertence ao campeonato");
            }

            if (clube == null)
                clube = equipa.getClube();
            else if (clube.getId().equals(equipa.getClube().getId()))
                throw new IllegalArgumentException("As equipas têm que ser de clubes diferentes");

            for (Jogador jogador : equipa.getJogadores()) {
                for (Equipa equipaJogador : jogador.getEquipas()) {
                    for (Jogo jogo : equipaJogador.getJogos()) {
                        if (jogo.getData().isEqual(jogoData))
                            throw new IllegalArgumentException(
                                    "O jogador " + jogador.getNome() + " já tem um jogo nesse dia");
                    }
                }
            }
            equipas.add(equipa);
        }

        List<Long> arbitroIDs = jogoDTO.getArbitroIDs();
        if (arbitroIDs.size() < 1)
            throw new IllegalArgumentException("Deve existir pelo menos um árbitro num jogo");

        Long arbitroPrincipalID = jogoDTO.getArbitroPrincipalID();
        Arbitro arbitroPrincipal = null;

        List<Arbitro> arbitros = new ArrayList<>();
        for (Long arbitroID : arbitroIDs) {
            Arbitro arbitro = (Arbitro) utilizadorRepository.findById(arbitroID)
                    .orElseThrow(() -> new IllegalArgumentException("O árbitro " + arbitroID + " não existe"));

            for (Jogo jogo : arbitro.getJogos()) {
                if (jogo.getData().isEqual(jogoData))
                    throw new IllegalArgumentException("O árbitro " + arbitro.getNome() + " já tem um jogo nesse dia");
            }

            arbitros.add(arbitro);
            if (arbitroID.equals(arbitroPrincipalID))
                arbitroPrincipal = arbitro;
        }

        if (campeonato != null && arbitros.stream().noneMatch(Arbitro::isCertificado))
            throw new IllegalArgumentException("Um jogo de campeonato deve ter pelo menos um árbitro certificado");

        if (arbitroPrincipal == null) {
            if (arbitros.size() == 1)
                arbitroPrincipal = arbitros.get(0);
            else
                throw new IllegalArgumentException("O árbitro principal tem que ser um dos árbitros do jogo");
        }

        Jogo jogo = jogoRepository
                .save(new Jogo(jogoData, jogoHorario, local, campeonato, equipas, arbitros, arbitroPrincipal));
        jogoDTO.setId(jogo.getId());
        jogoDTO.setEstadoAtual(jogo.getEstadoAtual().toString());
        jogoDTO.setEstatisticasID(0L);

        local.addJogo(jogo);
        localRepository.save(local);

        for (Equipa equipa : equipas)
            equipa.addJogo(jogo);
        equipaRepository.saveAll(equipas);

        for (Arbitro arbitro : arbitros)
            arbitro.addJogo(jogo);
        utilizadorRepository.saveAll(arbitros);

        if (campeonato != null) {
            campeonato.addJogo(jogo);
            campeonatoRepository.save(campeonato);
        }

        return jogoDTO;
    }

    public List<JogoDTO> getAllJogos() {
        List<Jogo> jogos = jogoRepository.findAll();
        return jogos.stream().map(jogo -> toDTO(jogo)).toList();
    }

    // I. Registar o resultado de um jogo
    public EstatisticasDTO registarResultado(EstatisticasDTO estatisticasDTO) {
        Jogo jogo = jogoRepository.findById(estatisticasDTO.getJogoID())
                .orElseThrow(() -> new IllegalArgumentException("Jogo não existe"));

        if (jogo.getEstadoAtual() == Estado.CONCLUIDO)
            throw new IllegalArgumentException("O jogo já foi concluído");

        Set<Long> equipaIDs = new HashSet<>();
        Set<Long> jogadorIDs = new HashSet<>();
        for (Equipa equipa : jogo.getEquipas()) {
            equipaIDs.add(equipa.getId());
            for (Jogador jogador : equipa.getJogadores())
                jogadorIDs.add(jogador.getId());
        }

        if (!jogadorIDs.containsAll(estatisticasDTO.getGolos().keySet())
                || !jogadorIDs.containsAll(estatisticasDTO.getCartoesAtribuidos().keySet()))
            throw new IllegalArgumentException("Os jogadores não correspondem aos jogadores das equipas");

        if (!equipaIDs.equals(estatisticasDTO.getResultado().keySet())
                || (!equipaIDs.contains(estatisticasDTO.getEquipaVitoriosa())
                        && estatisticasDTO.getEquipaVitoriosa() != null))
            throw new IllegalArgumentException("As equipas do jogo não correspondem às equipas das estatísticas");

        for (Integer numGolos : estatisticasDTO.getGolos().values()) {
            if (numGolos < 0)
                throw new IllegalArgumentException("O número de golos não pode ser negativo");
        }

        for (String cartao : estatisticasDTO.getCartoesAtribuidos().values()) {
            if (!cartao.equals(Cartao.AMARELO.toString()) && !cartao.equals(Cartao.VERMELHO.toString()))
                throw new IllegalArgumentException("O cartão atribuído não é válido");
        }

        for (Integer resultado : estatisticasDTO.getResultado().values()) {
            if (resultado < 0)
                throw new IllegalArgumentException("O resultado não pode ser negativo");
        }

        Map<Equipa, Integer> resultado = new HashMap<>();
        for (Long equipaID : estatisticasDTO.getResultado().keySet()) {
            Equipa equipa = equipaRepository.findById(equipaID).get();
            resultado.put(equipa, estatisticasDTO.getResultado().get(equipaID));
        }

        Equipa equipa1 = jogo.getEquipas().get(0);
        Equipa equipa2 = jogo.getEquipas().get(1);
        Equipa equipaVitoriosa = null;
        if (estatisticasDTO.getEquipaVitoriosa() != null) {
            equipaVitoriosa = equipaRepository.findById(estatisticasDTO.getEquipaVitoriosa()).get();
            if (equipaVitoriosa.getId().equals(equipa1.getId())) {
                equipa1.setNumVitorias(equipa1.getNumVitorias() + 1);
                equipa2.setNumDerrotas(equipa2.getNumDerrotas() + 1);
            } else {
                equipa2.setNumVitorias(equipa2.getNumVitorias() + 1);
                equipa1.setNumDerrotas(equipa1.getNumDerrotas() + 1);
            }
        } else { // empate
            equipa1.setNumEmpates(equipa1.getNumEmpates() + 1);
            equipa2.setNumEmpates(equipa2.getNumEmpates() + 1);
        }

        equipaRepository.save(equipa1);
        equipaRepository.save(equipa2);

        Map<Jogador, Integer> golos = new HashMap<>();
        for (Long jogadorID : estatisticasDTO.getGolos().keySet()) {
            Jogador jogador = (Jogador) utilizadorRepository.findById(jogadorID).get();
            Integer numGolos = estatisticasDTO.getGolos().get(jogadorID);
            golos.put(jogador, numGolos);
            jogador.setNumGolosMarcados(jogador.getNumGolosMarcados() + numGolos);
            utilizadorRepository.save(jogador);
        }

        Map<Jogador, Cartao> cartoesAtribuidos = new HashMap<>();
        for (Long jogadorID : estatisticasDTO.getCartoesAtribuidos().keySet()) {
            Jogador jogador = (Jogador) utilizadorRepository.findById(jogadorID).get();
            Cartao cartao = Cartao.valueOf(estatisticasDTO.getCartoesAtribuidos().get(jogadorID));
            cartoesAtribuidos.put(jogador, cartao);
            Map<Cartao, Integer> cartoesRecebidos = jogador.getCartoesRecebidos();
            if (cartoesRecebidos.containsKey(cartao))
                cartoesRecebidos.put(cartao, cartoesRecebidos.get(cartao) + 1);
            else
                cartoesRecebidos.put(cartao, 1);
            jogador.setCartoesRecebidos(cartoesRecebidos);
            utilizadorRepository.save(jogador);
        }

        Estatisticas estatisticas = new Estatisticas(jogo, resultado, equipaVitoriosa, golos, cartoesAtribuidos);
        jogo.setEstatisticas(estatisticas);
        jogo.setEstadoAtual(Estado.CONCLUIDO);
        jogoRepository.save(jogo);
        estatisticasDTO.setId(estatisticas.getId());

        Campeonato campeonato = jogo.getCampeonato();
        if (campeonato != null) {
            Map<Clube, Integer> pontuacao = campeonato.getPontuacao();
            if (equipaVitoriosa != null) {
                Clube clube = equipaVitoriosa.getClube();
                pontuacao.put(clube, pontuacao.get(clube) + 3);
            } else {
                for (Equipa equipa : jogo.getEquipas()) {
                    Clube clube = equipa.getClube();
                    pontuacao.put(clube, pontuacao.get(clube) + 1);
                }
            }

            List<Clube> clubesOrdenados = pontuacao.entrySet().stream()
                    .sorted(Map.Entry.<Clube, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .toList();

            Map<Clube, Integer> classificacao = campeonato.getClassificacao();
            for (int i = 0; i < clubesOrdenados.size(); i++)
                classificacao.put(clubesOrdenados.get(i), i + 1);

            campeonatoRepository.save(campeonato);
        }

        return estatisticasDTO;
    }

    // Criar local
    public LocalDTO createLocal(LocalDTO localDTO) {
        Local local = localRepository
                .save(new Local(localDTO.getNome(), localDTO.getMorada(), localDTO.getCodigoPostal()));
        localDTO.setId(local.getId());
        localDTO.setJogosID(new ArrayList<>());
        return localDTO;
    }

    public List<LocalDTO> getLocais() {
        List<LocalDTO> locais = new ArrayList<>();
        for (Local local : localRepository.findAll()) {
            List<Long> jogoIDs = local.getJogos().stream().map(Jogo::getId).toList();
            locais.add(
                    new LocalDTO(local.getId(), local.getNome(), local.getMorada(), local.getCodigoPostal(), jogoIDs));
        }
        return locais;
    }

    public List<JogoDTO> filterJogos(List<String> estadosStrings, Integer numGolos, List<Long> localIDs,
            List<String> turnos) {
        List<Jogo.Estado> estados = estadosStrings != null
                ? estadosStrings.stream().map(Jogo.Estado::valueOf).toList()
                : null;

        return jogoRepository.findAll().stream()
                .filter(jogo -> estados == null || estados.contains(jogo.getEstadoAtual()))
                .filter(jogo -> {
                    if (numGolos == null)
                        return true;
                    Estatisticas estatisticas = jogo.getEstatisticas();
                    if (estatisticas != null) {
                        int golos = 0;
                        for (Integer golosPorJogador : estatisticas.getGolos().values())
                            golos += golosPorJogador;
                        return golos == numGolos;
                    } else
                        return false;
                }).filter(jogo -> localIDs == null || localIDs.contains(jogo.getLocal().getId()))
                .filter(jogo -> {
                    if (turnos == null)
                        return true;

                    boolean pertenceTurnos = false;
                    if (turnos.contains("MANHA"))
                        pertenceTurnos |= !jogo.getHorario().isBefore(LocalTime.of(6, 0))
                                && jogo.getHorario().isBefore(LocalTime.NOON);
                    if (turnos.contains("TARDE"))
                        pertenceTurnos |= !jogo.getHorario().isBefore(LocalTime.NOON)
                                && jogo.getHorario().isBefore(LocalTime.of(18, 0));
                    if (turnos.contains("NOITE"))
                        pertenceTurnos |= !jogo.getHorario().isBefore(LocalTime.of(18, 0));

                    return pertenceTurnos;
                })
                .map(this::toDTO)
                .toList();
    }

    private JogoDTO toDTO(Jogo jogo) {
        JogoDTO dto = new JogoDTO();

        dto.setId(jogo.getId());
        dto.setData(jogo.getData());
        dto.setHorario(jogo.getHorario());
        dto.setLocalID(jogo.getLocal().getId());
        dto.setEstadoAtual(jogo.getEstadoAtual().toString());

        Campeonato campeonato = jogo.getCampeonato();
        dto.setCampeonatoID(campeonato != null ? campeonato.getId() : 0L);

        dto.setArbitroIDs(jogo.getArbitros().stream().map(Arbitro::getId).toList());
        dto.setArbitroPrincipalID(jogo.getArbitroPrincipal().getId());
        dto.setEquipaIDs(jogo.getEquipas().stream().map(Equipa::getId).toList());

        Estatisticas estatisticas = jogo.getEstatisticas();
        dto.setEstatisticasID(estatisticas != null ? estatisticas.getId() : 0L);

        return dto;
    }
}
