package csc03a.userinterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import csc03a.graph.UnemployementGraph;
import csc03a.graph.Graph.Edge;
import csc03a.graph.Graph.Vertex;
import csc03a.nodes.UnemploymentNode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UserInterface extends VBox {
	// Graph variable
	private UnemployementGraph<String> graph;

	private Collection<Vertex<String>> vertices;
	private Collection<Edge<String>> edges;
	private ArrayList<UnemploymentNode<String>> nodeCollection;
	private ArrayList<Edge<String>> edgeCollection;
	
    private ComboBox<UnemploymentNode<String>> startNodeComboBox;
    private ComboBox<UnemploymentNode<String>> endNodeComboBox;

	private GridPane gridP = null;
	private ScrollPane scrollPane;
	private Canvas canvas = new Canvas();
	private int count = 1;

	private Pane edgesPane = new Pane();
	private Pane graphPane = new Pane();

	public UserInterface() {
		graph = new UnemployementGraph<String>();
		nodeCollection = new ArrayList<>();
		edgeCollection = new ArrayList<>();
		
        Button saveButton = new Button("Save Collections");
        saveButton.setOnAction(event -> {

            	FileLoader fileLoader = new FileLoader();
            	fileLoader.storeNodeCollection(nodeCollection);
            	fileLoader.storeEdgeCollection(edgeCollection);

        });
        this.getChildren().add(saveButton);

		this.getChildren().add(nodesLayOut());

		this.getChildren().add(edgesPane);

		// Display each UnemploymentNode in the array list
		refreshGridPane();
	}
	
    public UserInterface(ArrayList<UnemploymentNode<String>> loadedNodes, ArrayList<Edge<String>> loadedEdges) {
        graph = new UnemployementGraph<String>();
        nodeCollection = loadedNodes;
        edgeCollection = loadedEdges;

        this.getChildren().add(nodesLayOut());

        this.getChildren().add(edgesPane);
        
        refreshGridPane();

        // Display each UnemploymentNode in the array list

        drawGraphEdges();
        
        this.getChildren().add(graphPane);
    }

	// Refreshes the grid pane to display the nodes
	private void refreshGridPane() {
		gridP.getChildren().clear();

		int rowIndex = 0;
		for (UnemploymentNode<String> node : nodeCollection) {
			Label nameLabel = new Label("Name:");
			TextField nameTextField = new TextField(node.getValue());
			Label weightLabel = new Label("Weight:");
			TextField weightTextField = new TextField(String.valueOf(node.getWeight()));

			// Add node information to the GridPane
			gridP.add(nameLabel, 0, rowIndex);
			gridP.add(nameTextField, 1, rowIndex);
			gridP.add(weightLabel, 2, rowIndex);
			gridP.add(weightTextField, 3, rowIndex);

			// Add a button for adding child nodes
			Button addChildButton = new Button("Add Child Node");
			addChildButton.setOnAction(new AddChildNodeEventHandler(node));
			gridP.add(addChildButton, 4, rowIndex);

			rowIndex++;

			// Display child nodes if any
			for (UnemploymentNode<String> child : node.getChildNodes()) {
				Label childNameLabel = new Label("Child Name:");
				TextField childNameTextField = new TextField(child.getValue());
				Label childWeightLabel = new Label("Child Weight:");
				TextField childWeightTextField = new TextField(String.valueOf(child.getWeight()));

				// Add child node information to the GridPane
				gridP.add(childNameLabel, 1, rowIndex);
				gridP.add(childNameTextField, 2, rowIndex);
				gridP.add(childWeightLabel, 3, rowIndex);
				gridP.add(childWeightTextField, 4, rowIndex);

				// Add a button for adding child nodes
				Button addChildNodeButton = new Button("Add Child Node");
				addChildNodeButton.setOnAction(new AddChildNodeEventHandler(child));
				gridP.add(addChildNodeButton, 5, rowIndex);

				rowIndex++;
			}
		}

		// Add a button for adding a new main node
		Button addMainButton = new Button("Add Main Node");
		addMainButton.setOnAction(new AddMainNodeEventHandler());
		gridP.add(addMainButton, 0, rowIndex);

		Button displayNodesButton = new Button("Submit Nodes");
		displayNodesButton.setOnAction(event -> {
			nodeScroll();
			// refreshGridPane();
		});
		gridP.add(displayNodesButton, 1, rowIndex++);
	}

	// Event handler for adding a new main node
	private class AddMainNodeEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Create a new stage for adding main node details
			Stage addMainStage = new Stage();
			addMainStage.setTitle("Add Main Node");

			// Create the layout for adding main node details
			VBox vbox = new VBox();
			vbox.setPadding(new Insets(10));
			vbox.setSpacing(5);

			Label nameLabel = new Label("Name:");
			TextField nameTextField = new TextField();
			Label weightLabel = new Label("Weight:");
			TextField weightTextField = new TextField();

			Button submitButton = new Button("Submit");
			submitButton.setOnAction(submitEvent -> {
				String mainName = nameTextField.getText();
				int mainWeight = Integer.parseInt(weightTextField.getText());

				// Create a new main node based on user input
				UnemploymentNode<String> mainNode = new UnemploymentNode<>(mainName, mainWeight);
				nodeCollection.add(mainNode);

				// Refresh the grid pane to display the nodes
				refreshGridPane();

				// Close the add main node stage
				addMainStage.close();
			});

			vbox.getChildren().addAll(nameLabel, nameTextField, weightLabel, weightTextField, submitButton);
			Scene scene = new Scene(vbox);
			addMainStage.setScene(scene);
			addMainStage.show();
		}
	}

	private void nodeScroll() {
		Stage scrollStage = new Stage();
		// Create the scroll pane
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefViewportHeight(300);
		scrollPane.setPrefViewportWidth(this.getWidth());

		// Create a VBox to hold the nodes
		VBox nodesVBox = new VBox();
		nodesVBox.setSpacing(10);
		nodesVBox.setPadding(new Insets(10));

		// Add nodes and their child nodes to the VBox
		for (UnemploymentNode<String> node : nodeCollection) {
			Button nodeButton = new Button(node.getValue());

			// Add an event handler to the node button
			nodeButton.setOnAction(event -> {
				// Create the AddEdgeInterface to add an edge with the selected node
				AddEdgeInterface addEdgeInterface = new AddEdgeInterface(node, nodeCollection, edgeCollection);
				addEdgeInterface.show();
			});

			nodesVBox.getChildren().add(nodeButton);

			// Add child nodes to the VBox
			for (UnemploymentNode<String> child : node.getChildNodes()) {
				Button childButton = new Button(child.getValue());

				// Add an event handler to the child button
				childButton.setOnAction(event -> {
					// Create the AddEdgeInterface to add an edge with the selected child node
					AddEdgeInterface addEdgeInterface = new AddEdgeInterface(child, nodeCollection, edgeCollection);
					addEdgeInterface.show();
				});

				nodesVBox.getChildren().add(childButton);
			}
		}

		// Set the VBox as the content of the scroll pane
		scrollPane.setContent(nodesVBox);

		// Create the main layout
		VBox mainLayout = new VBox();
		mainLayout.getChildren().add(scrollPane);
		Button completeAdd = new Button("Done");
		completeAdd.setOnAction(event -> {
			scrollStage.close();
			drawGraphEdges();

			ScrollPane graphScroll = new ScrollPane();
			graphScroll.setContent(graphPane);
			this.getChildren().add(graphScroll);
		});

		// Create the scene and set it to the stage
		mainLayout.getChildren().add(completeAdd);

		// Wrap the main layout in a ScrollPane
		ScrollPane mainScrollPane = new ScrollPane();
		mainScrollPane.setContent(mainLayout);
		this.getChildren().add(mainScrollPane);
	}

	// Event handler for the add child node button
	private class AddChildNodeEventHandler implements EventHandler<ActionEvent> {
		private UnemploymentNode<String> parentNode;

		public AddChildNodeEventHandler(UnemploymentNode<String> parentNode) {
			this.parentNode = parentNode;
		}

		@Override
		public void handle(ActionEvent event) {
			// Create the stage for adding child node details
			Stage addChildStage = new Stage();
			addChildStage.setTitle("Add Child Node");

			// Create the layout for adding child node details
			GridPane childGridPane = new GridPane();
			childGridPane.setPadding(new Insets(10));
			childGridPane.setHgap(10);
			childGridPane.setVgap(5);

			Label nameLabel = new Label("Name:");
			TextField nameTextField = new TextField();
			Label weightLabel = new Label("Weight:");
			TextField weightTextField = new TextField();

			Button submitButton = new Button("Submit");
			submitButton.setOnAction(submitEvent -> {
				String childName = nameTextField.getText();
				int childWeight = Integer.parseInt(weightTextField.getText());

				// Create a new child node based on user input
				UnemploymentNode<String> childNode = new UnemploymentNode<>(childName, childWeight);

				// Add the child node to the parent node
				parentNode.addChildNode(childNode);

				// Close the add child stage
				addChildStage.close();

				// Refresh the GridPane to update the UI
				refreshGridPane();
			});

			childGridPane.add(nameLabel, 0, 0);
			childGridPane.add(nameTextField, 1, 0);
			childGridPane.add(weightLabel, 0, 1);
			childGridPane.add(weightTextField, 1, 1);
			childGridPane.add(submitButton, 1, 2);

			Scene scene = new Scene(childGridPane);
			addChildStage.setScene(scene);
			addChildStage.show();
		}
	}
	
	private void drawGraphEdges() {
	    if (canvas == null) {
	        canvas = new Canvas();
	        graphPane.getChildren().add(canvas);
	    }
	    graphPane.getChildren().add(canvas);
	    
        // Create the combo boxes for selecting start and end nodes
        startNodeComboBox = new ComboBox<>();
        startNodeComboBox.setItems(FXCollections.observableArrayList(nodeCollection));
        startNodeComboBox.setPromptText("Select start node");
        
        endNodeComboBox = new ComboBox<>();
        endNodeComboBox.setItems(FXCollections.observableArrayList(nodeCollection));
        endNodeComboBox.setPromptText("Select end node");
        
        // Create the button for calculating the shortest path
        Button calculateButton = new Button("Calculate Shortest Path");
        calculateButton.setOnAction(event -> calculateShortestPath());
        
        // Add the combo boxes and button to the UI
        this.getChildren().addAll(startNodeComboBox, endNodeComboBox, calculateButton);
	    double canvasWidth = 800;
	    double canvasHeight = 400;

	    canvas.setWidth(canvasWidth);
	    canvas.setHeight(canvasHeight);

	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    gc.clearRect(0, 0, canvasWidth, canvasHeight);

	    Set<UnemploymentNode<String>> drawnNodes = new HashSet<>();

	    for (UnemploymentNode<String> node : nodeCollection) {
	        double sourceX = canvasWidth / (nodeCollection.size() + 1) * (nodeCollection.indexOf(node) + 1);
	        double sourceY = canvasHeight / 2; // Specify the y-coordinate of the source node

	        // Draw edges from the source node to its children
	        for (Edge<String> edge : edgeCollection) {
	            if (edge.getFromVertex() == node) {
	                UnemploymentNode<String> targetNode = (UnemploymentNode<String>) edge.getToVertex();
	                double targetX = canvasWidth / (nodeCollection.size() + 1) * (nodeCollection.indexOf(targetNode) + 1);
	                double targetY = sourceY + 50; // Specify the y-coordinate of the target node

	                // Draw a line between the source and target coordinates (edge line)
	                gc.setStroke(Color.BLACK);
	                gc.setLineWidth(2);
	                gc.strokeLine(sourceX, sourceY, targetX, targetY);

	                // Draw the target node (circle) at the target coordinates
	                double nodeRadius = 10;
	                gc.setFill(Color.RED);
	                gc.fillOval(targetX - nodeRadius, targetY - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

	                // Draw the node label (name) if not already drawn
	                if (!drawnNodes.contains(targetNode)) {
	                    gc.setFill(Color.BLACK);
	                    gc.fillText(targetNode.getValue(), targetX - nodeRadius, targetY - nodeRadius - 5);
	                    drawnNodes.add(targetNode);
	                }

	                // Draw edges from the target node to its children (recursive call)
	                drawChildEdges(targetNode, targetX, targetY, gc, drawnNodes);
	            }
	        }

	        // Draw the source node (circle) at the source coordinates
	        double nodeRadius = 10;
	        gc.setFill(Color.RED);
	        gc.fillOval(sourceX - nodeRadius, sourceY - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

	        // Draw the node label (name) if not already drawn
	        if (!drawnNodes.contains(node)) {
	            gc.setFill(Color.BLACK);
	            gc.fillText(node.getValue(), sourceX - nodeRadius, sourceY - nodeRadius - 5);
	            drawnNodes.add(node);
	        }
	    }
	}
	
    private void calculateShortestPath() {
        // Get the selected start and end nodes
        UnemploymentNode<String> startNode = startNodeComboBox.getSelectionModel().getSelectedItem();
        UnemploymentNode<String> endNode = endNodeComboBox.getSelectionModel().getSelectedItem();
        
        if (startNode != null && endNode != null) {
            // Calculate the shortest path using the graph
            List<UnemploymentNode<String>> shortestPath = graph.findShortestPath(startNode, endNode);
            
            // Display the shortest path or show an error message if no path found
            if (shortestPath != null) {
                System.out.println("Shortest Path:");
                for (UnemploymentNode<String> node : shortestPath) {
                    System.out.println(node.getValue());
                }
            } else {
                System.out.println("No path found between the selected nodes.");
            }
        } else {
            System.out.println("Please select start and end nodes.");
        }
    }
	

	private void drawChildEdges(UnemploymentNode<String> parentNode, double parentX, double parentY, GraphicsContext gc, Set<UnemploymentNode<String>> drawnNodes) {
	    double childY = parentY + 50; // Specify the y-coordinate of the child node

	    for (UnemploymentNode<String> childNode : parentNode.getChildNodes()) {
	        double childX = parentX - 20; // Specify the x-coordinate of the child node

	        // Draw a line between the parent and child coordinates (edge line)
	        gc.setStroke(Color.BLACK);
	        gc.setLineWidth(2);
	        gc.strokeLine(parentX, parentY, childX, childY);

	        // Draw the child node (circle) at the child coordinates
	        double nodeRadius = 10;
	        gc.setFill(Color.RED);
	        gc.fillOval(childX - nodeRadius, childY - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

	        // Draw the node label (name) if not already drawn
	        if (!drawnNodes.contains(childNode)) {
	            gc.setFill(Color.BLACK);
	            gc.fillText(childNode.getValue(), childX - nodeRadius, childY - nodeRadius - 5);
	            drawnNodes.add(childNode);
	        }

	        // Draw edges from the child node to its children (recursive call)
	        drawChildEdges(childNode, childX, childY, gc, drawnNodes);

	        childY += 50; // Increment the y-coordinate for the next child node
	    }
	}
	
	
	public void addNodeCollection(ArrayList<UnemploymentNode<String>> nodeCollection) {
	    for (UnemploymentNode<String> node : nodeCollection) {
	        vertices.add(node);
	    }
	}

	public void addEdgeCollection(ArrayList<Edge<String>> edgeCollection) {
	    for (Edge<String> edge : edgeCollection) {
	        edges.add(edge);
	    }
	}

	private VBox nodesLayOut() {
		// Create the main layout
		VBox vbox = new VBox();
		scrollPane = new ScrollPane();
		gridP = new GridPane();
		gridP.setPadding(new Insets(10));
		gridP.setHgap(10);
		gridP.setVgap(5);
		scrollPane.setContent(gridP);
		vbox.getChildren().add(scrollPane);

		return vbox;
	}

}
