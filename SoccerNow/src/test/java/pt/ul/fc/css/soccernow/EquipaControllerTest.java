package pt.ul.fc.css.soccernow;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pt.ul.fc.css.soccernow.controllers.EquipaController;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipaController.class)
public class EquipaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipaHandler equipaHandler;

    @Autowired
    private ObjectMapper objectMapper;
    
    private EquipaDTO dto;

    @BeforeEach
    public void generateMockEquipa() {
        dto = new EquipaDTO(1L,"Equipa",0,0,0,1L,List.of(1L, 2L, 3L, 4L, 5L),new ArrayList<>());
    }

    @Test
    public void testCreateEquipa() throws Exception {
        when(equipaHandler.createEquipa(any(EquipaDTO.class))).thenReturn(dto);
        mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Equipa"))
                .andExpect(jsonPath("$.jogadorIDs[0]").value(1))
                .andExpect(jsonPath("$.jogadorIDs[1]").value(2))
                .andExpect(jsonPath("$.jogadorIDs[2]").value(3))
                .andExpect(jsonPath("$.jogadorIDs[3]").value(4))
                .andExpect(jsonPath("$.jogadorIDs[4]").value(5))
                .andExpect(jsonPath("$.jogoIDs[0]").doesNotExist());
    }

    @Test
    public void testCreateEquipa_BadRequest() throws Exception {
        when(equipaHandler.createEquipa(any(EquipaDTO.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetEquipa() throws Exception {
        when(equipaHandler.getEquipa(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/equipas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetEquipa_NotFound() throws Exception {
        when(equipaHandler.getEquipa(1L)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/equipas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEquipa() throws Exception {
        mockMvc.perform(delete("/api/equipas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipa com id=1 apagada"));
    }

    @Test
    public void testUpdateEquipa() throws Exception {
        dto.setNome("NewEquipa");
        when(equipaHandler.updateEquipa(any(EquipaDTO.class))).thenReturn(dto);
        mockMvc.perform(patch("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("NewEquipa"));
    }

    @Test
    public void testUpdateEquipa_BadRequest() throws Exception {
        dto.setNome("NewEquipa");
        when(equipaHandler.updateEquipa(any(EquipaDTO.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(patch("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEquipa_NotFound() throws Exception {
        dto.setNome("NewEquipa");
        when(equipaHandler.updateEquipa(any(EquipaDTO.class))).thenThrow(new IllegalArgumentException("Equipa não existe"));
        mockMvc.perform(patch("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
