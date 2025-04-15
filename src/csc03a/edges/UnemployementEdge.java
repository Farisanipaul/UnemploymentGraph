package csc03a.edges;

import csc03a.graph.Graph.Edge;
import csc03a.graph.Graph.Vertex;

public class UnemployementEdge<T extends Comparable<T>> extends Edge<T> {

	public UnemployementEdge(Edge<T> e) {
		super(e);
		
	}

	public UnemployementEdge(int cost, Vertex<T> from, Vertex<T> to) {
		super(cost, from, to);
		
	}

}
