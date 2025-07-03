package pt.ul.fc.css.soccernow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.controllers.UtilizadorController;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.handlers.UtilizadorHandler;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilizadorController.class)
public class UtilizadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilizadorHandler handler;

    private JogadorDTO jogadorDTO;
    private ArbitroDTO arbitroDTO;

    @BeforeEach
    public void setup() {
        jogadorDTO = new JogadorDTO(10L, "Jogador1", "j1@email.com", null, "ALA", 0, null, 1L, null);
        arbitroDTO = new ArbitroDTO(20L, "Árbitro1", "a1@email.com", null, true, null);
    }

    @Test
    public void deveObterUtilizadorJogador() throws Exception {
        when(handler.obterUtilizador(10L)).thenReturn(jogadorDTO);

        mockMvc.perform(get("/api/utilizadores/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Jogador1"))
                .andExpect(jsonPath("$.posicao").value("ALA"));
    }

    @Test
    public void deveObterUtilizadorArbitro() throws Exception {
        when(handler.obterUtilizador(20L)).thenReturn(arbitroDTO);

        mockMvc.perform(get("/api/utilizadores/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.nome").value("Árbitro1"))
                .andExpect(jsonPath("$.certificado").value(true));
    }
}
