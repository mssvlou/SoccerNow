package pt.ul.fc.css.soccernow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.controllers.JogadorController;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

@WebMvcTest(JogadorController.class)
public class JogadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JogadorHandler jogadorHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private JogadorDTO validJogadorDTO;

    @BeforeEach
    public void setup() {
        // Set serialization to omit null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Setup a valid JogadorDTO with null equipeIDs
        validJogadorDTO = new JogadorDTO(1L, "Player John", "john@example.com", "password", "Fixo", 0, null, 1L, null);
    }

    @Test
    public void testCriarJogador() throws Exception {
        JogadorDTO createdJogador = new JogadorDTO(1L, "Player John", "john@example.com", "password", "Fixo", 0, null,
                1L, null);
        when(jogadorHandler.criarJogador(any(JogadorDTO.class))).thenReturn(createdJogador);

        mockMvc.perform(post("/api/jogadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validJogadorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Player John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.posicao").value("Fixo"))
                .andExpect(jsonPath("$.clubeID").value(1));
    }

    @Test
    public void testListarJogadores() throws Exception {
        JogadorDTO jogador1 = new JogadorDTO(1L, "Jogador1", "j1@email.com", "password", "ALA", 0, null, 1L, null);
        JogadorDTO jogador2 = new JogadorDTO(2L, "Jogador2", "j2@email.com", "password", "FIXO", 0, null, 1L, null);

        when(jogadorHandler.listarTodosJogadores()).thenReturn(Arrays.asList(jogador1, jogador2));

        mockMvc.perform(get("/api/jogadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Jogador1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Jogador2"));
    }

    @Test
    public void testAtualizarJogador() throws Exception {
        JogadorDTO updatedDTO = new JogadorDTO(
                1L,
                "Player John Updated",
                "john2@example.com",
                "newpass",
                "ALA",
                0,
                null,
                1L,
                null);
        when(jogadorHandler.atualizarJogador(eq(1L), any(JogadorDTO.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Player John Updated"))
                .andExpect(jsonPath("$.email").value("john2@example.com"))
                .andExpect(jsonPath("$.posicao").value("ALA")) // verify posicao
                .andExpect(jsonPath("$.clubeID").value(1)); // verify clubeID
    }

    @Test
    public void testRemoverJogador() throws Exception {
        doNothing().when(jogadorHandler).removerJogador(1L);

        mockMvc.perform(delete("/api/jogadores/1"))
                .andExpect(status().isNoContent());
    }
}
