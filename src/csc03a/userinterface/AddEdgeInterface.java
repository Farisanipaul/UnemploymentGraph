package csc03a.userinterface;
import java.util.ArrayList;

import csc03a.edges.UnemployementEdge;
import csc03a.graph.Graph.Edge;
import csc03a.nodes.UnemploymentNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddEdgeInterface extends Stage {

    private UnemploymentNode<String> selectedNode;

    private ArrayList<UnemploymentNode<String>> nodesList;
    private ArrayList<Edge<String>> edgesList;

    public AddEdgeInterface(UnemploymentNode<String> selectedNode, ArrayList<UnemploymentNode<String>> nodesList, ArrayList<Edge<String>> edgesList) {
        this.selectedNode = selectedNode;
        this.nodesList = nodesList;
        this.edgesList = edgesList;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Add Edge");

        // Create UI components
        Label targetNodeLabel = new Label("Select Target Node:");
        ChoiceBox<UnemploymentNode<String>> targetNodeChoiceBox = new ChoiceBox<>();
        
        // Add the selected node's children to the choice box
        targetNodeChoiceBox.getItems().addAll(selectedNode.getChildNodes());
        
        // Add the remaining nodes in the graph to the choice box
        for (UnemploymentNode<String> node : nodesList) {
            if (!node.equals(selectedNode)) {
                targetNodeChoiceBox.getItems().add(node);
            }
        }

        Label edgeValueLabel = new Label("Enter Edge Value:");
        TextField edgeValueField = new TextField();

        Button submitButton = new Button("Submit");

        // Handle submit button click
        submitButton.setOnAction(event -> {
            UnemploymentNode<String> targetNode = targetNodeChoiceBox.getValue();
            if (targetNode != null) {
                int edgeValue = Integer.parseInt(edgeValueField.getText());
                // Create a new edge between the selected node and the target node
                UnemployementEdge<String> edge = new UnemployementEdge<>(edgeValue, selectedNode, targetNode);

                // Add the edge to the list of edges in the graph
                edgesList.add(edge);

                // Close the interface window
                close();
            }
        });

        // Create the main layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(targetNodeLabel, targetNodeChoiceBox, edgeValueLabel, edgeValueField, submitButton);

        // Set the scene
        setScene(new Scene(vbox, 200, 200));
        setTitle("Adding Edge");
    }
}
