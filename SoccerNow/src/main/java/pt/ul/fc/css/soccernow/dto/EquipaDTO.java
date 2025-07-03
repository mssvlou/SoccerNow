package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class EquipaDTO {
    private Long id;
    private String nome;
    private int numVitorias;
    private int numEmpates;
    private int numDerrotas;
    private Long clubeID;
    private List<Long> jogadorIDs;
    private List<Long> jogoIDs;

    public EquipaDTO() {}

    public EquipaDTO(Long id, String nome, int numVitorias, int numEmpates, int numDerrotas, Long clubeID, List<Long> jogadorIDs, List<Long> jogoIDs) {
        this.id = id;
        this.nome = nome;
        this.numVitorias = numVitorias;
        this.numEmpates = numEmpates;
        this.numDerrotas = numDerrotas;
        this.clubeID = clubeID;
        this.jogadorIDs = jogadorIDs;
        this.jogoIDs = jogoIDs;
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

    public int getNumVitorias() {
        return numVitorias;
    }

    public void setNumVitorias(int numVitorias) {
        this.numVitorias = numVitorias;
    }

    public int getNumEmpates() {
        return numEmpates;
    }

    public void setNumEmpates(int numEmpates) {
        this.numEmpates = numEmpates;
    }

    public int getNumDerrotas() {
        return numDerrotas;
    }

    public void setNumDerrotas(int numDerrotas) {
        this.numDerrotas = numDerrotas;
    }

    public Long getClubeID() {
        return clubeID;
    }

    public void setClubeID(Long clubeID) {
        this.clubeID = clubeID;
    }

    public List<Long> getJogadorIDs() {
        return jogadorIDs;
    }

    public void setJogadorIDs(List<Long> jogadorIDs) {
        this.jogadorIDs = jogadorIDs;
    }

    public List<Long> getJogoIDs() {
        return jogoIDs;
    }

    public void setJogoIDs(List<Long> jogoIDs) {
        this.jogoIDs = jogoIDs;
    }
}
