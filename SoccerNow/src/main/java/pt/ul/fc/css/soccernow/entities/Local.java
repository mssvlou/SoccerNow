package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String morada;

    @Column(nullable = false, unique = true)
    private String codigoPostal;

    @OneToMany(mappedBy = "local")
    private List<Jogo> jogos = new ArrayList<>();

    // required empty constructor
    protected Local() {}

    public Local(String nome, String morada, String codigoPostal) {
        this.nome = nome;
        this.morada = morada;
        this.codigoPostal = codigoPostal;
    }

    public Long getId() {
        return id;
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

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void addJogo(Jogo jogo) {
        jogos.add(jogo);
    }
}
