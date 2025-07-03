package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

@Entity
public class Clube {

    public enum Conquista {
        PRIMEIRO_LUGAR, SEGUNDO_LUGAR, TERCEIRO_LUGAR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(unique = true, nullable = false)
    private String nome;

    @ElementCollection
    @CollectionTable(name = "clube_conquistas", joinColumns = @JoinColumn(name = "clube_id"))
    @MapKeyColumn(name = "conquista")
    @Enumerated(EnumType.STRING)
    @Column(name = "obtida")
    private Map<Conquista,Boolean> conquistas = new HashMap<>();
    
    @OneToMany(mappedBy = "clube")
    private List<Jogador> jogadores = new ArrayList<>();

    @OneToMany(mappedBy = "clube")
    private List<Equipa> equipas = new ArrayList<>();

    @ManyToMany(mappedBy = "clubes")
    private List<Campeonato> campeonatos = new ArrayList<>();

    // required empty constructor
    protected Clube() {}

    public Clube(String nome) {
        this.nome = nome;
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

    public Map<Conquista,Boolean> getConquistas() {
        return conquistas;
    }

    public void putConquista(Conquista conquista, Boolean obtida) {
        conquistas.put(conquista,obtida);
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void addJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public void removeJogador(Jogador jogador) {
        jogadores.remove(jogador);
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public void addEquipa(Equipa equipa) {
        equipas.add(equipa);
    }

    public void removeEquipa(Equipa equipa) {
        equipas.remove(equipa);
    }

    public List<Campeonato> getCampeonatos() {
        return campeonatos;
    }

    public void addCampeonato(Campeonato campeonato) {
        campeonatos.add(campeonato);
    }

    public void removeCampeonato(Campeonato campeonato) {
        campeonatos.remove(campeonato);
    }
}
