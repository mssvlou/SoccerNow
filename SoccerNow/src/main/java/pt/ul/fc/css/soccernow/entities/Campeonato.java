package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

@Entity
public class Campeonato {

    public enum Formato {
        PONTOS, ELIMINATORIA
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(unique = true, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Formato formato;

    @Column(nullable = false)
    private boolean concluido;

    @ManyToMany
    @JoinTable(
        name = "campeonato_clube",
        joinColumns = @JoinColumn(name = "campeonato_id"),
        inverseJoinColumns = @JoinColumn(name = "clube_id")
    )
    private List<Clube> clubes = new ArrayList<>();

    @OneToMany(mappedBy = "campeonato")
    private List<Jogo> jogos = new ArrayList<>();

    @ElementCollection
    @MapKeyJoinColumn(name = "clube_id")
    @Column(name = "pontuacao")
    private Map<Clube,Integer> pontuacao = new HashMap<>();

    @ElementCollection
    @MapKeyJoinColumn(name = "clube_id")
    @Column(name = "classificacao")
    private Map<Clube,Integer> classificacao = new HashMap<>(); 

    // required empty constructor
    protected Campeonato() {}

    public Campeonato(String nome, Formato formato, List<Clube> clubes) {
        this.nome = nome;
        this.formato = formato;
        this.clubes = clubes;
        this.concluido = false;
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

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
    
    public List<Clube> getClubes() {
        return clubes;
    }

    public void addClube(Clube clube) {
        clubes.add(clube);
    }

    public void removeClube(Clube clube) {
        clubes.remove(clube);
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void addJogo(Jogo jogo) {
        jogos.add(jogo);
    }

    public void removeJogo(Jogo jogo) {
        jogos.remove(jogo);
    }

    public Map<Clube, Integer> getPontuacao() {
        return pontuacao;
    }

    public void putPontuacao(Clube clube, int pontos) {
        pontuacao.put(clube,pontos);
    }
    
    public Map<Clube, Integer> getClassificacao() {
        return classificacao;
    }

    public void putClassificacao(Clube clube, int lugar) {
        classificacao.put(clube,lugar);
    }
}
