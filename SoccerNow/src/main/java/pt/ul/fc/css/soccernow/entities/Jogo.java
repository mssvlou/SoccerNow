package pt.ul.fc.css.soccernow.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Jogo {

    public enum Estado {
        AGENDADO, CANCELADO, EM_CURSO, CONCLUIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(columnDefinition = "DATE")
    private LocalDate data;

    @Column(columnDefinition = "TIME")
    private LocalTime horario;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estadoAtual;

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @ManyToMany
    @JoinTable(name = "jogo_arbitros", joinColumns = @JoinColumn(name = "jogo_id"), inverseJoinColumns = @JoinColumn(name = "arbitros_id"))
    private List<Arbitro> arbitros = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "arbitro_principal_id")
    private Arbitro arbitroPrincipal;

    @ManyToMany
    @JoinTable(name = "jogo_equipas", joinColumns = @JoinColumn(name = "jogo_id"), inverseJoinColumns = @JoinColumn(name = "equipa_id"))
    private List<Equipa> equipas = new ArrayList<>();

    @OneToOne(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Estatisticas estatisticas;

    protected Jogo() {
    }

    public Jogo(LocalDate data, LocalTime horario, Local local, Campeonato campeonato, List<Equipa> equipas,
            List<Arbitro> arbitros, Arbitro arbitroPrincipal) {
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.campeonato = campeonato;
        this.equipas = equipas;
        this.arbitros = arbitros;
        this.arbitroPrincipal = arbitroPrincipal;
        estadoAtual = Estado.AGENDADO;
    }

    // getters & setters ...

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public Local getLocal() {
        return local;
    }

    public Estado getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(Estado estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void removeCampeonato() {
        campeonato = null;
    }

    public List<Arbitro> getArbitros() {
        return arbitros;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public Estatisticas getEstatisticas() {
        return estatisticas;
    }

    public void setEstatisticas(Estatisticas estatisticas) {
        this.estatisticas = estatisticas;
    }
}
