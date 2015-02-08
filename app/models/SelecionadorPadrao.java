package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Seleciona o próximo episódio a ser assistido de uma temporada. Tal episódio
 * será o episódio imediatamente seguinte ao último episódio assistido.
 * 
 * @author Léo Vital
 *
 */
@Entity
public class SelecionadorPadrao extends SelecionadorProximoEpisodio {	

	@Override
	public String getProximoEpisodioASerAssistido(Temporada temporada) {
		List<Episodio> episodios = temporada.getEpisodios();

		String result = episodios.get(0).getNumero() + " - "
				+ episodios.get(0).getNome();
		
		if (temporada.getQtdAssistidos() == temporada.getQtdEpisodios()) {
			result = "Você já assistiu a todos os episódios da temporada.";
		} else {
			for (int i = temporada.getQtdEpisodios() - 1; i >= 0; i--) {
				if (episodios.get(i).isAssistido()) {
					if ((i + 1) == temporada.getQtdEpisodios()) {
						result = "Você já assistiu ao último episódio desta temporada.";
						break;
					} else {
						result = episodios.get(i + 1).getNumero() + " - "
								+ episodios.get(i + 1).getNome();
					}
					if (numeroLimiteEpisodios == 0) {
						break;
					} else {
						if (!definirSeHaRecomendacaoDeProximoEpisodio(episodios
								.get(i))) {
							result = "Não há recomendação de próximo episódio.";
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
			if (ordenacao.get(j) < episodio.getNumero()) {
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
