package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<Business, DefaultWeightedEdge> grafo ;
	private List<Business> vertici ;
	private Map<String, Business> verticiIdMap ;
	private ArrayList<String> cities;
	private List<Adiacenza> adiacenze;
	private List<Business> percorsoBest;
	
	
	public List<String> getAllCities() {
		YelpDao dao = new YelpDao() ;	
		if(cities==null){
		cities= new ArrayList<String>(dao.getCities());
		}
		return this.cities;
	}
	
	public void creaGrafo(String c){
		YelpDao dao = new YelpDao() ;
		//aggiunta vertici
		vertici = new ArrayList<Business>(dao.getVertici(c));
		verticiIdMap = new HashMap<String, Business>();
		adiacenze = new ArrayList<Adiacenza>();
		grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(Business b : vertici) {
			verticiIdMap.put(b.getBusinessId(), b);
		}
		for(Business b : vertici) {
			this.grafo.addVertex(b);
		}
		
		for(Business b1 : vertici) {
			for(Business b2 : vertici) {
				if(!b1.equals(b2)) {
					Double peso = LatLngTool.distance(b1.getPlace(), b2.getPlace(), LengthUnit.KILOMETER);
					Graphs.addEdge(grafo, b1, b2, peso);
					 Adiacenza a = new Adiacenza(b1, b2, peso); 
					if(!adiacenze.contains(a))
					  adiacenze.add(a);
				}
		}}

		
	}
	
	
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String distante(Business b1) {
		String s ="";
		Business lontano=null;
		ArrayList<Adiacenza> adiacenzeO = new ArrayList <Adiacenza>();
		for(Adiacenza a : adiacenze) {
			if(a.getB1().equals(b1)||a.getB2().equals(b1)) {
				adiacenzeO.add(a);
			}
		}
		Collections.sort(adiacenzeO);
		Adiacenza ad=adiacenzeO.get(adiacenzeO.size()-1);
		if(ad.getB1().equals(b1))
			lontano=ad.getB2();
		if(ad.getB2().equals(b1))
			lontano=ad.getB1();
		s+=lontano.getBusinessName()+" "+ad.getPeso();
		return s;
	}

	public List<Business> getVertici() {
		return this.vertici;
	}
	
	//ricorsione
	public String calcolaPercorso(Business b1, Business b2, double media) {
		    String s = "";
			this.percorsoBest = new ArrayList<Business>();
			
			List<Business> parziale = new ArrayList<Business>() ;
			parziale.add(b1) ;
			
			cerca(parziale, 1, b2, media) ;
			for(Business b :percorsoBest)
			  s+="\n"+b;
			return s ;
		}
		
		private void cerca(List<Business> parziale, int livello, Business arrivo, double soglia) {
	    Business ultimo = parziale.get(parziale.size()-1) ;
			
			// caso terminale: ho trovato l'arrivo
			if(ultimo.equals(arrivo)) {
				if(this.percorsoBest==null) {
					this.percorsoBest = new ArrayList<>(parziale) ;
					return ;
				} else if( parziale.size() > this.percorsoBest.size() ) {
					this.percorsoBest = new ArrayList<>(parziale) ;
					return ;
				} else {
					return ;
				}
			}
			

			for(Business e: Graphs.neighborListOf(this.grafo, ultimo)) {
				if(e.getStars()>=soglia) {			
					if(!parziale.contains(e)) { // evita i cicli
						parziale.add(e);
						cerca(parziale, livello + 1, arrivo, soglia);
						parziale.remove(parziale.size()-1) ;
					}
				}
			}	
		}
		

	
}
