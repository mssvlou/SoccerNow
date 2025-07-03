package pt.ul.fc.css.soccernow.mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Estatisticas.Cartao;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;

public class UtilizadorMapper {
    
    public static JogadorDTO jogadorToDTO(Jogador jogador) {
        Map<Cartao, Integer> cartoesRecebidos = jogador.getCartoesRecebidos();
        Map<String, Integer> cartoesRecebidosString = cartoesRecebidos.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue));

        List<Long> equipaIDs = jogador.getEquipas().stream().map(Equipa::getId).toList();

        return new JogadorDTO(
                jogador.getId(),
                jogador.getNome(),
                jogador.getEmail(),
                "password",
                jogador.getPosicao().name(),
                jogador.getNumGolosMarcados(),
                cartoesRecebidosString,
                jogador.getClube() != null ? jogador.getClube().getId() : null,
                equipaIDs);
    }

    public static ArbitroDTO arbitroToDTO(Arbitro arbitro) {
        List<Long> jogoIDs = arbitro.getJogos().stream().map(Jogo::getId).toList();
        return new ArbitroDTO(
                arbitro.getId(),
                arbitro.getNome(),
                arbitro.getEmail(),
                "password", // oculta o valor real da senha
                arbitro.isCertificado(),
                jogoIDs
        );
    }
}
