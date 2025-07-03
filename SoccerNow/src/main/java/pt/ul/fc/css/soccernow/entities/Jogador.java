package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import pt.ul.fc.css.soccernow.entities.Estatisticas.Cartao;

@Entity
@DiscriminatorValue("Jogador")
public class Jogador extends Utilizador {

    public enum Posicao {
        GUARDA_REDES, FIXO, ALA, PIVO
    }

    // no id attribute, it comes from the superclass Utilizador
    // no version attribute, it comes from the superclass Utilizador

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Posicao posicao;

    @Column
    private int numGolosMarcados;

    @ElementCollection
    @CollectionTable(name = "jogador_cartoes", joinColumns = @JoinColumn(name = "jogador_id"))
    @MapKeyColumn(name = "cartao")
    @Enumerated(EnumType.STRING)
    @Column(name = "quantidade")
    private Map<Cartao, Integer> cartoesRecebidos = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

    @ManyToMany(mappedBy = "jogadores")
    private List<Equipa> equipas = new ArrayList<>();

    // required empty constructor
    protected Jogador() {}

    public Jogador(String nome, String email, String password, Posicao posicao, Clube clube) {
        super(nome, email, password);
        this.posicao = posicao;
        this.clube = clube;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public int getNumGolosMarcados() {
        return numGolosMarcados;
    }

    public void setNumGolosMarcados(int numGolosMarcados) {
        this.numGolosMarcados = numGolosMarcados;
    }

    public Map<Cartao, Integer> getCartoesRecebidos() {
        return cartoesRecebidos;
    }

    public void setCartoesRecebidos(Map<Cartao, Integer> cartoesRecebidos) {
        this.cartoesRecebidos = cartoesRecebidos;
    }

    public Clube getClube() {
        return clube;
    }

    public void setClube(Clube clube) {
        this.clube = clube;
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
}
