package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Serie {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String nome;
	@Column
	private boolean acompanhada;
	@Column
	private int qtdTemporadas;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="TEMPS")
	private List<Temporada> temporadas;

	public Serie() {
		temporadas = new ArrayList<>();
		acompanhada = false;
		qtdTemporadas = temporadas.size();
	}
	
	public Serie(String nome){
		this();	
		this.nome = nome;
	}
	
	public void addTemporada(Temporada temp){
		temporadas.add(temp);
		qtdTemporadas++;
	}
	
	public void addEpisodio(Episodio ep){
		temporadas.get(ep.getTemporada().getNumero()-1).addEpisodio(ep);
	}
	
	public Temporada getTemporada(int num){
		return temporadas.get(num-1);
	}
	
	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Temporada> getTemporadas() {
		return temporadas;
	}

	private void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}
	
	public boolean isAcompanhada() {
		return acompanhada;
	}
	
	public void setAcompanhada(boolean acompanhada) {
		this.acompanhada = acompanhada;
	}

	public int getQtdTemporadas() {
		return qtdTemporadas;
	}

	public void setQtdTemporadas(int qtdTemporadas) {
		this.qtdTemporadas = qtdTemporadas;
	}
	
}

