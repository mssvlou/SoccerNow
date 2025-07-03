package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;
import pt.ul.fc.css.soccernow.handlers.UtilizadorHandler;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {

    @Autowired
    private UtilizadorHandler handler;

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestParam(required=true) String email, @RequestParam(required=false) String password) {
        try {
            UtilizadorDTO response = handler.autenticar(email,password);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Email ou password incorretos.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterUtilizador(@PathVariable Long id) {
        try {
            UtilizadorDTO dto = handler.obterUtilizador(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Utilizador não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<UtilizadorDTO>> getAllUtilizadores() {
        List<UtilizadorDTO> dtos = handler.getAllUtilizadores();
        return ResponseEntity.ok(dtos);
    }
}
