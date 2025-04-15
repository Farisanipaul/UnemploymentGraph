package csc03a.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import csc03a.graph.Graph.Vertex;

public class UnemploymentNode<T extends Comparable<T>> extends Vertex<T>{
	
	private ArrayList<UnemploymentNode<T>> children;
	
	private double distace; 
	
    public UnemploymentNode(T value, int weight) {
		super(value, weight);
		children = new ArrayList<>();
	}

	public UnemploymentNode(T value) {
		super(value);
		children = new ArrayList<>();
	}
	
	public UnemploymentNode(Vertex<T> vertex) {
		super(vertex);
		children = new ArrayList<>();
	}
	
	public void addChildNode(UnemploymentNode<T> node) {
		children.add(node);
	}
	
	public ArrayList<UnemploymentNode<T>> getChildNodes() {
		//ListIterator variable
		return children;
	}

	public void setDistance(double newDistance) {
		// TODO Auto-generated method stub
		distace = newDistance;
	}
	
	public double getDistance() {
		return distace;
	}

}
