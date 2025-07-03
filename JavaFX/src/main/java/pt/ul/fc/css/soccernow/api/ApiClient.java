package pt.ul.fc.css.soccernow.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalDTO;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static UtilizadorDTO login(String email, String password) throws Exception {
        String endpoint = String.format("%s/utilizadores/login?email=%s&password=%s", 
            BASE_URL, email, password);
        HttpResponse<String> response = POST(endpoint,null);
        if (response.statusCode() != 200) 
            throw new RuntimeException(response.body());

        JsonNode node = mapper.readTree(response.body());
        if (node.has("posicao")) 
            return mapper.treeToValue(node, JogadorDTO.class);
        else
            return mapper.treeToValue(node, ArbitroDTO.class);
    }

    // UTILIZADORES (JOGADORES & ÁRBITROS)

    public static List<UtilizadorDTO> getUtilizadores() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/utilizadores");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());

        List<UtilizadorDTO> utilizadores = new ArrayList<>();
        for (JsonNode node : mapper.readTree(response.body()))
            if (node.has("posicao"))
                utilizadores.add(mapper.treeToValue(node, JogadorDTO.class));
            else 
                utilizadores.add(mapper.treeToValue(node, ArbitroDTO.class));

        return utilizadores;
    }
    
    public static Long registarArbitro(ArbitroDTO arbitro) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/arbitros", mapper.writeValueAsString(arbitro));
        if (response.statusCode() != 201)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), ArbitroDTO.class).getId();
    }

    public static Long registarJogador(JogadorDTO jogador) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/jogadores", mapper.writeValueAsString(jogador));
        if (response.statusCode() != 201)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), JogadorDTO.class).getId();
    }

    public static List<JogadorDTO> getJogadoresByClube(Long clubeID) throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/jogadores/search/clube/" + clubeID);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());

        return mapper.readValue(response.body(), new TypeReference<List<JogadorDTO>>() {});
    }

    public static void removeJogador(Long id) throws Exception {
        HttpResponse<String> response = DELETE(BASE_URL + "/jogadores/" + id);
        if (response.statusCode() != 204)
            throw new RuntimeException(response.body());
    }

    public static void removeArbitro(Long id) throws Exception {
        HttpResponse<String> response = DELETE(BASE_URL + "/arbitros/" + id);
        if (response.statusCode() != 204) 
            throw new RuntimeException(response.body());
    }

    public static void updateJogador(JogadorDTO jogador) throws Exception {
        HttpResponse<String> response = PUT(BASE_URL + "/jogadores/" + jogador.getId(), mapper.writeValueAsString(jogador));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void updateArbitro(ArbitroDTO arbitro) throws Exception {
        HttpResponse<String> response = PUT(BASE_URL + "/arbitros/" + arbitro.getId(), mapper.writeValueAsString(arbitro));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    // CLUBES
    
    public static ClubeDTO getClube(Long id) throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/clubes/" + id);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<ClubeDTO>() {});
    }

    public static List<ClubeDTO> getClubes() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/clubes");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<List<ClubeDTO>>() {});
    }

    public static void criarClube(ClubeDTO clube) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/clubes", mapper.writeValueAsString(clube));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void removeClube(Long id) throws Exception {
        HttpResponse<String> response = DELETE(BASE_URL + "/clubes/" + id);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void updateClube(ClubeDTO clube) throws Exception {
        HttpResponse<String> response = PATCH(BASE_URL + "/clubes", mapper.writeValueAsString(clube));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    // EQUIPAS
    
    public static void criarEquipa(EquipaDTO equipa) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/equipas", mapper.writeValueAsString(equipa));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static EquipaDTO getEquipa(Long id) throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/equipas/" + id);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<EquipaDTO>() {});
    }

    public static List<EquipaDTO> getEquipas() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/equipas");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<List<EquipaDTO>>() {});
    }

    public static void removeEquipa(Long id) throws Exception {
        HttpResponse<String> response = DELETE(BASE_URL + "/equipas/" + id);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void updateEquipa(EquipaDTO equipa) throws Exception {
        HttpResponse<String> response = PATCH(BASE_URL + "/equipas", mapper.writeValueAsString(equipa));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    // JOGOS

    public static void criarJogo(JogoDTO jogo) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/jogos", mapper.writeValueAsString(jogo));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static List<JogoDTO> getJogos() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/jogos");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<List<JogoDTO>>() {});
    }

    public static void registarResultado(EstatisticasDTO estatisticas) throws Exception {
        HttpResponse<String> response = PATCH(BASE_URL + "/jogos", mapper.writeValueAsString(estatisticas));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void criarLocal(LocalDTO local) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/locais", mapper.writeValueAsString(local));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static List<LocalDTO> getLocais() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/locais");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<List<LocalDTO>>() {});
    }

    // CAMPEONATOS
    
    public static void criarCampeonato(CampeonatoDTO campeonato) throws Exception {
        HttpResponse<String> response = POST(BASE_URL + "/campeonatos", mapper.writeValueAsString(campeonato));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    } 

    public static List<CampeonatoDTO> getCampeonatos() throws Exception {
        HttpResponse<String> response = GET(BASE_URL + "/campeonatos");
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
        return mapper.readValue(response.body(), new TypeReference<List<CampeonatoDTO>>() {});
    }

    public static void removeCampeonato(Long id) throws Exception {
        HttpResponse<String> response = DELETE(BASE_URL + "/campeonatos/" + id);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void updateCampeonato(CampeonatoDTO campeonato) throws Exception {
        HttpResponse<String> response = PATCH(BASE_URL + "/campeonatos", mapper.writeValueAsString(campeonato));
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    public static void cancelarJogo(Long campeonatoID, Long jogoID) throws Exception {
        HttpResponse<String> response = PATCH(BASE_URL + "/campeonatos/" + campeonatoID + "/cancel/" + jogoID, null);
        if (response.statusCode() != 200)
            throw new RuntimeException(response.body());
    }

    // --------------------------------------------------------------------------------------------
    
    private static HttpResponse<String> GET(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> POST(String endpoint, String json) throws Exception {
        BodyPublisher body = json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(body).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> PUT(String endpoint, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> DELETE(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .DELETE().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> PATCH(String endpoint, String json) throws Exception{
        BodyPublisher body = json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .method("PATCH", body).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
