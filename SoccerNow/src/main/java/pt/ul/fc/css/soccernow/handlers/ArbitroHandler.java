package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.mappers.UtilizadorMapper;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

import pt.ul.fc.css.soccernow.entities.Estatisticas;
import pt.ul.fc.css.soccernow.entities.Jogo;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbitroHandler {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    public ArbitroDTO criarArbitro(ArbitroDTO dto) {
        String email = dto.getEmail();
        if (utilizadorRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Já existe um utilizador com o e-mail especificado");
        Arbitro arbitro = new Arbitro(dto.getNome(), email, dto.getPassword(), dto.isCertificado());

        // jogos são ignorados na criação por enquanto
        arbitro = (Arbitro) utilizadorRepository.save(arbitro);

        return UtilizadorMapper.arbitroToDTO(arbitro);
    }

    public void removerArbitro(Long id) {
        Arbitro arbitro = (Arbitro) utilizadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Árbitro não encontrado"));

        // Verifica se o árbitro tem jogos associados
        if (!arbitro.getJogos().isEmpty()) {
            throw new IllegalStateException("Árbitro possui jogos associados");
        }

        utilizadorRepository.delete(arbitro);
    }

    public ArbitroDTO atualizarArbitro(Long id, ArbitroDTO dto) {
        Arbitro arbitro = (Arbitro) utilizadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Árbitro não encontrado"));

        // Atualiza os dados do árbitro
        arbitro.setNome(dto.getNome());

        String email = dto.getEmail();
        if (arbitro.getEmail() != email) // avoid violating unique constraint
            arbitro.setEmail(email);

        arbitro.setPassword(dto.getPassword());
        arbitro.setCertificado(dto.isCertificado());

        // Atualizações de jogos ainda não são tratadas por agora

        utilizadorRepository.save(arbitro);
        return UtilizadorMapper.arbitroToDTO(arbitro);
    }

    public List<ArbitroDTO> filtrarArbitros(String nome, Integer jogosOficiados, Integer cartoesMostrados) {
        return utilizadorRepository.findAll().stream()
                .filter(u -> u instanceof Arbitro)
                .map(u -> (Arbitro) u)
                .filter(a -> nome == null || nome.isBlank() || a.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(a -> jogosOficiados == null || a.getJogos().stream()
                        .filter(jogo -> jogo.getEstadoAtual() == Jogo.Estado.CONCLUIDO).count() == jogosOficiados)
                .filter(a -> {
                    if (cartoesMostrados == null)
                        return true;
                    int totalCartoes = a.getJogos().stream()
                            // considerando que só o principal pode atribuir cartões
                            .filter(j -> j.getArbitroPrincipal().getId().equals(a.getId()))
                            .map(Jogo::getEstatisticas)
                            .filter(Objects::nonNull)
                            .map(Estatisticas::getCartoesAtribuidos)
                            .filter(Objects::nonNull)
                            .mapToInt(map -> map.size()) // um cartão por entrada
                            .sum();
                    return totalCartoes == cartoesMostrados;
                })
                .map(UtilizadorMapper::arbitroToDTO)
                .collect(Collectors.toList());
    }
}
