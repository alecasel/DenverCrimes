package it.polito.tdp.crimes.model;

public class Arco {
	
	private String vertex1;
	private String vertex2;
	private double weight;
	
	public Arco(String vertex1, String vertex2, double weight) {
		super();
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
	}
	public String getVertex1() {
		return vertex1;
	}
	public void setVertex1(String vertex1) {
		this.vertex1 = vertex1;
	}
	public String getVertex2() {
		return vertex2;
	}
	public void setVertex2(String vertex2) {
		this.vertex2 = vertex2;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "v1: " + vertex1 + ", v2: " + vertex2 + ", weight = " + weight + "\n";
	}
	
	

}