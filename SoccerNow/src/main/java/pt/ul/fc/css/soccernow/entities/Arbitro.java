package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Arbitro")
public class Arbitro extends Utilizador {

    // no id atrtibute, it comes from the superclass Utilizador
    // no version attribute, it comes from the superclass Utilizador

    @Column(nullable = true)
    private boolean certificado;

    @ManyToMany(mappedBy = "arbitros")
    private List<Jogo> jogos = new ArrayList<>();

    // required empty constructor
    protected Arbitro() {}

    public Arbitro(String nome, String email, String password, boolean certificado){
        super(nome, email, password);
        this.certificado = certificado;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void addJogo(Jogo jogo) {
        jogos.add(jogo);
    }
}
