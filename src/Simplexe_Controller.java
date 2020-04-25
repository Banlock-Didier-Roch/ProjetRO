import Domain.Contraintes;
import Domain.FO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Simplexe_Controller {
    @FXML
    private VBox historique;
    @FXML
    private Button ajouter;
    @FXML
    private BorderPane border;
    @FXML
    public VBox vbox_contraintes;
    @FXML
    public Label label_fo;
    @FXML
    public Label labelMaxMin;
    @FXML
    public ComboBox min_max;


    private int test = 0;
    private Stage stage;
    private Parent root;
    private ArrayList<Contraintes> contraintes = new ArrayList<>();
    private FO fonctionObjective;

    @FXML
    private void initialize() {

        //Initialisation du combobox
        String valeurs[] = { "Maximisation", "Minimisation" };
        min_max.getItems().addAll(valeurs);

        // Create action event
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        if(min_max.getValue().toString().equalsIgnoreCase("Maximisation")){
                            labelMaxMin.setText("max(f) =");
                        }
                        else if(min_max.getValue().toString().equalsIgnoreCase("Minimisation")){
                            labelMaxMin.setText("min(f) =");
                        }
                    }
                };

        // Set on action
        min_max.setOnAction(event);

    }

    @FXML
    public void methode_graphique() {
        try {
            stage = (Stage) border.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("views/graphique.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Ajouter() {

        Label l = new Label();
        l.setText("Test : " + test);
        historique.getChildren().add(l);

    }

    @FXML
    public void ajoutContrainte() {

        stage = (Stage) border.getScene().getWindow();
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/ajout_contraintes.fxml"));
        AnchorPane page = new AnchorPane();
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ajout de contrainte");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();

        //Liaison avec le controller de l"ajout de contraintes
        Ajout_contrainte_controller controller = loader.getController();
        controller.setParentController(this);

    }

    @FXML
    public void ajoutFo(){
        stage = (Stage) border.getScene().getWindow();
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/ajout_FO.fxml"));
        AnchorPane page = new AnchorPane();
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ajout de la fonction objective");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();

        //Liaison avec le controller de l"ajout de contraintes
        Ajout_FO_controller controller = loader.getController();
        controller.setParentController(this);
    }

    public  void addConstraint(Contraintes contrainte, String inequation){
        contraintes.add(contrainte);
        refreshContraints();
    }

    public  void addFo(FO fo){
        fonctionObjective = new FO();
        fonctionObjective.setCoefs(fo.getCoefs());
        refreshFo();
    }

    public void refreshContraints(){
        vbox_contraintes.getChildren().clear();
        int variableCounter;
        Label l;

        for(Contraintes contrainte1 : contraintes){
            variableCounter = 1;
            l = new Label();
            l.setFont(new Font(14));

            for(int coef : contrainte1.getCoefs()){

                if(!l.getText().equalsIgnoreCase("")) {
                    l.setText(l.getText() + " + "+coef+"x"+variableCounter);
                }
                else{
                    l.setText(""+coef+"x"+variableCounter);
                }
                variableCounter++;
            }
            vbox_contraintes.getChildren().add(l);
        }
    }

    public void refreshFo(){
        label_fo.setText("");
        int variableCounter=1;
        String fo = "";
        for(int coef:fonctionObjective.getCoefs()){
            if(!fo.equalsIgnoreCase("")) {
                fo = fo + label_fo.getText() + " + "+coef+"X"+variableCounter;
            }
            else{
                fo = ""+coef+"X"+variableCounter;
            }
            variableCounter++;
        }
        label_fo.setText(fo);
    }

    @FXML
    public void retirerContrainte(){
        if(contraintes.size()>0) {
            contraintes.remove(contraintes.size() - 1);
            refreshContraints();
        }
    }
}
