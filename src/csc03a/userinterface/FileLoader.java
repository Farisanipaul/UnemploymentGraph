package csc03a.userinterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc03a.graph.Graph.Edge;
import csc03a.nodes.UnemploymentNode;

public class FileLoader {
	
	public FileLoader() {
		
	}
	
	public static ArrayList<UnemploymentNode<String>> loadNodeCollection() {
	    ArrayList<UnemploymentNode<String>> nodeCollection = new ArrayList<>();
	    Map<String, List<UnemploymentNode<String>>> childNodeMap = new HashMap<>();
	    Path filePath = Paths.get("data/output/nodes.txt");

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            String name = parts[0];
	            int weight = Integer.parseInt(parts[1]);

	            UnemploymentNode<String> node = new UnemploymentNode<>(name, weight);
	            nodeCollection.add(node);

	            if (parts.length > 2) {
	                String[] childNames = Arrays.copyOfRange(parts, 2, parts.length);
	                List<UnemploymentNode<String>> childNodes = new ArrayList<>();
	                for (String childName : childNames) {
	                    UnemploymentNode<String> childNode = new UnemploymentNode<>(childName);
	                    childNodes.add(childNode);
	                }
	                childNodeMap.put(name, childNodes);
	            }
	        }

	        for (UnemploymentNode<String> node : nodeCollection) {
	            List<UnemploymentNode<String>> childNodes = childNodeMap.get(node.getValue());
	            if (childNodes != null) {
	                
	                for (UnemploymentNode<String> childNode : childNodes) {
	                	node.addChildNode(childNode);
	                }

	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return nodeCollection;
	}

	public static ArrayList<Edge<String>> loadEdgeCollection(ArrayList<UnemploymentNode<String>> nodeCollection) {
	    ArrayList<Edge<String>> edgeCollection = new ArrayList<>();
	    Map<String, UnemploymentNode<String>> nodeMap = new HashMap<>();
	    Path filePath = Paths.get("data/output/edges.txt");

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            String parentName = parts[0];
	            int parentWeight = Integer.parseInt(parts[1]);
	            String childName = parts[2];
	            int childWeight = Integer.parseInt(parts[3]);
	            int weight = Integer.parseInt(parts[4]);

	            UnemploymentNode<String> parentNode = getNodeByNameAndWeight(nodeCollection, parentName, parentWeight);
	            UnemploymentNode<String> childNode = getNodeByNameAndWeight(nodeCollection, childName, childWeight);

	            if (parentNode != null && childNode != null) {
	                Edge<String> edge = new Edge<>(weight,parentNode, childNode);
	                edgeCollection.add(edge);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return edgeCollection;
	}

	private static UnemploymentNode<String> getNodeByNameAndWeight(ArrayList<UnemploymentNode<String>> nodeCollection, String name, int weight) {
	    for (UnemploymentNode<String> node : nodeCollection) {
	        if (node.getValue().equals(name) && node.getWeight() == weight) {
	            return node;
	        }
	    }
	    return null;
	}




    public void storeNodeCollection(ArrayList<UnemploymentNode<String>> nodeCollection) {
        Path filePath = Paths.get("data/output/nodes.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (UnemploymentNode<String> node : nodeCollection) {
                storeNode(writer, node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeNode(BufferedWriter writer, UnemploymentNode<String> node) throws IOException {
        String line = node.getValue() + "," + node.getWeight();
        writer.write(line);

        List<UnemploymentNode<String>> childNodes = node.getChildNodes();
        if (childNodes != null && !childNodes.isEmpty()) {
            for (UnemploymentNode<String> child : childNodes) {
                writer.write("," + child.getValue());
            }
            writer.newLine();
            for (UnemploymentNode<String> child : childNodes) {
                storeNode(writer, child);
            }
        } else {
            writer.newLine();
        }
    }


    public void storeEdgeCollection(ArrayList<Edge<String>> edgeCollection) {
        Path filePath = Paths.get("data/output/edges.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (Edge<String> edge : edgeCollection) {
                UnemploymentNode<String> parentNode = (UnemploymentNode<String>) edge.getFromVertex();
                UnemploymentNode<String> childNode = (UnemploymentNode<String>) edge.getToVertex();
                int weight = edge.getCost();

                String parentName = parentNode.getValue();
                int parentWeight = parentNode.getWeight();
                String childName = childNode.getValue();
                int childWeight = childNode.getWeight();

                String line = parentName + "," + parentWeight + "," + childName + "," + childWeight + "," + weight;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
