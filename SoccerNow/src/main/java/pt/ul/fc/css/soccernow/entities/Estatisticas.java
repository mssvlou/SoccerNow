package pt.ul.fc.css.soccernow.entities;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;

@Entity
public class Estatisticas {

    public enum Cartao {
        AMARELO, VERMELHO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // no version attribute, estatisticas won't be updated after creation

    @OneToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @ElementCollection
    @MapKeyJoinColumn(name = "equipa_id")
    @Column(name = "resultado")
    private Map<Equipa, Integer> resultado = new HashMap<>();

    @ManyToOne
    private Equipa equipaVitoriosa;

    @ElementCollection
    @MapKeyJoinColumn(name = "jogador_id")
    @Column(name = "golos")
    private Map<Jogador, Integer> golos = new HashMap<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @MapKeyJoinColumn(name = "jogador_id")
    @Column(name = "cartoesAtribuidos")
    private Map<Jogador, Cartao> cartoesAtribuidos = new HashMap<>();

    // required empty constructor
    protected Estatisticas() {}

    public Estatisticas(Jogo jogo, Map<Equipa, Integer> resultado, Equipa equipaVitoriosa, Map<Jogador, Integer> golos,
            Map<Jogador, Cartao> cartoesAtribuidos) {
        this.jogo = jogo;
        this.resultado = resultado;
        this.equipaVitoriosa = equipaVitoriosa;
        this.golos = golos;
        this.cartoesAtribuidos = cartoesAtribuidos;
    }

    public Long getId() {
        return id;
    }

    public Jogo getJogo() {
        return jogo;
    }
    
    public Map<Equipa, Integer> getResultado() {
        return resultado;
    }

    public Equipa getEquipaVitoriosa() {
        return equipaVitoriosa;
    }

    public Map<Jogador, Integer> getGolos() {
        return golos;
    }

    public Map<Jogador, Cartao> getCartoesAtribuidos() {
        return cartoesAtribuidos;
    }
}
