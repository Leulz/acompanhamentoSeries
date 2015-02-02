import static org.junit.Assert.*;
import models.Episodio;
import models.Serie;
import models.Temporada;

import org.junit.Before;
import org.junit.Test;


public class SerieTest {
	String nomeSerie;
	Serie serie;
	Temporada temporada;
	
	
	@Before
	public void iniciaSerie(){
		nomeSerie = "Arrow";
		serie = new Serie(nomeSerie);
		temporada = new Temporada(1, serie);
		temporada.addEpisodio(new Episodio("Pilot", temporada, 1));
		temporada.addEpisodio(new Episodio("Pilot2", temporada, 2));
		temporada.addEpisodio(new Episodio("Pilot3", temporada, 3));
		serie.addTemporada(temporada);
		
		temporada = new Temporada(2, serie);
		temporada.addEpisodio(new Episodio("Pilot2.1", temporada, 1));
		temporada.addEpisodio(new Episodio("Pilot2.2", temporada, 2));
		temporada.addEpisodio(new Episodio("Pilot2.3", temporada, 3));
		serie.addTemporada(temporada);
		
		temporada = new Temporada(3, serie);
		temporada.addEpisodio(new Episodio("Pilot3.1", temporada, 1));
		temporada.addEpisodio(new Episodio("Pilot3.2", temporada, 2));
		temporada.addEpisodio(new Episodio("Pilot3.3", temporada, 3));
		serie.addTemporada(temporada);
	}
	
	@Test
	public void construtorSerieTest(){
		assertTrue(serie.getNome().equals(nomeSerie));
		assertTrue(serie.getQtdTemporadas() == 3);
		
		assertTrue(serie.getTemporada(1).getQtdEpisodios() == 3);
		assertTrue(serie.getTemporada(2).getQtdEpisodios() == 3);
		assertTrue(serie.getTemporada(3).getQtdEpisodios() == 3);
	}
	
	
}
