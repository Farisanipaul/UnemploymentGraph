import java.util.ArrayList;

import csc03a.graph.Graph.Edge;
import csc03a.nodes.UnemploymentNode;
import csc03a.userinterface.FileLoader;
import csc03a.userinterface.UserInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private UserInterface userInterface;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        Button loadFromFileButton = new Button("Load Nodes from Previous Files");

        loadFromFileButton.setOnAction(e -> {
            ArrayList<UnemploymentNode<String>> nodeCollection = FileLoader.loadNodeCollection();
            ArrayList<Edge<String>> edgeCollection = FileLoader.loadEdgeCollection(nodeCollection);
            userInterface = new UserInterface(nodeCollection, edgeCollection);

            Scene scene = new Scene(userInterface, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        });

        Button defaultButton = new Button("Manually Add Nodes");

        defaultButton.setOnAction(e -> {
            userInterface = new UserInterface();

            Scene scene = new Scene(userInterface, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        });

        root.getChildren().addAll(loadFromFileButton, defaultButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Choose the desired starting point");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
