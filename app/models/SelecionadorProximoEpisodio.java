package models;

public abstract class SelecionadorProximoEpisodio {	
	int numeroLimiteEpisodios = 0;
	
	/**
	 * Retorna o nome do próximo episódio a ser assistido.
	 * 
	 * @param temporada
	 *            A temporada a ser analisada.
	 * @return O nome do próximo episódio a ser assistido.
	 */
	public abstract String getProximoEpisodioASerAssistido(Temporada temporada);

	/**
	 * Define o número máximo de episódios a serem assistidos após o próximo a
	 * ser assistido. Se o usuário assistir a mais episódios do que o limite, o
	 * sistema não mais recomendará um próximo episódio a ser assistido.
	 * 
	 * @param numeroLimiteEpisodios
	 *            O número limite de episódios.
	 */
	public void desativarSelecao(int numeroLimiteEpisodios) {
		this.numeroLimiteEpisodios = numeroLimiteEpisodios;
	}
	/**
	 * Define se é para haver ou não uma recomendação de próximo episódio, 
	 * levando em conta o limite determinado.
	 * @param episodio O episódio 
	 * @return
	 */
	public abstract boolean definirSeHaRecomendacaoDeProximoEpisodio(Episodio episodio);
}
