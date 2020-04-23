import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Simplexe_Controller {
    @FXML
    private VBox historique;
    @FXML
    private Button ajouter;
    @FXML
    private BorderPane border;

    private int test=0;
    Stage stage;
    Parent root;

    @FXML
    private void initialize() {

    }

    @FXML
    public void methode_graphique(){
        try {
            stage = (Stage)border.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("views/graphique.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Ajouter(){

        Label l = new Label();
        l.setText("Test : "+test);
        historique.getChildren().add(l);

    }

}
