package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;
import pt.ul.fc.css.soccernow.handlers.CampeonatoHandler;
import pt.ul.fc.css.soccernow.handlers.ClubeHandler;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;
import pt.ul.fc.css.soccernow.handlers.UtilizadorHandler;

@Controller
public class WebController {

    private boolean autenticado;
    
    @Autowired
    private ArbitroHandler arbitroHandler;

    @Autowired
    private JogadorHandler jogadorHandler;

    @Autowired
    private UtilizadorHandler utilizadorHandler;

    @Autowired
    private ClubeHandler clubeHandler;

    @Autowired
    private EquipaHandler equipaHandler;

    @Autowired
    private CampeonatoHandler campeonatoHandler;

    @Autowired
    private JogoHandler jogoHandler;

    @GetMapping("/")
    public String index() {
        if (autenticado)
            return "index";
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String renderLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = true) String email,
            @RequestParam(required = false) String password, 
            final Model model) {
        try {
            utilizadorHandler.autenticar(email,password);
            autenticado = true;
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("erro", "E-mail ou password incorretos.");
            return "login";
        }
    }

    @GetMapping("/search/arbitros")
    public String filtroArbitros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer jogosOficiados,
            @RequestParam(required = false) Integer cartoesMostrados,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        model.addAttribute("arbitros", arbitroHandler.filtrarArbitros(nome, jogosOficiados, cartoesMostrados));
        return "search_arbitros";
    }

    @GetMapping("/search/jogadores")
    public String filtroJogadores(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) List<String> posicoes,
            @RequestParam(required = false) Integer golos,
            @RequestParam(required = false) Integer cartoes,
            @RequestParam(required = false) Integer jogos,
            Model model) {
        if (!autenticado)
            return "redirect:/login";
        model.addAttribute("jogadores", jogadorHandler.filtrarJogadores(nome, posicoes, golos, cartoes, jogos));
                return "search_jogadores";
    }

    @GetMapping("/search/clubes")
    public String searchClubes(@RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer numJogadores,
            @RequestParam(required = false) List<String> conquistas,
            @RequestParam(required = false) List<String> posicoesAusentes,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        model.addAttribute("clubes",
                clubeHandler.filterClubes(nome, numJogadores, conquistas, posicoesAusentes));
        return "search_clubes";
    }

    @GetMapping("/search/equipas")
    public String searchEquipas(@RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer numVitorias,
            @RequestParam(required = false) Integer numEmpates,
            @RequestParam(required = false) Integer numDerrotas,
            @RequestParam(required = false) List<String> posicoesAusentes,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        model.addAttribute("equipas",
                equipaHandler.filterEquipas(nome, numVitorias, numEmpates, numDerrotas, posicoesAusentes));
        return "search_equipas";
    }

    @GetMapping("/search/campeonatos")
    public String searchCampeonatos(@RequestParam(required = false) String nome,
            @RequestParam(required = false) List<Long> clubes,
            @RequestParam(required = false) Integer numJogosConcluidos,
            @RequestParam(required = false) Integer numJogosAgendados,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        model.addAttribute("clubes", clubeHandler.getAllClubes());
        model.addAttribute("campeonatos",
                campeonatoHandler.filterCampeonatos(nome, clubes, numJogosConcluidos, numJogosAgendados));
        return "search_campeonatos";
    }

    @GetMapping("/search/jogos")
    public String searchJogos(@RequestParam(required = false) List<String> estados,
            @RequestParam(required = false) Integer numGolos,
            @RequestParam(required = false) List<Long> localIDs,
            @RequestParam(required = false) List<String> turnos,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        Map<Long, String> equipas = equipaHandler.getAllEquipas().stream()
                .collect(Collectors.toMap(EquipaDTO::getId, EquipaDTO::getNome));

        model.addAttribute("equipas", equipas);
        model.addAttribute("locais", jogoHandler.getLocais());
        model.addAttribute("jogos",
                jogoHandler.filterJogos(estados, numGolos, localIDs, turnos));
        return "search_jogos";
    }

    @GetMapping("/jogos/selecionar")
    public String selecionarJogo(@RequestParam(required = false) Long jogoID,
            final Model model) {
        if (!autenticado)
            return "redirect:/login";
        Map<Long, String> equipas = equipaHandler.getAllEquipas().stream()
                .collect(Collectors.toMap(EquipaDTO::getId, EquipaDTO::getNome));
        List<JogoDTO> jogos = new ArrayList<>();
        for(JogoDTO jogo : jogoHandler.getAllJogos()) {
            if (jogo.getEstadoAtual().equals("AGENDADO") || jogo.getEstadoAtual().equals("EM_CURSO"))
                jogos.add(jogo);
        }
        model.addAttribute("equipas", equipas);
        model.addAttribute("jogos", jogos);

        if (jogoID != null) {
            JogoDTO jogoSelecionado = jogos.stream()
                    .filter(j -> j.getId().equals(jogoID))
                    .findFirst().get();
            model.addAttribute("jogoSelecionado", jogoSelecionado);

            Long equipa1ID = jogoSelecionado.getEquipaIDs().get(0);
            Long equipa2ID = jogoSelecionado.getEquipaIDs().get(1);
            List<JogadorDTO> jogadoresEquipa1 = equipaHandler.getEquipa(equipa1ID).getJogadorIDs().stream()
                    .map(utilizadorHandler::obterUtilizador)
                    .map(j -> (JogadorDTO) j)
                    .toList();
            List<JogadorDTO> jogadoresEquipa2 = equipaHandler.getEquipa(equipa2ID).getJogadorIDs().stream()
                    .map(utilizadorHandler::obterUtilizador)
                    .map(j -> (JogadorDTO) j)
                    .toList();

            model.addAttribute("jogadoresEquipa1", jogadoresEquipa1);
            model.addAttribute("jogadoresEquipa2", jogadoresEquipa2);
        }

        return "selecionar_jogo";
    }

    @PostMapping("/jogos/registar")
    public String registarJogo(
            @RequestParam Long jogoID,
            @RequestParam(required = false) Map<String, String> golos,
            @RequestParam(required = false) Map<String, String> cartoesAtribuidos) {
        if (!autenticado)
            return "redirect:/login";
        JogoDTO jogoSelecionado = jogoHandler.getAllJogos().stream()
                .filter(j -> j.getId().equals(jogoID))
                .findFirst().get();

        Long equipa1ID = jogoSelecionado.getEquipaIDs().get(0);
        Long equipa2ID = jogoSelecionado.getEquipaIDs().get(1);
        List<JogadorDTO> jogadoresEquipa1 = equipaHandler.getEquipa(equipa1ID).getJogadorIDs().stream()
                .map(utilizadorHandler::obterUtilizador).map(j -> (JogadorDTO) j).toList();
        List<JogadorDTO> jogadoresEquipa2 = equipaHandler.getEquipa(equipa2ID).getJogadorIDs().stream()
                .map(utilizadorHandler::obterUtilizador).map(j -> (JogadorDTO) j).toList();

        golos.entrySet().removeIf(entry -> !entry.getKey().startsWith("golos[")); // filter out fields added unexpectedly
        Map<Long, Integer> golosMap = new HashMap<>();
        for (String key : golos.keySet()) {
            Long id = Long.parseLong(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
            Integer numGolos = Integer.parseInt(golos.get(key));
            golosMap.put(id, numGolos);
        }

        cartoesAtribuidos.entrySet().removeIf(entry -> !entry.getKey().startsWith("cartoesAtribuidos[")); // filter out fields added unexpectedly
        cartoesAtribuidos.entrySet().removeIf(entry -> entry.getValue().equals("")); // filter out nenhum
        Map<Long, String> cartoesMap = new HashMap<>();
        for (String key : cartoesAtribuidos.keySet()) {
            Long id = Long.parseLong(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
            cartoesMap.put(id, cartoesAtribuidos.get(key));
        }

        int totalEquipa1 = jogadoresEquipa1.stream()
                .mapToInt(j -> golosMap.getOrDefault(j.getId(), 0)).sum();
        int totalEquipa2 = jogadoresEquipa2.stream()
                .mapToInt(j -> golosMap.getOrDefault(j.getId(), 0)).sum();

        Long equipaVitoriosaID = null;
        if (totalEquipa1 > totalEquipa2)
            equipaVitoriosaID = equipa1ID;
        else if (totalEquipa2 > totalEquipa1)
            equipaVitoriosaID = equipa2ID;

        Map<Long, Integer> resultado = Map.of(equipa1ID, totalEquipa1, equipa2ID, totalEquipa2);

        EstatisticasDTO estatisticasDTO = new EstatisticasDTO(
                jogoID, resultado, equipaVitoriosaID, golosMap, cartoesMap);

        jogoHandler.registarResultado(estatisticasDTO);
        return "redirect:/";
    }
}
