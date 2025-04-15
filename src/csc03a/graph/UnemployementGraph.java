package csc03a.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import csc03a.nodes.UnemploymentNode;

public class UnemployementGraph<T extends Comparable<T>> extends Graph<T> {

	public UnemployementGraph() {
		super();
		
	} 

	public UnemployementGraph(Collection<Vertex<T>> vertices, Collection<Edge<T>> edges) {
		super(vertices, edges);
		
	}

	public UnemployementGraph(Graph<T> g) {
		super(g);

	}

	public UnemployementGraph(TYPE type, Collection<Vertex<T>> vertices, Collection<Edge<T>> edges) {
		super(type, vertices, edges);
		
	}

	public UnemployementGraph(TYPE type) {
		super(type);
		
	}
	
    public List<UnemploymentNode<T>> findShortestPath(UnemploymentNode<T> root, UnemploymentNode<T> target) {
        // Create a priority queue to store vertices based on their distances
        PriorityQueue<UnemploymentNode<T>> queue = new PriorityQueue<>(Comparator.comparingDouble(UnemploymentNode::getDistance));

        // Set the distance of the root node to 0 and add it to the queue
        root.setDistance(0);
        queue.add(root);

        // Create a map to track the shortest path from each vertex
        Map<UnemploymentNode<T>, UnemploymentNode<T>> shortestPathMap = new HashMap<>();
        shortestPathMap.put(root, null);

        // Process the vertices in the queue until it's empty
        while (!queue.isEmpty()) {
            // Remove the vertex with the minimum distance from the queue
            UnemploymentNode<T> currentVertex = queue.poll();

            // Stop if the target vertex is reached
            if (currentVertex.equals(target)) {
                break;
            }

            // Visit all neighboring vertices of the current vertex
            for (Edge<T> edge : currentVertex.getEdges()) {
            	UnemploymentNode<T> neighbor = (UnemploymentNode<T>) edge.getToVertex();
                double newDistance = currentVertex.getDistance() + edge.getCost();

                // Update the distance and shortest path if a shorter path is found
                if (newDistance < neighbor.getDistance()) {
                    queue.remove(neighbor); // Remove the neighbor from the queue to update its distance
                    neighbor.setDistance(newDistance);
                    queue.add(neighbor); // Add it back to the queue with the updated distance
                    shortestPathMap.put((UnemploymentNode<T>) neighbor, currentVertex);
                }
            }
        }

        // Build the shortest path by backtracking from the target vertex to the root
        List<UnemploymentNode<T>> shortestPath = new ArrayList<>();
        UnemploymentNode<T> currentVertex = target;

        while (currentVertex != null) {
            shortestPath.add(0, currentVertex);
            currentVertex = shortestPathMap.get(currentVertex);
        }

        return shortestPath;
    }
}
	
	


