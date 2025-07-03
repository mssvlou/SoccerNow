package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.entities.Clube;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.entities.Utilizador;
import pt.ul.fc.css.soccernow.mappers.UtilizadorMapper;
import pt.ul.fc.css.soccernow.repositories.ClubeRepository;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JogadorHandler {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    public JogadorDTO criarJogador(JogadorDTO dto) {
        String email = dto.getEmail();
        if (utilizadorRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Já existe um utilizador com o e-mail especificado");

        Clube clube = null;
        if (dto.getClubeID() != null)
            clube = clubeRepository.findById(dto.getClubeID())
                    .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        Jogador.Posicao posicao;
        try {
            posicao = Jogador.Posicao.valueOf(dto.getPosicao());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Posição inválida.");
        }

        Jogador jogador = new Jogador(dto.getNome(), dto.getEmail(), dto.getPassword(), posicao, clube);
        jogador = (Jogador) utilizadorRepository.save(jogador);

        return UtilizadorMapper.jogadorToDTO(jogador);
    }

    public List<JogadorDTO> listarTodosJogadores() {
        return utilizadorRepository.findAll().stream()
                .filter(u -> u instanceof Jogador)
                .map(u -> UtilizadorMapper.jogadorToDTO((Jogador) u))
                .collect(Collectors.toList());
    }

    public List<JogadorDTO> getJogadoresByClube(Long clubeID) {
        return utilizadorRepository.findJogadoresByClubeId(clubeID).stream()
                .map(jogador -> UtilizadorMapper.jogadorToDTO(jogador)).toList();
    }

    public List<JogadorDTO> filtrarJogadores(String nome, List<String> posicoesStrings, Integer golos, Integer cartoes,
            Integer jogos) {
        List<Jogador.Posicao> posicoes = posicoesStrings != null
                ? posicoesStrings.stream().map(Jogador.Posicao::valueOf).toList()
                : null;

        return utilizadorRepository.findAll().stream()
                .filter(u -> u instanceof Jogador)
                .map(u -> (Jogador) u)
                .filter(j -> nome == null || nome.isBlank() || j.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(j -> {
                    if (posicoes == null)
                        return true;
                    return posicoes.contains(j.getPosicao());
                })
                .filter(j -> golos == null || j.getNumGolosMarcados() == golos)
                .filter(j -> {
                    if (cartoes == null)
                        return true;
                    int totalCartoes = j.getCartoesRecebidos().values().stream().mapToInt(Integer::intValue).sum();
                    return totalCartoes == cartoes;
                })
                .filter(j -> jogos == null || j.getEquipas().stream()
                        .mapToInt(e -> (int) e.getJogos().stream()
                                .filter(jogo -> jogo.getEstadoAtual() == Jogo.Estado.CONCLUIDO).count())
                        .sum() == jogos)
                .map(UtilizadorMapper::jogadorToDTO)
                .toList();
    }

    public void removerJogador(Long id) {
        Jogador jogador = (Jogador) utilizadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        if (!jogador.getEquipas().isEmpty()) {
            throw new IllegalStateException("Jogador pertence a uma ou mais equipas");
        }

        if (jogador.getClube() != null) {
            jogador.getClube().getJogadores().remove(jogador);
        }

        utilizadorRepository.delete(jogador);
    }

    public JogadorDTO atualizarJogador(Long id, JogadorDTO dto) {
        Utilizador u = utilizadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
        if (!(u instanceof Jogador jogador)) {
            throw new IllegalArgumentException("Utilizador não é um jogador");
        }

        jogador.setNome(dto.getNome());

        String email = dto.getEmail();
        if (jogador.getEmail() != email) // avoid violating unique constraint
            jogador.setEmail(email);

        jogador.setPassword(dto.getPassword());

        // Atualizar posição
        try {
            jogador.setPosicao(Jogador.Posicao.valueOf(dto.getPosicao()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Posição inválida.");
        }

        // Atualizar clube, se necessário
        if (dto.getClubeID() != null) {
            Clube novoClube = clubeRepository.findById(dto.getClubeID())
                    .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

            // Se o jogador já tiver um clube, remover da lista de jogadores do clube
            // anterior
            if (jogador.getClube() != null) {
                if (!jogador.getEquipas().isEmpty())
                    throw new IllegalArgumentException(
                            "Jogador não pode sair do clube se ainda pertence a equipas ativas");
                jogador.getClube().getJogadores().remove(jogador);
            }

            // Atualizar o clube do jogador
            jogador.setClube(novoClube);
            novoClube.getJogadores().add(jogador); // Adicionar o jogador ao novo clube
        }

        jogador = (Jogador) utilizadorRepository.save(jogador);
        return UtilizadorMapper.jogadorToDTO(jogador);
    }
}
