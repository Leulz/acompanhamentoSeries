package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import play.Logger;

@Entity
public class Temporada implements Comparable<Temporada>{
	@Transient
	private int completa = 1;
	@Transient
	private int incompleta = 0;
	@Transient
	private int totalIncompleta = -1;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private int numero;
	@Column
	private int status;
	@Column
	private int qtdEpisodios;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Serie serie;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	@JoinColumn(name="TEMP_EPS")
	private List<Episodio> episodios;
	
	@ElementCollection
	List<Integer> ordenacaoEpisodios;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="selecao_id")
	SelecionadorProximoEpisodio selecionadorProximoEpisodio;

	public Temporada() {
		status = -1;
		episodios = new ArrayList<>();
		qtdEpisodios = episodios.size();
		this.setSelecionadorProximoEpisodio(new SelecionadorPadrao());
		ordenacaoEpisodios = new ArrayList<Integer>();
	}	

	public Temporada(int num, Serie serie){
		this();
		this.numero = num;
		this.serie = serie;
	}
	
	public void addEpisodio(Episodio ep){
		episodios.add(ep);
		qtdEpisodios++;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<Episodio> getEpisodios() {
		Collections.sort(episodios);
		return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	
	public Episodio getEpisodio(int num){		
		return episodios.get(num-1);
	}

	public int getQtdEpisodios() {
		return qtdEpisodios;
	}

	public void setQtdEpisodios(int qtdEpisodios) {
		this.qtdEpisodios = qtdEpisodios;
	}
	
	public String getProximoEpisodio(){
		return selecionadorProximoEpisodio.getProximoEpisodioASerAssistido(this);
	}
	
	public List<Integer> getOrdenacaoEpisodios() {
		return ordenacaoEpisodios;
	}
	
	public int getQtdAssistidos() {
		int contadorEpisodios = 0;
		
		for (Episodio episodio : episodios) {
			if (episodio.isAssistido()) {
				contadorEpisodios = contadorEpisodios + 1;
			}
		}
		return contadorEpisodios;
	}
	
	public int verificaStatus(){
		int cont = 0;
		
		for (Episodio ep : this.getEpisodios()) {
			if(ep.isAssistido()){
				cont += 1;
			}
		}
		
		if(cont == 0) {
			status = totalIncompleta;
		}
		if(cont > 0) {
			status = incompleta;
		}
		if(cont == qtdEpisodios || episodios.get(qtdEpisodios-1).isAssistido()) {
			status = completa;
		}
		
		return status;
	}
	
	public SelecionadorProximoEpisodio getSelecionadorProximoEpisodio() {
		return selecionadorProximoEpisodio;
	}

	public void setSelecionadorProximoEpisodio(
			SelecionadorProximoEpisodio selecionadorProximoEpisodio) {
		this.selecionadorProximoEpisodio = selecionadorProximoEpisodio;
	}
	
	public void addOrdenacaoEpisodios(Integer numeroEpisodio) {
		this.ordenacaoEpisodios.add(numeroEpisodio);
	}
	
	public void removeOrdenacaoEpisodios(Integer numeroEpisodio) {
		int indiceEpisodio = this.ordenacaoEpisodios.indexOf(numeroEpisodio);
		this.ordenacaoEpisodios.remove(indiceEpisodio);		
	}

	@Override
	public int compareTo(Temporada outraTemporada) {
		if (this.getNumero()>outraTemporada.getNumero()){
			return 1;
		} else if (this.getNumero()<outraTemporada.getNumero()){
			return -1;
		}
		return 0;
	}	
}
