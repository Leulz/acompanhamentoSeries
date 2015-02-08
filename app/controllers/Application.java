package controllers;

import java.util.List;

import models.Episodio;
import models.SelecionadorProximoEpisodio;
import models.Serie;
import models.Temporada;
import models.dao.GenericDAO;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	private static List<Serie> series;
	private static GenericDAO dao = new GenericDAO();
	
	@Transactional
	public static Result index() {
		String port = System.getenv("PORT");
		if (port==null || port.isEmpty()){
			port = "8080";
		}
		System.setProperty("server.port", port);
    	getSeriesFromDB();
        return ok(index.render(series));
    }
    
	@Transactional
	public static Result acompanhaSerie(Long id){
		Serie serie = getDAO().findByEntityId(Serie.class, id);
		serie.setAcompanhada(true);
		getDAO().merge(serie);
		getDAO().flush();
		
		Logger.info("Acompanhando " + serie.getNome());
		
		return redirect("/");
	}
	
	@Transactional
	public static Result desacompanhaSerie(Long id){
		Serie serie = getDAO().findByEntityId(Serie.class, id);
		serie.setAcompanhada(false);
		getDAO().merge(serie);
		getDAO().flush();
		
		Logger.info("Desacompanhou " + serie.getNome());
		
		return redirect("/");
	}
	
	@Transactional
	public static Result cancelaEpisodio(Long id){
		Episodio ep = getDAO().findByEntityId(Episodio.class, id);
		ep.setAssistido(false);
		Temporada temp = ep.getTemporada();
		int epiNum = ep.getNumero();
		temp.removeOrdenacaoEpisodios(epiNum);
		getDAO().merge(ep);
		getDAO().merge(temp);
		getDAO().flush();
		
		Logger.info("Cancelou ep " + ep.getNome());
		
		return redirect("/");
	}
	
	@Transactional
	public static Result setaSelecaoProximo() throws InstantiationException, IllegalAccessException {
		Form<Temporada> metaForm = Form.form(Temporada.class);
		Form<Temporada> filledForm = metaForm.bindFromRequest();
		String arg = filledForm.data().get("arg");
		Long id = Long.parseLong(filledForm.data().get("id"));
		Integer desativarRecomendacao = null;
		if (filledForm.data().get("desativarRecomendacao") != null) {
			desativarRecomendacao = Integer.parseInt(filledForm.data().get("desativarRecomendacao"));
		}		
		try {
			Temporada temp = getDAO().findByEntityId(Temporada.class, id);
			if (arg != null) {
				Class selecionador = Class.forName(arg);			
				temp.setSelecionadorProximoEpisodio((SelecionadorProximoEpisodio) selecionador.newInstance());
			}
			if (desativarRecomendacao != null) {
				if (temp.getSelecionadorProximoEpisodio().getNumeroLimiteEpisodios() == 0) {
					temp.getSelecionadorProximoEpisodio().desativarSelecao(desativarRecomendacao);
				} else {
					temp.getSelecionadorProximoEpisodio().desativarSelecao(0);
				}
			}
			getDAO().merge(temp);
			getDAO().flush();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return redirect("/");
	}
	
	@Transactional
	public static Result assisteEpisodio(Long id){
		Episodio ep = getDAO().findByEntityId(Episodio.class, id);
		Temporada temp = ep.getTemporada();
		int epiNum = ep.getNumero();
		temp.addOrdenacaoEpisodios(epiNum);
		Logger.debug(""+temp.getOrdenacaoEpisodios());
		ep.setAssistido(true);
		getDAO().merge(ep);
		getDAO().merge(temp);
		getDAO().flush();
		
		Logger.info("Assistiu ep " + ep.getNome());
		
		return redirect("/");
	}
	
    @Transactional
	private static void getSeriesFromDB(){
		series = getDAO().findAllByClassName("Serie");
		getDAO().flush();
	}
    
    public static GenericDAO getDAO(){
		return dao;
	}

}
