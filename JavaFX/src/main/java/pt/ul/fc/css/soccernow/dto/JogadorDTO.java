package pt.ul.fc.css.soccernow.dto;

import java.util.List;
import java.util.Map;

public class JogadorDTO extends UtilizadorDTO {
    private String posicao;
    private int numGolosMarcados;
    private Map<String, Integer> cartoesRecebidos;
    private Long clubeID;
    private List<Long> equipaIDs;

    public JogadorDTO() {}

    public JogadorDTO(Long id, String nome, String email, String password, String posicao, int numGolosMarcados,
            Map<String, Integer> cartoesRecebidos,
            Long clubeID, List<Long> equipaIDs) {
        super(id, nome, email, password);
        this.posicao = posicao;
        this.numGolosMarcados = numGolosMarcados;
        this.cartoesRecebidos = cartoesRecebidos;
        this.clubeID = clubeID;
        this.equipaIDs = equipaIDs;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public int getNumGolosMarcados() {
        return numGolosMarcados;
    }

    public void setNumGolosMarcados(int numGolosMarcados) {
        this.numGolosMarcados = numGolosMarcados;
    }

    public Map<String, Integer> getCartoesRecebidos() {
        return cartoesRecebidos;
    }

    public void setCartoesRecebidos(Map<String, Integer> cartoesRecebidos) {
        this.cartoesRecebidos = cartoesRecebidos;
    }

    public Long getClubeID() {
        return clubeID;
    }

    public void setClubeID(Long clubeID) {
        this.clubeID = clubeID;
    }

    public List<Long> getEquipaIDs() {
        return equipaIDs;
    }

    public void setEquipaIDs(List<Long> equipaIDs) {
        this.equipaIDs = equipaIDs;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", super.getNome(), posicao);
    }
}
