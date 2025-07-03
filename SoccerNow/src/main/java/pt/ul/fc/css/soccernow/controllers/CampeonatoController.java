package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.handlers.CampeonatoHandler;

@RestController
@RequestMapping("/api/campeonatos")
@Api(value = "Campeonato API", tags = "Campeonatos")
public class CampeonatoController {
    
    @Autowired
    private CampeonatoHandler campeonatoHandler;

    @Operation(
        summary = "Criar campeonato",
        description = "Cria um campeonato com o formato e clubes especificados.\n\n" +
                "- **200 OK**: Campeonato criado com sucesso, devolve o seu DTO\n" +
                "- **400 Bad Request**: Formato inválido ou algum clube não existe")
    @PostMapping
    public ResponseEntity<?> createCampeonato(@NonNull @RequestBody CampeonatoDTO campeonatoDTO) {
        try {
            CampeonatoDTO responseDTO = campeonatoHandler.createCampeonato(campeonatoDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Verificar campeonato",
        description = "Obtém um campeonato pelo seu ID.\n\n" +
                     "- **200 OK**: Campeonato encontrado, devolve o seu DTO\n" +
                     "- **404 Not Found**: Campeonato não existe")    
    @GetMapping("{id}")
    public ResponseEntity<CampeonatoDTO> getCampeonato(@PathVariable("id") Long id) {
        try {
            CampeonatoDTO responseDTO = campeonatoHandler.getCampeonato(id);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Verificar todos os campeonatos",
        description = "Obtém todos os campeonatos.\n\n" +
                     "- **200 OK**: Devolve o DTO de todos os campeonatos")    
    @GetMapping
    public ResponseEntity<List<CampeonatoDTO>> getAllCampeonatos() {
        List<CampeonatoDTO> responseDTO = campeonatoHandler.getAllCampeonatos();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
        summary = "Apagar campeonato",
        description = "Apaga um campeonato pelo seu ID.\n\n" +
                     "- **200 OK**: Campeonato apagado, devolve confirmação da remoção\n" +
                     "- **400 Bad Request**: Remoção inválida, devolve mensagem de erro\n" +
                     "- **404 Not Found**: Campeonato não existe")    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCampeonato(@PathVariable("id") Long id) {
        try {
            campeonatoHandler.deleteCampeonato(id);
            return ResponseEntity.ok(String.format("Campeonato com id=%s apagado", id));
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Campeonato não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Atualizar campeonato",
        description = "Atualiza um campeonato.\n\n" +
                     "- **200 OK**: Campeonato atualizado com sucesso, devolve o seu DTO\n" +
                     "- **400 Bad Request**: Atualização inválida, devolve mensagem de erro\n" +
                     "- **404 Not Found**: Campeonato não existe")
    @PatchMapping
    public ResponseEntity<?> updateCampeonato(@NonNull @RequestBody CampeonatoDTO campeonatoDTO) {
        try {
            campeonatoHandler.updateCampeonato(campeonatoDTO);
            return ResponseEntity.ok("Campeonato atualizado");
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Campeonato não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Cancelar jogo de um campeonato",
        description = "Cancela o jogo de um campeonato.\n\n" +
                "- **200 OK**: Jogo cancelado com sucesso.\n" +
                "- **400 Bad Request**: Campeonato não existe ou jogo não existe/não pertence ao campeonato")
    @PatchMapping("{cID}/cancel/{jID}")
    public ResponseEntity<String> cancelJogo(@PathVariable("cID") Long campeonatoID, @PathVariable("jID") Long jogoID) {
        try {
            campeonatoHandler.cancelJogo(campeonatoID, jogoID);
            return ResponseEntity.ok("Jogo cancelado");
        } catch (IllegalArgumentException e) {
            if(e.getMessage().endsWith("não existe"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Buscar por campeonatos com filtros",
        description = "Busca por campeonatos.\n\n" +
                     "- **200 OK**: Campeonato(s) encontrado(s), devolve o(s) seu(s) DTO(s)")
    @GetMapping("/search")
    public ResponseEntity<?> searchCampeonatos(@RequestParam(required=false) String nome,
                                        @RequestParam(required=false) List<Long> clubes,
                                        @RequestParam(required=false) Integer numJogosConcluidos,
                                        @RequestParam(required=false) Integer numJogosAgendados) {
        try {
            List<CampeonatoDTO> responseDTO = campeonatoHandler.filterCampeonatos(nome, clubes, numJogosConcluidos, numJogosAgendados);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
