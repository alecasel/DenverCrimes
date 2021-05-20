/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxCategoria"
	private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

	@FXML // fx:id="boxMese"
	private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="boxArco"
	private ComboBox<Arco> boxArco; // Value injected by FXMLLoader

	@FXML // fx:id="btnPercorso"
	private Button btnPercorso; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	Arco a = this.boxArco.getValue();
    	
    	if (a == null) {
			txtResult.appendText("Seleziona un arco");
		}
    	
    	String src = a.getVertex1();
    	String dst = a.getVertex2();
    	
    	List<String> route = this.model.findRoute(src, dst);
    	
    	for (String string : route) {
    		txtResult.appendText(string + "\n");
		}
    		
    }

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();

		String category = this.boxCategoria.getValue();
		Integer month = this.boxMese.getValue();

		if (category == null || month == null) {
			txtResult.appendText("Selezionare i valori di input");
			return;
		}

		this.model.createGraph(category, month);

		List<Arco> archi = this.model.getArchi();
		// Stampa degli archi
		for (Arco a : archi) {
			txtResult.appendText(a.toString());
		}

		/*
		 * PUNTO 2 !!! A partire dal grafo calcolato nel punto precedente, si permetta
		 * all’utente di selezionare dalla tendina uno degli archi sopra mostrati. Lo
		 * metto qui perché ho già tutti gli archi a disposizione
		 */
		boxArco.getItems().addAll(archi);

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;

		// Riempire le tendine: vado nel DAO
		this.boxCategoria.getItems().addAll(model.getCategories());

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 13; i++) {
			list.add(i);
		}
		this.boxMese.getItems().addAll(list);
	}
}
