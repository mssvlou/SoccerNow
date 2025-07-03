package pt.ul.fc.css.soccernow.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class JogoDTO {
    private Long id;
    private LocalDate data;
    private LocalTime horario;
    private Long localID;
    private String estadoAtual;
    private Long campeonatoID;
    private List<Long> arbitroIDs;
    private Long arbitroPrincipalID;
    private List<Long> equipaIDs;
    private Long estatisticasID;

    public JogoDTO() {}

    public JogoDTO(Long id, LocalDate data, LocalTime horario, Long localID, String estadoAtual, Long campeonatoID, List<Long> arbitroIDs, Long arbitroPrincipalID, List<Long> equipaIDs, Long estatisticasID) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.localID = localID;
        this.estadoAtual = estadoAtual;
        this.campeonatoID = campeonatoID;
        this.arbitroIDs = arbitroIDs;
        this.arbitroPrincipalID = arbitroPrincipalID;
        this.equipaIDs =  equipaIDs;
        this.estatisticasID = estatisticasID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Long getLocalID() {
        return localID;
    }

    public void setLocalID(Long localID) {
        this.localID = localID;
    }

    public String getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(String estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public Long getCampeonatoID() {
        return campeonatoID;
    }
    
    public void setCampeonatoID(Long campeonatoID) {
        this.campeonatoID = campeonatoID;
    }

    public List<Long> getArbitroIDs() {
        return arbitroIDs;
    }

    public void setArbitroIDs(List<Long> arbitroIDs) {
        this.arbitroIDs = arbitroIDs;
    }

    public Long getArbitroPrincipalID() {
        return arbitroPrincipalID;
    }

    public void setArbitroPrincipalID(Long arbitroPrincipalID) {
        this.arbitroPrincipalID = arbitroPrincipalID;
    }

    public List<Long> getEquipaIDs() {
        return equipaIDs;
    }

    public void setEquipaIDs(List<Long> equipaIDs) {
        this.equipaIDs = equipaIDs;
    }

    public Long getEstatisticasID() {
        return estatisticasID;
    }

    public void setEstatisticasID(Long estatisticasID) {
        this.estatisticasID = estatisticasID;
    }
}
