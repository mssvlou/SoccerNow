package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class LocalDTO {
    private Long id;
    private String nome;
    private String morada;
    private String codigoPostal;
    private List<Long> jogosID;

    public LocalDTO() {}

    public LocalDTO(Long id, String nome, String morada, String codigoPostal, List<Long> jogosID) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.codigoPostal = codigoPostal;
        this.jogosID = jogosID;
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

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public List<Long> getJogosID() {
        return jogosID;
    }

    public void setJogosID(List<Long> jogosID) {
        this.jogosID = jogosID;
    }

    @Override
    public String toString() {
        return nome;
    }
}
