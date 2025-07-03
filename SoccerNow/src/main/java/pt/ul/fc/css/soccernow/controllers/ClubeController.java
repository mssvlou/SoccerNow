package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.handlers.ClubeHandler;

@RestController
@RequestMapping("/api/clubes")
@Api(value = "Clube API", tags = "Clubes")
public class ClubeController {

    @Autowired
    private ClubeHandler clubeHandler;

    @Operation(
        summary = "Criar clube",
        description = "Cria um clube novo com o nome especificado.\n\n" +
              "- **200 OK**: Clube criado com sucesso, devolve o seu DTO\n" +
              "- **400 Bad Request**: Já existe um clube com o nome especificado")
    @PostMapping
    public ResponseEntity<?> createClube(@NonNull @RequestBody ClubeDTO clubeDTO) {
        try {
            ClubeDTO responseDTO = clubeHandler.createClube(clubeDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Verificar clube",
        description = "Obtém um clube pelo seu ID.\n\n" +
                     "- **200 OK**: Clube encontrado, devolve o seu DTO\n" +
                     "- **404 Not Found**: Clube não existe")    
    @GetMapping("{id}")
    public ResponseEntity<ClubeDTO> getClube(@PathVariable("id") Long id) {
        try {
            ClubeDTO responseDTO = clubeHandler.getClube(id);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Verificar todos os clubes",
        description = "Obtém todos os clubes.\n\n" +
                     "- **200 OK**: Devolve o DTO de todos os clubes")    
    @GetMapping
    public ResponseEntity<List<ClubeDTO>> getAllClubes() {
        List<ClubeDTO> responseDTO = clubeHandler.getAllClubes();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
        summary = "Apagar clube",
        description = "Apaga um clube pelo seu ID.\n\n" +
                     "- **200 OK**: Clube apagado, devolve confirmação da remoção\n" +
                     "- **400 Bad Request**: Remoção inválida, devolve mensagem de erro\n" +
                     "- **404 Not Found**: Clube não existe")    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteClube(@PathVariable("id") Long id) {
        try {
            clubeHandler.deleteClube(id);
            return ResponseEntity.ok(String.format("Clube com id=%s apagado", id));
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Clube não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Atualizar clube",
        description = "Atualiza o nome do clube.\n\n" +
                     "- **200 OK**: Clube atualizado, devolve o seu DTO\n" +
                     "- **400 Bad Request**: Já existe um clube com o nome especificado\n" +
                     "- **404 Not Found**: Clube não existe")
    @PatchMapping
    public ResponseEntity<?> updateClube(@NonNull @RequestBody ClubeDTO clubeDTO) {
        try {
            ClubeDTO responseDTO = clubeHandler.updateClube(clubeDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Clube não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Buscar por clubes com filtros",
        description = "Busca por clubes.\n\n" +
                     "- **200 OK**: Clube encontrado, devolve o seu DTO")
    @GetMapping("/search")
    public ResponseEntity<?> filterClubes(@RequestParam(required=false) String nome,
                                        @RequestParam(required=false) Integer numJogadores,
                                        @RequestParam(required=false) List<String> conquistas,
                                        @RequestParam(required=false) List<String> posicoes) {
        try {
            List<ClubeDTO> responseDTO = clubeHandler.filterClubes(nome, numJogadores, conquistas, posicoes);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
