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
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

@RestController
@RequestMapping("/api/equipas")
@Api(value = "Equipa API", tags = "Equipas")
public class EquipaController {

    @Autowired
    private EquipaHandler equipaHandler;

    @Operation(
        summary = "Criar equipa",
        description = "Cria uma equipa nova com o nome, clube e jogadores especificados.\n\n" +
                     "- **200 OK**: Equipa criada com sucesso, devolve o seu DTO\n" +
                     "- **400 Bad Request**:\n" +
                     "  - Já existe uma equipa com o nome especificado\n" +
                     "  - O clube indicado não existe")    
    @PostMapping
    public ResponseEntity<?> createEquipa(@NonNull @RequestBody EquipaDTO equipaDTO) {
        try {
            EquipaDTO responseDTO = equipaHandler.createEquipa(equipaDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Verificar equipa",
        description = "Obtém uma equipa pelo seu ID.\n\n" +
                     "- **200 OK**: Equipa encontrada, devolve o seu DTO\n" +
                     "- **404 Not Found**: Equipa não existe")    
    @GetMapping("{id}")
    public ResponseEntity<EquipaDTO> getEquipa(@PathVariable("id") Long id) {
        try {
            EquipaDTO responseDTO = equipaHandler.getEquipa(id);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Verifica todas as equipas",
        description = "Obtém todas as equipas.\n\n" +
                     "- **200 OK**: Devolve o DTO de todas as equipas")
    @GetMapping
    public ResponseEntity<List<EquipaDTO>> getAllEquipas() {
        List<EquipaDTO> responseDTO = equipaHandler.getAllEquipas();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
        summary = "Apagar equipa",
        description = "Apaga uma equipa pelo seu ID.\n\n" +
                     "- **200 OK**: Equipa apagada, devolve confirmação da remoção\n" +
                     "- **400 Bad Request**: Remoção inválida, devolve mensagem de erro\n" +
                     "- **404 Not Found**: Equipa não existe")    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEquipa(@PathVariable("id") Long id) {
        try {
            equipaHandler.deleteEquipa(id);
            return ResponseEntity.ok(String.format("Equipa com id=%s apagada", id));
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Equipa não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @Operation(
        summary = "Atualizar equipa",
        description = "Atualiza a equipa, campos null não seram atualizados.\n\n" +
                     "- **200 OK**: Equipa atualizada, devolve o seu DTO\n" +
                     "- **400 Bad Request**: Atualização inválida, devolve mensagem de erro\n" +
                     "- **404 Not Found**: Equipa não existe")
    @PatchMapping
    public ResponseEntity<?> updateEquipa(@NonNull @RequestBody EquipaDTO equipaDTO) {
        try {
            EquipaDTO responseDTO = equipaHandler.updateEquipa(equipaDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            if(e.getMessage() == "Equipa não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Buscar por equipas com filtros",
        description = "Busca por equipas.\n\n" +
                     "- **200 OK**: Equipa(s) encontrada(s), devolve o(s) seu(s) DTO(s)")
    @GetMapping("/search")
    public ResponseEntity<?> searchEquipa(@RequestParam(required=false) String nome,
                                        @RequestParam(required=false) Integer numVitorias,
                                        @RequestParam(required=false) Integer numEmpates,
                                        @RequestParam(required=false) Integer numDerrotas,
                                        @RequestParam(required=false) List<String> posicoesAusentes) {
        try {
            List<EquipaDTO> responseDTO = equipaHandler.filterEquipas(nome, numVitorias, numEmpates, numDerrotas, posicoesAusentes);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
