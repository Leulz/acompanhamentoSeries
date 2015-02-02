import static org.junit.Assert.*;
import models.Episodio;
import models.Serie;
import models.Temporada;

import org.junit.Before;
import org.junit.Test;


public class TemporadaTest {
	int num;
	Temporada temporada;
	
	@Before
	public void iniciaTemporada(){
		num = 1;
		temporada = new Temporada(num, new Serie("Arrow"));
		
		for (int i = 1; i <= 5; i++) {
			temporada.addEpisodio(new Episodio("Pilot"+i, temporada, i));
		}
	}
	
	@Test
	public void construtorTemporadaTest(){
		assertTrue(temporada.getNumero() == 1);
		assertTrue(temporada.getQtdEpisodios() == 5);
		assertTrue(temporada.getSerie().getNome().equals("Arrow"));
		assertTrue(temporada.getStatus() == -1);
	}
	
	@Test
	public void testaAddEp(){
		assertTrue(temporada.getQtdEpisodios() == 5);
		temporada.addEpisodio(new Episodio("Pilot6", temporada, 6));
		assertTrue(temporada.getQtdEpisodios() == 6);
	}
	
	@Test
	public void testaStatus(){
		assertTrue(temporada.getStatus() == -1);
		temporada.getEpisodio(1).setAssistido(true);
		assertTrue(temporada.getStatus() == 0);
		temporada.getEpisodio(2).setAssistido(true);
		assertTrue(temporada.getStatus() == 0);
		temporada.getEpisodio(3).setAssistido(true);
		temporada.getEpisodio(4).setAssistido(true);
		temporada.getEpisodio(5).setAssistido(true);
		assertTrue(temporada.getStatus() == 1);
	}
	
	@Test
	public void testaProximoEp(){
		assertTrue(temporada.getProximoEpisodio().equals("1 - Pilot1"));
		temporada.getEpisodio(2).setAssistido(true);
		assertTrue(temporada.getProximoEpisodio().equals("3 - Pilot3"));
		temporada.getEpisodio(5).setAssistido(true);
		assertTrue(temporada.getProximoEpisodio().equals("Você já assistiu ao último episódio desta temporada."));
	}
}
