package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

    @Autowired
    private JogadorHandler jogadorHandler;

    @PostMapping
    public ResponseEntity<?> criarJogador(@RequestBody JogadorDTO jogadorDTO) {
        try {
            JogadorDTO criado = jogadorHandler.criarJogador(jogadorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<JogadorDTO>> listarTodosJogadores() {
        List<JogadorDTO> jogadores = jogadorHandler.listarTodosJogadores();
        return ResponseEntity.ok(jogadores);
    }

    @GetMapping("/search/clube/{clubeID}")
    public ResponseEntity<List<JogadorDTO>> getJogadoresByClube(@PathVariable Long clubeID) {
        List<JogadorDTO> jogadores = jogadorHandler.getJogadoresByClube(clubeID);
        return ResponseEntity.ok(jogadores);
    }
    
    @GetMapping("/filtro")
    public ResponseEntity<List<JogadorDTO>> filtrarJogadores(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) List<String> posicoes,
            @RequestParam(required = false) Integer golos,
            @RequestParam(required = false) Integer cartoes,
            @RequestParam(required = false) Integer jogos) {
        List<JogadorDTO> jogadores = jogadorHandler.filtrarJogadores(nome, posicoes, golos, cartoes, jogos);
        return ResponseEntity.ok(jogadores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarJogador(@PathVariable Long id, @RequestBody JogadorDTO dto) {
        try {
            JogadorDTO atualizado = jogadorHandler.atualizarJogador(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerJogador(@PathVariable Long id) {
        try {
            jogadorHandler.removerJogador(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
