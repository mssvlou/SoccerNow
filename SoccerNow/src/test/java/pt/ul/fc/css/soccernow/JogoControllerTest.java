package pt.ul.fc.css.soccernow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.soccernow.controllers.JogoController;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.entities.Estatisticas.Cartao;
import pt.ul.fc.css.soccernow.entities.Jogo.Estado;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;
import pt.ul.fc.css.soccernow.repositories.ClubeRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;
import pt.ul.fc.css.soccernow.repositories.LocalRepository;
import pt.ul.fc.css.soccernow.repositories.UtilizadorRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JogoController.class)
public class JogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JogoHandler jogoHandler;

    @MockBean
    private JogoRepository jogoRepository;

    @MockBean
    private LocalRepository localRepository;

    @MockBean
    private ClubeRepository clubeRepository;

    @MockBean
    private EquipaRepository equipaRepository;

    @MockBean
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateJogo() throws Exception {
        LocalDate data = LocalDate.of(LocalDate.now().getYear() + 1, LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth());
        JogoDTO jogoDTO = new JogoDTO(1L, data, LocalTime.now(), 1L, Estado.AGENDADO.toString(), 1L, List.of(1L), 1L,
                List.of(1L, 2L), 0L);
        when(jogoHandler.createJogo(any(JogoDTO.class))).thenReturn(jogoDTO);

        mockMvc.perform(post("/api/jogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jogoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.data").value(jogoDTO.getData().toString()))
                .andExpect(jsonPath("$.localID").value(jogoDTO.getLocalID()))
                .andExpect(jsonPath("$.estadoAtual").value(jogoDTO.getEstadoAtual()))
                .andExpect(jsonPath("$.arbitroIDs[0]").value(1L))
                .andExpect(jsonPath("$.arbitroPrincipalID").value(1L))
                .andExpect(jsonPath("$.equipaIDs[0]").value(1L))
                .andExpect(jsonPath("$.equipaIDs[1]").value(2L))
                .andExpect(jsonPath("$.estatisticasID").value(0L));
    }

    @Test
    public void testCreateJogo_BadRequest() throws Exception {
        LocalDate data = LocalDate.of(LocalDate.now().getYear() + 1, LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth());
        JogoDTO jogoDTO = new JogoDTO(1L, data, LocalTime.now(), 1L, Estado.AGENDADO.toString(), 1L, List.of(1L), 1L,
                List.of(1L, 2L), 0L);
        when(jogoHandler.createJogo(any(JogoDTO.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/jogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jogoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegistarResultado() throws Exception {
        EstatisticasDTO estatisticasDTO = new EstatisticasDTO(1L, Map.of(1L, 2, 2L, 0), 1L, Map.of(1L, 1, 3L, 1),
                Map.of(6L, Cartao.AMARELO.toString(), 7L, Cartao.VERMELHO.toString()));
        when(jogoHandler.registarResultado(any(EstatisticasDTO.class))).thenReturn(estatisticasDTO);

        mockMvc.perform(patch("/api/jogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticasDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jogoID").value(1L))
                .andExpect(jsonPath("$.resultado.1").value(2))
                .andExpect(jsonPath("$.resultado.2").value(0))
                .andExpect(jsonPath("$.equipaVitoriosa").value(1))
                .andExpect(jsonPath("$.golos.1").value(1))
                .andExpect(jsonPath("$.golos.3").value(1))
                .andExpect(jsonPath("$.cartoesAtribuidos.6").value(Cartao.AMARELO.toString()))
                .andExpect(jsonPath("$.cartoesAtribuidos.7").value(Cartao.VERMELHO.toString()));
    }

    @Test
    public void testRegistarResultado_BadRequest() throws Exception {
        EstatisticasDTO estatisticasDTO = new EstatisticasDTO(1L, Map.of(1L, 2, 2L, 0), 1L, Map.of(1L, 1, 3L, 1),
                Map.of(6L, Cartao.AMARELO.toString(), 7L, Cartao.VERMELHO.toString()));
        when(jogoHandler.registarResultado(any(EstatisticasDTO.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch("/api/jogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticasDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegistarResultado_NotFound() throws Exception {
        EstatisticasDTO estatisticasDTO = new EstatisticasDTO(1L, Map.of(1L, 2, 2L, 0), 1L, Map.of(1L, 1, 3L, 1),
                Map.of(6L, Cartao.AMARELO.toString(), 7L, Cartao.VERMELHO.toString()));
        when(jogoHandler.registarResultado(any(EstatisticasDTO.class)))
                .thenThrow(new IllegalArgumentException("Jogo não existe"));

        mockMvc.perform(patch("/api/jogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticasDTO)))
                .andExpect(status().isNotFound());
    }
}
