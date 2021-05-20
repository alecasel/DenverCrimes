package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	/*
	 * si costruisca un grafo semplice pesato non orientato, i cui VERTICI
	 * corrispondano ai tipi di reato (colonna ‘offense_type_id’)
	 */
	// colonna ‘offense_type_id’ è di tipo String, non è neanche un Object
	private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	private EventsDao dao;

	private List<String> bestRoute;

	public Model() {
		dao = new EventsDao();
	}

	public void createGraph(String category, int month) {
		this.graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// AGGIUNTA VERTICI
		// Vado nel DAO a creare un metodo che mi ritorni List<String>
		// Come nel DAO: devo passare gli stessi parametri
		Graphs.addAllVertices(graph, dao.getVertici(category, month));

		// AGGIUNTA ARCHI
		/*
		 * Gli ARCHI che collegano ciascuna coppia vertici (tipi di reato, diversi tra
		 * loro) hanno un peso pari al numero di quartieri distinti (neighborhood_id) in
		 * cui si siano verificati entrambi i tipi di reato.
		 */
		// Procedo allo stesso modo
		for (Arco a : dao.getArchi(category, month)) {
			if (this.graph.getEdge(a.getVertex1(), a.getVertex2()) == null) {
				Graphs.addEdgeWithVertices(graph, a.getVertex1(), a.getVertex2(), a.getWeight());
			}
		}

		System.out.println("vertici " + graph.vertexSet().size() + " archi " + this.graph.edgeSet().size());
	}

	/**
	 * Si visualizzi l’elenco di tutti gli archi il cui peso sia superiore al peso
	 * medio presente nel grafo. Per ogni arco si visualizzino i due tipi di reato
	 * (i due vertici) ed il peso stesso.
	 * 
	 * @return
	 */
	public List<Arco> getArchi() {

		// 1) Calcolo PESO MEDIO di TUTTI GLI ARCHI del grafo
		double avgWeight = 0.0;
		for (DefaultWeightedEdge e : graph.edgeSet()) {
			avgWeight += graph.getEdgeWeight(e);
		}
		avgWeight = avgWeight / graph.edgeSet().size();

		// 2) FILTRO: tengo solo ARCO i t.c. PESO(i) > PESO(medio)
		List<Arco> rslt = new ArrayList<Arco>();
		for (DefaultWeightedEdge e : graph.edgeSet()) {
			if (graph.getEdgeWeight(e) > avgWeight) {
				Arco a = new Arco(graph.getEdgeSource(e), graph.getEdgeTarget(e), graph.getEdgeWeight(e));
				rslt.add(a);
			}
		}

		return rslt;

	}

	// PUNTO 2
	/**
	 * Alla pressione del bottone ‘calcola percorso’ di calcoli e visualizzi un
	 * cammino aciclico semplice, che inizi e termini nei due vertici selezionati, e
	 * che tocchi il numero massimo di vertici. TROVARE IL PERCORSO MIGLIORE.
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public List<String> findRoute(String src, String dst) {

		// Dichiaro la soluzione migliore nel Model e la inizializzo qui
		bestRoute = new ArrayList<String>();

		// Dichiaro e inizializzo la soluzione parziale qui
		List<String> partial = new ArrayList<String>();

		// Sicuramente la partial conterrà come primo vertice il src
		partial.add(src);

		// Chiamo la ricorsione
		loadRecursion(partial, dst);

		return bestRoute;
	}

	private void loadRecursion(List<String> partial, String dst) {

		// Caso terminale
		if (partial.get(partial.size() - 1).equals(dst)) {
			// Prima di ritornare devo controllare se è il percorso migliore tra quelli
			// trovati
			if (partial.size() > this.bestRoute.size()) {
				// Sovrascrivo
				this.bestRoute = new ArrayList<String>(partial);
			}
			return;
		}

		// Altrimenti
		// Prendo tutti i vicini dell'ultimo vertice inserito:
		// Graphs.neighborListOf() sull'ultimo partial.get(partial.size()-1)
		/*
		 * Grafo ACICLICO = non possiamo mettere un vertice che abbiamo già visitato
		 * Controllo con contains()
		 */
		for (String neighbor : Graphs.neighborListOf(graph, partial.get(partial.size() - 1))) {
			if (!partial.contains(neighbor)) {
				partial.add(neighbor);
				loadRecursion(partial, dst);
				partial.remove(partial.size() - 1); // meglio rimuovere sempre per index
				/*
				 * BACKTRACKING perché dalla lista dei possibili vicini, provo a prenderne uno
				 * per volta e ogni volta confronto best con partial contenente il vicino aggiunto.
				 * Backtracking non nel lab 9 perché là volevamo esprimere tutti i nodi. Una volta 
				 * che il nodo era visitato non lo toglievo più. 
				 * Invece in questo caso lo tolgo perché cerco la soluzione migliore, invece là
				 * cercavo l'unica soluzione possibile.
				 */
			}
		}
	}
	
	public List<String> getCategories() {
		return dao.getCategories();
	}

}
