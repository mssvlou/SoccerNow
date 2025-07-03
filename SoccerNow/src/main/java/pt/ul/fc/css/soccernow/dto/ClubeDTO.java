package pt.ul.fc.css.soccernow.dto;

import java.util.List;
import java.util.Map;

public class ClubeDTO {
    private Long id;
    private String nome;
    private Map<String,Boolean> conquistas;
    private List<Long> jogadorIDs;
    private List<Long> equipaIDs;
    private List<Long> campeonatoIDs;

    public ClubeDTO() {}

    public ClubeDTO(Long id, String nome, Map<String,Boolean> conquistas, List<Long> jogadorIDs, List<Long> equipaIDs, List<Long> campeonatoIDs) {
        this.id = id;
        this.nome = nome;
        this.conquistas = conquistas;
        this.jogadorIDs = jogadorIDs;
        this.equipaIDs = equipaIDs;
        this.campeonatoIDs = campeonatoIDs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Boolean> getConquistas() {
        return conquistas;
    }

    public void setConquistas(Map<String, Boolean> conquistas) {
        this.conquistas = conquistas;
    }

    public List<Long> getJogadorIDs() {
        return jogadorIDs;
    }

    public void setJogadorIDs(List<Long> jogadorIDs) {
        this.jogadorIDs = jogadorIDs;
    }

    public List<Long> getEquipaIDs() {
        return equipaIDs;
    }

    public void setEquipaIDs(List<Long> equipaIDs) {
        this.equipaIDs = equipaIDs;
    }

    public List<Long> getCampeonatoIDs() {
        return campeonatoIDs;
    }

    public void setCampeonatoIDs(List<Long> campeonatoIDs) {
        this.campeonatoIDs = campeonatoIDs;
    }
}
