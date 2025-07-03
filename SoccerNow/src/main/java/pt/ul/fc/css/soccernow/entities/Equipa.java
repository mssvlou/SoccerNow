package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Equipa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column
    private int numVitorias;

    @Column
    private int numEmpates;

    @Column
    private int numDerrotas;

    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

    @ManyToMany
    @JoinTable(name = "equipa_jogadores", joinColumns = @JoinColumn(name = "equipa_id"), inverseJoinColumns = @JoinColumn(name = "jogador_id"))
    private List<Jogador> jogadores = new ArrayList<>();

    @ManyToMany(mappedBy = "equipas")
    private List<Jogo> jogos = new ArrayList<>();

    // required empty constructor
    protected Equipa() {}

    public Equipa(String nome, Clube clube, List<Jogador> jogadores) {
        this.nome = nome;
        this.clube = clube;
        this.jogadores = jogadores;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNumVitorias() {
        return numVitorias;
    }

    public int getNumEmpates() {
        return numEmpates;
    }

    public int getNumDerrotas() {
        return numDerrotas;
    }

    public Clube getClube() {
        return clube;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setNumVitorias(int numVitorias) {
        this.numVitorias = numVitorias;
    }

    public void setNumEmpates(int numEmpates) {
        this.numEmpates = numEmpates;
    }

    public void setNumDerrotas(int numDerrotas) {
        this.numDerrotas = numDerrotas;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public void addJogo(Jogo jogo) {
        jogos.add(jogo);
    }
}
