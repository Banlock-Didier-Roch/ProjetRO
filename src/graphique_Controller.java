import Domain.Contraintes;
import Domain.FO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class graphique_Controller {
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
    @FXML
    private AnchorPane ancorePaneContenu;

    private int test = 0;
    private Stage stage;
    private Parent root;
    private ArrayList<Contraintes> contraintes = new ArrayList<>();
    private ArrayList<Contraintes> contraintesIntersections = new ArrayList<>();
    private FO fonctionObjective;
    private Contraintes resultat = new Contraintes();
    private ArrayList<Boolean> respecteContraintes = new ArrayList<Boolean>();
    LineChart<Number, Number> chart;

    public graphique_Controller() {
    }


    @FXML
    private void initialize() {

        //Initialisation du combobox
        String valeurs[] = {"Maximisation", "Minimisation"};
        min_max.getItems().addAll(valeurs);
        min_max.getSelectionModel().selectFirst();

        // Create action event
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        if (min_max.getValue().toString().equalsIgnoreCase("Maximisation")) {
                            labelMaxMin.setText("max(f) =");
                            refreshContraints();
                        } else if (min_max.getValue().toString().equalsIgnoreCase("Minimisation")) {
                            labelMaxMin.setText("min(f) =");
                            refreshContraints();
                        }
                    }
                };

        // Set on action
        min_max.setOnAction(event);

    }

    @FXML
    public void methode_simplexe() {
        try {
            stage = (Stage) border.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("views/simplexe.fxml"));
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

    public void addConstraint(Contraintes contrainte) {
        contraintes.add(contrainte);
        refreshContraints();
    }

    public void addFo(FO fo) {
        fonctionObjective = new FO();
        fonctionObjective.setCoefs(fo.getCoefs());
        refreshFo();
    }

    public void refreshFo() {
        label_fo.setText("");
        int variableCounter = 1;
        String fo = "";
        for (double coef : fonctionObjective.getCoefs()) {
            if (!fo.equalsIgnoreCase("")) {
                fo = fo + label_fo.getText() + " + " + coef + "X" + variableCounter;
            } else {
                fo = "" + coef + "X" + variableCounter;
            }
            variableCounter++;
        }
        label_fo.setText(fo);
    }

    public void refreshContraints() {
        vbox_contraintes.getChildren().clear();
        int variableCounter;
        Label l;

        for (Contraintes contrainte1 : contraintes) {
            variableCounter = 1;
            l = new Label();
            l.setFont(new Font(14));

            for (double coef : contrainte1.getCoefs()) {

                if (!l.getText().equalsIgnoreCase("")) {
                    l.setText(l.getText() + " + " + coef + "x" + variableCounter);
                } else {
                    l.setText("" + coef + "x" + variableCounter);
                }
                variableCounter++;
            }
            //Ajout de la valeur : si c'est une maximistion
            if (min_max.getValue().toString().equalsIgnoreCase("Maximisation"))
                l.setText(l.getText() + " <= " + contrainte1.getValeur());

            //Ajout de la valeur : si c'est une minimisation
            if (min_max.getValue().toString().equalsIgnoreCase("Minimisation"))
                l.setText(l.getText() + " >= " + contrainte1.getValeur());
            vbox_contraintes.getChildren().add(l);
        }
    }

    @FXML
    public void ajoutContrainte() {

        stage = (Stage) border.getScene().getWindow();
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/ajoutContraintes.fxml"));
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
        AjoutContraintesController controller = loader.getController();
        controller.setParentController(this);

        //Initialisation du signe de l'inéquation
        if (min_max.getValue().toString().equalsIgnoreCase("Maximisation"))
            controller.setSigne("<=");
        if (min_max.getValue().toString().equalsIgnoreCase("Minimisation"))
            controller.setSigne(">=");

    }


    @FXML
    public void ajoutFo() {
        stage = (Stage) border.getScene().getWindow();
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/ajoutFo.fxml"));
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
        AjoutFoController controller = loader.getController();
        controller.setParentController(this);
    }


    @FXML
    public void retirerContrainte() {
        if (contraintes.size() > 0) {
            contraintes.remove(contraintes.size() - 1);
            refreshContraints();
        }
    }

    @FXML
    void optimiser(ActionEvent event) {

        if (!contraintes.isEmpty()&&!fonctionObjective.getCoefs().isEmpty()) {
            dessinerLignes();
            detecterPointsIntersection();
        }

    }

    public void detecterPointsIntersection(){

        Label l = new Label();
        l.setFont(new Font(14));
        l.setStyle("-fx-font-weight: bold");
        l.setText("RESOLUTION\n");

        Contraintes intersection;
        double diviseur, divise, val1, val2;

        for (Contraintes contrainte : contraintes) {

            for(Contraintes contrainte2 : contraintes){

                if(contraintes.indexOf(contrainte) != contraintes.indexOf(contrainte2)){
                    intersection = new Contraintes();
                    //Identification de xIntersection et yIntersection
                    divise = ((contrainte.getValeur()*contrainte2.getCoefs().get(1))-(contrainte2.getValeur()*contrainte.getCoefs().get(1)));
                    diviseur = ((contrainte.getCoefs().get(0)*contrainte2.getCoefs().get(1))-(contrainte2.getCoefs().get(0)*contrainte.getCoefs().get(1)));
                    intersection.getCoefs().add(divise/diviseur);

                    divise = ((contrainte.getValeur()*contrainte2.getCoefs().get(0))-(contrainte2.getValeur()*contrainte.getCoefs().get(0)));
                    diviseur = (contrainte.getCoefs().get(1)*contrainte2.getCoefs().get(0))-(contrainte2.getCoefs().get(1)*contrainte.getCoefs().get(0));
                    intersection.getCoefs().add(divise/diviseur);
                    contraintesIntersections.add(intersection);
                }
            }

        }

        //Detection du point optimal
        for(Contraintes contrainte : contraintesIntersections){

            val1 = ((fonctionObjective.getCoefs().get(0)*(contrainte.getCoefs().get(0)))+(fonctionObjective.getCoefs().get(1)*(contrainte.getCoefs().get(1))));
            if (min_max.getValue().toString().equalsIgnoreCase("Maximisation")) {
                if((val1 > ((resultat.getCoefs().get(0)*fonctionObjective.getCoefs().get(0))+(resultat.getCoefs().get(1)*fonctionObjective.getCoefs().get(1))))&&(verifieContraintes(contrainte.getCoefs().get(0),contrainte.getCoefs().get(1)))){
                    resultat.getCoefs().clear();
                    resultat.getCoefs().add(contrainte.getCoefs().get(0));
                    resultat.getCoefs().add(contrainte.getCoefs().get(1));
                    resultat.setValeur(val1);
                }
            }else{
                if((val1 < ((resultat.getCoefs().get(0)*fonctionObjective.getCoefs().get(0))+(resultat.getCoefs().get(1)*fonctionObjective.getCoefs().get(1))))&&(verifieContraintes(contrainte.getCoefs().get(0),contrainte.getCoefs().get(1)))){
                    resultat.getCoefs().clear();
                    resultat.getCoefs().add(contrainte.getCoefs().get(0));
                    resultat.getCoefs().add(contrainte.getCoefs().get(1));
                    resultat.setValeur(val1);
                }
            }
        }
        l.setText(l.getText()+"\n Le point optimal est : "+resultat.getCoefs()+"\n La fonction économique devient donc : \nf="+fonctionObjective.getCoefs().get(0)+"("+resultat.getCoefs().get(0)+") + "+fonctionObjective.getCoefs().get(1)+"("+resultat.getCoefs().get(1)+") = "+resultat.getValeur());
        vbox_resolution.getChildren().add(l);

        XYChart.Series<Number, Number> ligne = new XYChart.Series<>();
        ligne.setName("Point optimal");
        ligne.getData().add(new XYChart.Data<>(resultat.getCoefs().get(0), resultat.getCoefs().get(1)));
        chart.getData().add(ligne);
        ancorePaneContenu.getChildren().clear();
        ancorePaneContenu.getChildren().add(chart);

        //Réinitialisation
        contraintes.clear();
        refreshContraints();
        fonctionObjective.getCoefs().clear();
        refreshFo();
        contraintesIntersections.clear();
        respecteContraintes.clear();
        resultat.getCoefs().clear();



    }

    public void dessinerLignes(){
        vbox_resolution.getChildren().clear();

        ancorePaneContenu.getChildren().clear();
        //Initialisation de l'abscisse
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Abscisse");
        xAxis.setTickUnit(1);
        xAxis.setMinorTickCount(0);

        //Initialisation de l'ordonnée
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ordonnée");
        yAxis.setMinorTickCount(0);

        //Initialisation du graphe en insertion dans l'AncorPane
        chart = new LineChart<Number, Number>(xAxis, yAxis);
        chart.setTitle("Résolution Graphique");
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefHeight(500);
        chart.setPrefWidth(650);
        chart.setData(getHEIAdata());
        ancorePaneContenu.getChildren().add(chart);
    }

    private ObservableList<XYChart.Series<Number, Number>> getHEIAdata() {
        double val1, val2;

        //Initialisation du chartData
        ObservableList<XYChart.Series<Number, Number>> chartData = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
        int counterLine = 1;
        XYChart.Series<Number, Number> ligne = new XYChart.Series<>();

            resultat.getCoefs().add(0.0);
            resultat.getCoefs().add(0.0);

        for (Contraintes contrainte : contraintes) {
            //Initialisation de la ligne
            ligne = new XYChart.Series<>();
            ligne.setName("Ligne " + counterLine);

            if(contrainte.getCoefs().get(0)!=0 && contrainte.getCoefs().get(1)!=0){
                ligne.getData().addAll(new XYChart.Data<>(contrainte.getValeur()/contrainte.getCoefs().get(0), 0),
                        new XYChart.Data<>(0, contrainte.getValeur()/contrainte.getCoefs().get(1)));


                //Déterminer le point max/min des intersections aves l'abscisse et l'ordonnée

                val1 = fonctionObjective.getCoefs().get(0)*(contrainte.getValeur()/contrainte.getCoefs().get(0));
                val2 = fonctionObjective.getCoefs().get(1)*(contrainte.getValeur()/contrainte.getCoefs().get(1));
                if (min_max.getValue().toString().equalsIgnoreCase("Maximisation")) {
                    if((val1 > ((resultat.getCoefs().get(0)*fonctionObjective.getCoefs().get(0))+(resultat.getCoefs().get(1)*fonctionObjective.getCoefs().get(1))))&&(verifieContraintes(contrainte.getValeur()/contrainte.getCoefs().get(0),0.0))){
                        resultat.getCoefs().clear();
                        resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(0));
                        resultat.getCoefs().add(0.0);
                        resultat.setValeur(val1);
                    }else if((val2 >((resultat.getCoefs().get(0)*fonctionObjective.getCoefs().get(0))+(resultat.getCoefs().get(1)*fonctionObjective.getCoefs().get(1))))&&(verifieContraintes(0,contrainte.getValeur()/contrainte.getCoefs().get(1)))){
                        resultat.getCoefs().clear();
                        resultat.getCoefs().add(0.0);
                        resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(1));
                        resultat.setValeur(val2);
                    }
                }else {
                    if (resultat.getCoefs().get(0) == 0 && resultat.getCoefs().get(1) == 0 && (verifieContraintes(contrainte.getValeur() / contrainte.getCoefs().get(0), 0.0))) {
                        resultat.getCoefs().clear();
                        resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(0));
                        resultat.getCoefs().add(0.0);
                        resultat.setValeur(val1);
                    } else if (resultat.getCoefs().get(0) == 0 && resultat.getCoefs().get(1) == 0 && (verifieContraintes(0.0, contrainte.getValeur() / contrainte.getCoefs().get(0)))) {
                        resultat.getCoefs().clear();
                        resultat.getCoefs().add(0.0);
                        resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(1));
                        resultat.setValeur(val2);
                    } else {

                        if ((val1 < ((resultat.getCoefs().get(0) * fonctionObjective.getCoefs().get(0)) + (resultat.getCoefs().get(1) * fonctionObjective.getCoefs().get(1)))) && (verifieContraintes(contrainte.getValeur() / contrainte.getCoefs().get(0), 0.0))) {
                            resultat.getCoefs().clear();
                            resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(0));
                            resultat.getCoefs().add(0.0);
                            resultat.setValeur(val1);
                        } else if ((val2 < ((resultat.getCoefs().get(0) * fonctionObjective.getCoefs().get(0)) + (resultat.getCoefs().get(1) * fonctionObjective.getCoefs().get(1)))) && (verifieContraintes(0, contrainte.getValeur() / contrainte.getCoefs().get(1)))) {
                            resultat.getCoefs().clear();
                            resultat.getCoefs().add(0.0);
                            resultat.getCoefs().add(contrainte.getValeur() / contrainte.getCoefs().get(1));
                            resultat.setValeur(val2);
                        }
                    }
                }
            }

            chartData.add(ligne);
            counterLine++;
        }

        return chartData;

    }

    private boolean verifieContraintes(double val1, double val2){
        boolean respecte = true;
        respecteContraintes.clear();

        for(Contraintes contrainte : contraintes){

            if (min_max.getValue().toString().equalsIgnoreCase("Maximisation")) {
                if((((contrainte.getCoefs().get(0)*val1)+(contrainte.getCoefs().get(1)*val2))<=contrainte.getValeur())){
                    respecteContraintes.add(true);
                }else{
                    respecteContraintes.add(false);
                }
            }else{
                if((((contrainte.getCoefs().get(0)*val1)+(contrainte.getCoefs().get(1)*val2))>=contrainte.getValeur())){
                    respecteContraintes.add(true);
                }else{
                    respecteContraintes.add(false);
                }
            }

        }

        for(boolean bool : respecteContraintes){
            respecte = respecte && bool;
        }
        return respecte;
    }

}
