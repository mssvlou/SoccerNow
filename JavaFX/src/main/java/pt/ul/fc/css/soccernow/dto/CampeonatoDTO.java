package pt.ul.fc.css.soccernow.dto;

import java.util.List;
import java.util.Map;

public class CampeonatoDTO {
    private Long id;
    private String nome;
    private String formato;
    private boolean concluido;
    private List<Long> clubes;
    private List<Long> jogos;
    private Map<Long,Integer> pontuacao;
    private Map<Long,Integer> classificacao; 

    public CampeonatoDTO() {}

    public CampeonatoDTO(Long id, String nome, String formato, boolean concluido, List<Long> clubes, List<Long> jogos, Map<Long,Integer> pontuacao, Map<Long,Integer> classificacao) {
        this.id = id;
        this.nome = nome;
        this.formato = formato;
        this.concluido = concluido;
        this.clubes = clubes;
        this.jogos = jogos;
        this.pontuacao = pontuacao;
        this.classificacao = classificacao;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public List<Long> getClubes() {
        return clubes;
    }

    public void setClubes(List<Long> clubes) {
        this.clubes = clubes;
    }

    public List<Long> getJogos() {
        return jogos;
    }

    public void setJogos(List<Long> jogos) {
        this.jogos = jogos;
    }

    public Map<Long, Integer> getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Map<Long, Integer> pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Map<Long, Integer> getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Map<Long, Integer> classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public String toString() {
        return nome;
    }
}
