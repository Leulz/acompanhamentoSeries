import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.dao.GenericDAO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;


public class Global extends GlobalSettings {

	private static GenericDAO dao = new GenericDAO();
	private static final int SERIE = 0;
	private static final int TEMPORADA = 1;
	private static final int EPISODIONUM = 2;
	private static final int EPISODIONOME = 3;
	
	private List<Serie> series = new ArrayList<>();
	
	@Override
	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				try {
					series = dao.findAllByClassName("Serie");
					if (series.size() == 0){
						readCSV();
					}
				} catch (Exception ex) {
					Logger.debug(ex.getMessage());
				}				
			}
		});
	}
	
	@Override
	public void onStop(Application app){
	    JPA.withTransaction(new play.libs.F.Callback0() {
	    @Override
	    public void invoke() throws Throwable {
	        Logger.info("Aplicação finalizando...");
	        try {
		        series = dao.findAllByClassName("Serie");
	
		        for (Serie serie: series) {
		        dao.removeById(Serie.class, serie.getId());
	        }
	       } catch (Exception ex) {
	    	   Logger.debug("Problema na finalização: "+ex.getMessage());
	       }
	    }}); 
	}
	
	private void readCSV() throws IOException{
		File csv = new File("conf/seriesFinalFile.csv");
		BufferedReader br = new BufferedReader(new FileReader(csv)); 
		String line = br.readLine();
		String[] linha = line.split(",");
		
		String serieLinha;
		int temporadaLinha;
		String episodioLinha;
		
		String serieAtualNome = linha[SERIE];
		int temporadaAtualNum = TEMPORADA;
		int epLinhaNum = Integer.parseInt(linha[EPISODIONUM]);
		String episodioAtual = linha[EPISODIONOME];
		
		Serie serieAtual = new Serie(serieAtualNome);

		Temporada temporadaAtual = new Temporada(TEMPORADA, serieAtual);
		Episodio epAtual = new Episodio(episodioAtual, temporadaAtual, epLinhaNum);
		temporadaAtual.addEpisodio(epAtual);
		serieAtual.addTemporada(temporadaAtual);
		
		while((line = br.readLine()) != null){
			linha = line.split(",");
			
			if(linha.length > EPISODIONOME){
				serieLinha = linha[SERIE];
				temporadaLinha = Integer.parseInt(linha[TEMPORADA]);
				epLinhaNum = Integer.parseInt(linha[EPISODIONUM]);
				episodioLinha = linha[EPISODIONOME];
		
				if(serieLinha.equals(serieAtualNome)){
					if(temporadaLinha == temporadaAtualNum){
						if(!episodioLinha.equals(episodioAtual)){
							serieAtual.addEpisodio(new Episodio(episodioLinha, temporadaAtual, epLinhaNum));
							episodioAtual = episodioLinha;
						}
					}
					else{
						temporadaAtual = new Temporada(temporadaLinha, serieAtual);
						serieAtual.addTemporada(temporadaAtual);
						serieAtual.addEpisodio(new Episodio(episodioLinha, temporadaAtual, epLinhaNum));
						temporadaAtualNum = temporadaLinha;
					}
				}
				else{
					dao.persist(serieAtual);
					
					serieAtualNome = serieLinha;
					temporadaAtualNum = TEMPORADA;
					episodioAtual = episodioLinha;
					serieAtual = new Serie(serieAtualNome);
					temporadaAtual = new Temporada(1, serieAtual);
					epAtual = new Episodio(episodioAtual, temporadaAtual, epLinhaNum);
					temporadaAtual.addEpisodio(epAtual);
					serieAtual.addTemporada(temporadaAtual);
					
					Logger.info(serieLinha + " adicionada ao BD");
				}
			}
		}
		dao.flush();
		br.close();
	}
	
}
