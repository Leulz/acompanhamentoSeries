package models;

import java.util.List;

import javax.persistence.Entity;

/**
 * Seleciona o próximo episódio a ser assistido de uma temporada. Tal episódio
 * será o primeiro episódio não assistido, na ordem cronológica.
 * 
 * @author Léo Vital
 *
 */
@Entity
public class SelecionadorCronologico extends SelecionadorProximoEpisodio {
	
	public SelecionadorCronologico() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getProximoEpisodioASerAssistido(Temporada temporada) {
		List<Episodio> episodios = temporada.getEpisodios();

		String result = episodios.get(0).getNumero() + " - "
				+ episodios.get(0).getNome();
		
		if (temporada.getQtdAssistidos() == temporada.getQtdEpisodios()) {
			result = "Você já assistiu a todos os episódios da temporada.";
		} else {
			for (int i = 0; i < episodios.size(); i++) {
				if (!episodios.get(i).isAssistido()) {
					result = episodios.get(i).getNumero() + " - "
							+ episodios.get(i).getNome();
					if (numeroLimiteEpisodios == 0) {
						break;
					} else if (i == 0) {
						if (temporada.getQtdAssistidos() >= numeroLimiteEpisodios) {
							result = "Sem recomendação de próximo episódio";
						}
					} else {
						if (!definirSeHaRecomendacaoDeProximoEpisodio(episodios
								.get(i - 1))) {
							result = "Sem recomendação de próximo episódio";
						}
					}
					break;
				}
			}
		}
		return result;
	}

	@Override
	public boolean definirSeHaRecomendacaoDeProximoEpisodio(Episodio episodio) {
		int numeroUltimoEpiAssistido = episodio.getNumero();
		List<Integer> ordenacaoEpis = episodio.getTemporada().getOrdenacaoEpisodios();
		int indice = ordenacaoEpis.indexOf(numeroUltimoEpiAssistido);
		int contador = 0;
		for (int j = indice + 1; j < ordenacaoEpis.size(); j++) {
			if (ordenacaoEpis.get(j) > episodio.getNumero()) {
				contador = contador + 1;
			}
			if (contador == numeroLimiteEpisodios) {
				return false;
			}
		}
		return true;
	}
}
