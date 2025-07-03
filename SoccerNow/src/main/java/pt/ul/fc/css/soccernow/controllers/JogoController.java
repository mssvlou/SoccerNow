package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalDTO;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;

@RestController
@RequestMapping("/api")
@Api(value = "Jogo API", tags = "Jogos")
public class JogoController {

    @Autowired
    private JogoHandler jogoHandler;

    @Operation(summary = "Criar jogo", description = "Cria um jogo novo com a data, horário, local, equipas e árbitros (incluíndo principal) especificados.\n\n"
            +
            "- **200 OK**: Jogo criado com sucesso, devolve o seu DTO\n" +
            "- **400 Bad Request**\n\n" +
            "**IMPORTANTE:** Usar formato `\"horario\": \"HH:mm:ss\"` para o horário.")
    @PostMapping("/jogos")
    public ResponseEntity<?> createJogo(@NonNull @RequestBody JogoDTO jogoDTO) {
        try {
            JogoDTO responseDTO = jogoHandler.createJogo(jogoDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Verifica todos os jogos", description = "Obtém todos os jogos.\n\n" +
            "- **200 OK**: Devolve o DTO de todos os jogos")
    @GetMapping("/jogos")
    public ResponseEntity<List<JogoDTO>> getAllJogos() {
        List<JogoDTO> responseDTO = jogoHandler.getAllJogos();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Registar resultado", description = "Atualiza o jogo que é especificado nas estatísticas (que são criadas com o resultado, equipa vitoriosa, golos e cartões atribuídos especificados).\n\n"
            +
            "- **200 OK**: Resultado registado com sucesso, devolve o DTO das estatísticas\n" +
            "- **400 Bad Request**\n" +
            "- **404 Not Found**: Jogo não existe")
    @PatchMapping("/jogos")
    public ResponseEntity<?> registarResultado(@NonNull @RequestBody EstatisticasDTO estatisticasDTO) {
        try {
            EstatisticasDTO responseDTO = jogoHandler.registarResultado(estatisticasDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() == "Jogo não existe")
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Criar local", description = "Cria um local novo com o nome, morada e código postal especificados.\n\n"
            +
            "- **200 OK**: Local criado com sucesso, devolve o seu DTO")
    @PostMapping("/locais")
    public ResponseEntity<LocalDTO> createLocal(@NonNull @RequestBody LocalDTO localDTO) {
        LocalDTO responseDTO = jogoHandler.createLocal(localDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/locais")
    public ResponseEntity<List<LocalDTO>> getLocais() {
        return ResponseEntity.ok(jogoHandler.getLocais());
    }

    @Operation(summary = "Buscar por jogos com filtros", description = "Busca por jogos.\n\n" +
            "- **200 OK**: Jogo(s) encontrado(s), devolve o(s) seu(s) DTO(s)")
    @GetMapping("jogos/search")
    public ResponseEntity<?> searchJogo(@RequestParam(required = false) List<String> estados,
            @RequestParam(required = false) Integer numGolos,
            @RequestParam(required = false) List<Long> localIDs,
            @RequestParam(required = false) List<String> turnos) {
        try {
            List<JogoDTO> responseDTO = jogoHandler.filterJogos(estados, numGolos, localIDs, turnos);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
