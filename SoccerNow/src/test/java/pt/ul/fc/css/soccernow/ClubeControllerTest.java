package pt.ul.fc.css.soccernow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.soccernow.controllers.ClubeController;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.handlers.ClubeHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;

@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeHandler clubeHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private ClubeDTO dto;

    @BeforeEach
    public void generateMockClube() {
        dto = new ClubeDTO(1L, "Clube", new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testCreateClube() throws Exception {
        when(clubeHandler.createClube(any(ClubeDTO.class))).thenReturn(dto);
        mockMvc.perform(post("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Clube"));
    }

    @Test
    public void testCreateClube_BadRequest() throws Exception {
        when(clubeHandler.createClube(any(ClubeDTO.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetClube() throws Exception {
        when(clubeHandler.getClube(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/clubes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Clube"));
    }

    @Test
    public void testGetClube_NotFound() throws Exception {
        when(clubeHandler.getClube(1L)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/clubes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteClube() throws Exception {
        mockMvc.perform(delete("/api/clubes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Clube com id=1 apagado"));
    }

    @Test
    public void testUpdateClube() throws Exception {
        dto.setNome("NewNome");
        when(clubeHandler.updateClube(any(ClubeDTO.class))).thenReturn(dto);
        mockMvc.perform(patch("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("NewNome"));
    }

    @Test
    public void testUpdateClube_BadRequest() throws Exception {
        dto.setNome("NewNome");
        when(clubeHandler.updateClube(any(ClubeDTO.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(patch("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateClube_NotFound() throws Exception {
        dto.setNome("NewNome");
        when(clubeHandler.updateClube(any(ClubeDTO.class))).thenThrow(new IllegalArgumentException("Clube não existe"));
        mockMvc.perform(patch("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
