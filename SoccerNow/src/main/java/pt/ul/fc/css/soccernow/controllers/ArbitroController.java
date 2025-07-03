package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;

@RestController
@RequestMapping("/api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroHandler arbitroHandler;

    @PostMapping
    public ResponseEntity<?> criarArbitro(@RequestBody ArbitroDTO arbitroDTO) {                  
        try {
            ArbitroDTO criado = arbitroHandler.criarArbitro(arbitroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarArbitro(@PathVariable Long id, @RequestBody ArbitroDTO dto) {
        try {
            ArbitroDTO atualizado = arbitroHandler.atualizarArbitro(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerArbitro(@PathVariable Long id) {
        try {
            arbitroHandler.removerArbitro(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ArbitroDTO>> filtrarArbitros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer jogosOficiados,
            @RequestParam(required = false) Integer cartoesMostrados) {
        List<ArbitroDTO> arbitros = arbitroHandler.filtrarArbitros(nome, jogosOficiados, cartoesMostrados);
        return ResponseEntity.ok(arbitros);
    }

}
