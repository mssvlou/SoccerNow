package pt.ul.fc.css.soccernow.dto;

import java.util.Map;

public class EstatisticasDTO {
    private Long id;
    private Long jogoID;
    Map<Long, Integer> resultado;
    private Long equipaVitoriosa;
    Map<Long, Integer> golos;
    Map<Long, String> cartoesAtribuidos;

    public EstatisticasDTO() {}

    public EstatisticasDTO(Long jogoID, Map<Long, Integer> resultado, Long equipaVitoriosa, Map<Long, Integer> golos,
            Map<Long, String> cartoesAtribuidos) {
        this.jogoID = jogoID;
        this.resultado = resultado;
        this.equipaVitoriosa = equipaVitoriosa;
        this.golos = golos;
        this.cartoesAtribuidos = cartoesAtribuidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJogoID() {
        return jogoID;
    }

    public void setJogoID(Long jogoID) {
        this.jogoID = jogoID;
    }

    public Map<Long, Integer> getResultado() {
        return resultado;
    }

    public void setResultado(Map<Long, Integer> resultado) {
        this.resultado = resultado;
    }

    public Long getEquipaVitoriosa() {
        return equipaVitoriosa;
    }

    public void setEquipaVitoriosa(Long equipaVitoriosa) {
        this.equipaVitoriosa = equipaVitoriosa;
    }

    public Map<Long, Integer> getGolos() {
        return golos;
    }

    public void setGolos(Map<Long, Integer> golos) {
        this.golos = golos;
    }

    public Map<Long, String> getCartoesAtribuidos() {
        return cartoesAtribuidos;
    }

    public void setCartoesAtribuidos(Map<Long, String> cartoesAtribuidos) {
        this.cartoesAtribuidos = cartoesAtribuidos;
    }

}
