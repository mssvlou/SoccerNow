package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class ArbitroDTO extends UtilizadorDTO {
    private boolean certificado;
    private List<Long> jogoIDs;

    public ArbitroDTO() {}
    
    public ArbitroDTO(Long id, String nome, String email, String password, boolean certificado, List<Long> jogoIDs){
        super(id, nome, email, password);
        this.certificado = certificado;
        this.jogoIDs = jogoIDs;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public List<Long> getJogoIDs() {
        return jogoIDs;
    }

    public void setJogoIDs(List<Long> jogoIDs) {
        this.jogoIDs = jogoIDs;
    }

    @Override
    public String toString() {
        return String.format("Árbitro: %s", super.getNome());
    }
}
