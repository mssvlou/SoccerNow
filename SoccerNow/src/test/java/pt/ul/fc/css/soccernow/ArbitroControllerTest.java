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
import pt.ul.fc.css.soccernow.controllers.ArbitroController;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArbitroController.class)
class ArbitroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArbitroHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Set serialization to omit null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void deveCriarArbitro() throws Exception {
        ArbitroDTO arbitroDTO = new ArbitroDTO(1L, "Árbitro X", "arbitro@example.com", "password", true,
                null);

        when(handler.criarArbitro(any())).thenReturn(arbitroDTO);

        mockMvc.perform(post("/api/arbitros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(arbitroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Árbitro X"));
    }

    @Test
    public void deveAtualizarArbitro() throws Exception {
        ArbitroDTO updatedArbitro = new ArbitroDTO(1L, "Árbitro Y", "novo@email.com", "novaPass", false,
                null);

        when(handler.atualizarArbitro(eq(1L), any())).thenReturn(updatedArbitro);

        mockMvc.perform(put("/api/arbitros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedArbitro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L)) // Verifica o ID
                .andExpect(jsonPath("$.nome").value("Árbitro Y")) // Verifica o nome
                .andExpect(jsonPath("$.email").value("novo@email.com")) // Verifica o email
                .andExpect(jsonPath("$.certificado").value(false)); // Verifica o estado do certificado
    }

    @Test
    public void deveRemoverArbitro() throws Exception {
        doNothing().when(handler).removerArbitro(1L);

        mockMvc.perform(delete("/api/arbitros/1"))
                .andExpect(status().isNoContent());
    }
}
