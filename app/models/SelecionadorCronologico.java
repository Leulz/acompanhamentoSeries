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
		boolean result = true;
		Temporada temporada = episodio.getTemporada();

		int numeroUltimoAssistido = episodio.getNumero();
		List<Integer> ordenacao = temporada.getOrdenacaoEpisodios();
		int indiceOrdenacao = ordenacao.indexOf(numeroUltimoAssistido);
		int contadorEpisodios = 0;
		for (int j = indiceOrdenacao + 1; j < ordenacao.size(); j++) {
			if (ordenacao.get(j) > episodio.getNumero()) {
				contadorEpisodios = contadorEpisodios + 1;
			}
			if (contadorEpisodios == numeroLimiteEpisodios) {
				result = false;
				break;
			}
		}
		return result;
	}
}
