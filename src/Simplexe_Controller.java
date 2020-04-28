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
    private VBox vbox_contraintes;
    @FXML
    private VBox vbox_resolution;
    @FXML
    private Label label_fo;
    @FXML
    private Label labelMaxMin;
    @FXML
    private ComboBox min_max;
    @FXML
    private Button optimiser;


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
        min_max.getSelectionModel().selectFirst();

        // Create action event
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        if(min_max.getValue().toString().equalsIgnoreCase("Maximisation")){
                            labelMaxMin.setText("max(f) =");
                            refreshContraints();
                        }
                        else if(min_max.getValue().toString().equalsIgnoreCase("Minimisation")){
                            labelMaxMin.setText("min(f) =");
                            refreshContraints();
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

        //Initialisation du signe de l'inéquation
        if(min_max.getValue().toString().equalsIgnoreCase("Maximisation"))
            controller.setSigne("<=");
        if(min_max.getValue().toString().equalsIgnoreCase("Minimisation"))
            controller.setSigne(">=");

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

    public  void addConstraint(Contraintes contrainte){
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
            //Ajout de la valeur : si c'est une maximistion
            if(min_max.getValue().toString().equalsIgnoreCase("Maximisation"))
             l.setText(l.getText() + " <= "+contrainte1.getValeur());

            //Ajout de la valeur : si c'est une minimisation
            if(min_max.getValue().toString().equalsIgnoreCase("Minimisation"))
                l.setText(l.getText() + " >= "+contrainte1.getValeur());
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

    @FXML
    public void optimiser(){

        if(fonctionObjective!=null && !contraintes.isEmpty()){
            reinitialisation_vboxs();

            int variableCounterCoef=1;
            Label l = new Label();
            l.setFont(new Font(14));
            Label l2 = new Label();
            l.setStyle("-fx-font-weight: bold");
            l.setText("Etape 1 : Introduction des variables d'écart");
            vbox_resolution.getChildren().add(l);
            historique.getChildren().add(new Label("Etape 1 : Introduction des variables d'écart"));

            //Ecriture des équations avec les variables d'écart
            int variableCounterContrainte = 1;
            for(Contraintes contrainte1 : contraintes){
                variableCounterCoef = 1;
                l2 = new Label();
                l2.setFont(new Font(14));

                for(int coef : contrainte1.getCoefs()){

                    if(!l2.getText().equalsIgnoreCase("")) {
                        l2.setText(l2.getText() + " + "+coef+"x"+variableCounterCoef);
                    }
                    else{
                        l2.setText(""+coef+"x"+variableCounterCoef);
                    }
                    variableCounterCoef++;
                }
                //Ajout des variables d'écart
                for(int i=0; i<=contraintes.size()-1;i++){
                    if(i+1 == variableCounterContrainte) {
                        l2.setText(l2.getText() + " + 1"+"x"+variableCounterCoef);
                        variableCounterCoef++;
                    }else{
                        l2.setText(l2.getText() + " + 0" + "x" + variableCounterCoef);
                        variableCounterCoef++;
                    }
                }
                //Ajout de la valeur
                l2.setText(l2.getText() + " = "+ contrainte1.getValeur() );

                //Ajout dans le vbox du contenu
                vbox_resolution.getChildren().add(l2);
                variableCounterContrainte++;
            }

    //Ecriture du programme de base numéro 1
            //Ecriture des titres
            l = new Label();
            l.setFont(new Font(14));
            l.setStyle("-fx-font-weight: bold");
            l.setText("\n\nEtape 2 : Ecriture du programme de base n°1");
            vbox_resolution.getChildren().add(l);
            historique.getChildren().add(new Label("\nEtape 2 : Ecriture du programme de base n°1"));

            //Ecriture de la ligne des variables
            l2 = new Label();
            l2.setFont(new Font(14));
            l2.setText("x1");
            for(int i=1; i<variableCounterCoef-1;i++){
                l2.setText(l2.getText()+"     x"+(i+1));
            }
            vbox_resolution.getChildren().add(l2);

            //Remlissage du tableau
            variableCounterContrainte = 1;
            l2 = new Label();
            for(Contraintes contrainte1 : contraintes){
                variableCounterCoef = 1;
                l2 = new Label();
                l2.setFont(new Font(14));

                for(int coef : contrainte1.getCoefs()){

                    if(!l2.getText().equalsIgnoreCase("")) {
                        l2.setText(l2.getText()+"      "+coef);
                    }
                    else{
                        l2.setText(" "+coef);
                    }
                    variableCounterCoef++;
                }
                //Ajout des variables d'écart
                for(int i=0; i<=contraintes.size()-1;i++){
                    if((i+1) == variableCounterContrainte) {
                        l2.setText(l2.getText()+"      1");
                        variableCounterCoef++;
                    }else{
                        l2.setText(l2.getText()+"      0");
                        variableCounterCoef++;
                    }
                }
                //Ajout de la valeur
                l2.setText(l2.getText() + "     "+ contrainte1.getValeur() );

                //Ajout dans le vbox du contenu
                vbox_resolution.getChildren().add(l2);
                variableCounterContrainte++;
            }
        }
    }

    public void reinitialisation_vboxs(){
        //Vidage du vbox de resolution
        vbox_resolution.getChildren().clear();
        Label l0 = new Label();
        l0.setFont(new Font(16));
        l0.setStyle("-fx-font-weight: bold");
        l0.setText("ETAPES DE RESOLUTION");
        vbox_resolution.getChildren().add(l0);

        //Vidage du vbox historique
        historique.getChildren().clear();
        l0 = new Label();
        l0.setFont(new Font(16));
        l0.setStyle("-fx-font-weight: bold");
        l0.setText("METHODE DE SIMPLEXE");
        historique.getChildren().add(l0);

        l0 = new Label();
        l0.setFont(new Font(16));
        l0.setStyle("-fx-font-weight: bold");
        l0.setText("Historique");
        historique.getChildren().add(l0);
    }

    public ComboBox getMin_max() {
        return min_max;
    }

    public void setMin_max(ComboBox min_max) {
        this.min_max = min_max;
    }
}
