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
import java.util.Collections;

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
    private long resultat = 0;
    private Stage stage;
    private Parent root;
    private ArrayList<Contraintes> contraintes = new ArrayList<>();
    private ArrayList<Contraintes> contraintes2 = new ArrayList<>();
    private Contraintes contrainteBAS;
    private Contraintes contrainteBAS2;
    private FO fonctionObjective;
    private Label l;
    private Label l2;
    private int variableCounterCoef;
    private int variableCounterContrainte;
    private int countProgramme=1;
    private boolean isOver = false;
    private boolean isFirstIteration = true;

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
        calcul_programme(contraintes);
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

            for(double coef : contrainte1.getCoefs()){

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
        for(double coef:fonctionObjective.getCoefs()){
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
    public void optimiser() throws InterruptedException {
        contraintes2 = new ArrayList<>(contraintes);
        isOver = false;
        isFirstIteration =true;
        contrainteBAS = new Contraintes();
        contrainteBAS.setValeur(0);
        countProgramme = 1;

        if(fonctionObjective!=null && !contraintes2.isEmpty()){
        //Initialisation de la ligne du bas
            for (double coef : fonctionObjective.getCoefs()){
                contrainteBAS.getCoefs().add(coef);
            }
          //Reinitialisation des VBoxs
            reinitialisation_vboxs();

            variableCounterCoef=1;
            l = new Label();
            l.setFont(new Font(14));
            l2 = new Label();
            l.setStyle("-fx-font-weight: bold");
            l.setText("Etape 1 : Introduction des variables d'écart");
            vbox_resolution.getChildren().add(l);
            historique.getChildren().add(new Label("Etape 1 : Introduction des variables d'écart"));

            //Ecriture des équations avec les variables d'écart
            variableCounterContrainte = 1;
            for(Contraintes contrainte1 : contraintes2){
                variableCounterCoef = 1;
                l2 = new Label();
                l2.setFont(new Font(14));
                contrainte1.getCoefs_ecart().clear();

                for(double coef : contrainte1.getCoefs()){

                    if(!l2.getText().equalsIgnoreCase("")) {
                        l2.setText(l2.getText() + " + "+coef+"x"+variableCounterCoef);
                    }
                    else{
                        l2.setText(""+coef+"x"+variableCounterCoef);
                    }
                    variableCounterCoef++;
                }
                //Ajout des variables d'écart
                for(int i=0; i<=contraintes2.size()-1;i++){
                    if(i+1 == variableCounterContrainte) {
                        l2.setText(l2.getText() + " + 1"+"x"+variableCounterCoef);
                        contrainte1.getCoefs_ecart().add(1.0);
                        contrainteBAS.getCoefs_ecart().add(0.0);
                        variableCounterCoef++;
                    }else{
                        l2.setText(l2.getText() + " + 0" + "x" + variableCounterCoef);
                        contrainte1.getCoefs_ecart().add(0.0);
                        variableCounterCoef++;
                    }
                }
                //Ajout de la valeur
                l2.setText(l2.getText() + " = "+ contrainte1.getValeur() );

                //Ajout dans le vbox du contenu
                vbox_resolution.getChildren().add(l2);
                variableCounterContrainte++;
            }
            while(!isOver){
                generer_programme(contraintes2);
                calcul_programme(contraintes2);
            }
            contraintes.clear();
            refreshContraints();
            fonctionObjective.getCoefs().clear();
            refreshFo();
        }
    }

    public void generer_programme(ArrayList<Contraintes> contraintesTab) throws InterruptedException {

        //Ecriture du programme de base numéro 1
        //Ecriture des titres
        l = new Label();
        l.setFont(new Font(14));
        l.setStyle("-fx-font-weight: bold");
        l.setText("\nEtape "+(countProgramme+1)+" : Ecriture du programme de base n°"+countProgramme);
        vbox_resolution.getChildren().add(l);
        Thread.sleep(100);
        historique.getChildren().add(new Label("\nEtape "+(countProgramme+1)+" : Ecriture du programme de base n°"+countProgramme));

        //Ecriture de la ligne des variables
        l2 = new Label();
        l2.setFont(new Font(14));
        l2.setText("  x1");
        for(int i=1; i<(contraintesTab.get(0).getCoefs().size()+contraintesTab.get(0).getCoefs_ecart().size());i++){
            l2.setText(l2.getText()+"       x"+(i+1));
        }
        vbox_resolution.getChildren().add(l2);

        //Remlissage du tableau
        variableCounterContrainte = 1;
        l2 = new Label();
        for(Contraintes contrainte1 : contraintesTab){
            variableCounterCoef = 1;
            l2 = new Label();
            l2.setFont(new Font(14));

            //Ajout des valeurs des variables simples
            for(double coef : contrainte1.getCoefs()){

                if(!l2.getText().equalsIgnoreCase("")) {
                    l2.setText(l2.getText()+"      "+coef);
                }
                else{
                    l2.setText(" "+coef);
                }
                variableCounterCoef++;
            }
            //Ajout des valeurs des variables d'écart
            for(double coef : contrainte1.getCoefs_ecart()){

                if(!l2.getText().equalsIgnoreCase("")) {
                    l2.setText(l2.getText()+"      "+coef);
                }
                else{
                    l2.setText(" "+coef);
                }
                variableCounterCoef++;
            }

            //Ajout de la valeur
            l2.setText(l2.getText() + "     "+ contrainte1.getValeur() );

            //Ajout dans le vbox du contenu
            vbox_resolution.getChildren().add(l2);
            variableCounterContrainte++;
        }

        //Remplissage ligne du bas
        l2 = new Label();
        l2.setFont(new Font(14));
        for (double coef : contrainteBAS.getCoefs()){
            if(!l2.getText().equalsIgnoreCase("")) {
                l2.setText(l2.getText()+"      "+coef);
            }
            else{
                l2.setText(" "+coef);
            }
        }
        for (double coef : contrainteBAS.getCoefs_ecart()){
            if(!l2.getText().equalsIgnoreCase("")) {
                l2.setText(l2.getText()+"      "+coef);
            }
            else{
                l2.setText(" "+coef);
            }
        }
        //Ajout de la valeur de la dernière ligne
        l2.setText(l2.getText() + "     "+ contrainteBAS.getValeur() );
        //Ajout dans le vbox de la dernière ligne
        vbox_resolution.getChildren().add(l2);
        variableCounterContrainte++;

        countProgramme++;

    }

    public void calcul_programme(ArrayList<Contraintes> contraintesTab){
        double maxMin_ligneBas;
        int indexColonnePivot;
        int indexLignePivot=1;
        if(min_max.getValue().toString().equalsIgnoreCase("Minimisation")) {
            maxMin_ligneBas=getVariableEnteringBase(contrainteBAS);
            if(maxMin_ligneBas<0 || isFirstIteration){
                //Si le pivot se trouve dans les varibles simples
                if(contrainteBAS.getCoefs().indexOf(maxMin_ligneBas)!=-1){
                    indexColonnePivot = contrainteBAS.getCoefs().indexOf(maxMin_ligneBas);

                    //Détermination de la ligne pivot
                    double maxValue = 0;
                    for(Contraintes contrainte : contraintesTab){
                        if(contrainte.getCoefs().get(indexColonnePivot)!=0) {
                            if (maxValue == 0) {
                                maxValue = contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            } else if ((contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot) > maxValue)) {
                                maxValue = contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            }
                        }
                    }

                    //Effectuer les opérations sur la matrice
                    variableCounterContrainte = 1;
                    variableCounterCoef = 1;
                    double multiplicateur=1;
                    double pivot = contraintesTab.get(indexLignePivot).getCoefs().get(indexColonnePivot);
                    //Traitement des lignes des contraintes
                    //Cas de la ligne Pivot
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs().set(i,contraintesTab.get(indexLignePivot).getCoefs().get(i)/pivot);
                    }
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs_ecart().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs_ecart().set(i,contraintesTab.get(indexLignePivot).getCoefs_ecart().get(i)/pivot);
                    }

                    contraintesTab.get(indexLignePivot).setValeur(contraintesTab.get(indexLignePivot).getValeur()/pivot);

                    for(Contraintes contrainte : contraintesTab){

                        //Cas des autres lignes
                        if(variableCounterContrainte != (indexLignePivot+1)){

                            variableCounterCoef = 1;
                            multiplicateur = contrainte.getCoefs().get(indexColonnePivot);
                            for(double coef : contrainte.getCoefs()){
                                contrainte.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            variableCounterCoef = 1;
                            for(double coef : contrainte.getCoefs_ecart()){
                                contrainte.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            contrainte.setValeur(contrainte.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                        }

                        variableCounterContrainte++;
                    }

                    //Traitement de la ligne de la fonction objective
                    variableCounterCoef = 1;
                    multiplicateur = contrainteBAS.getCoefs().get(indexColonnePivot);
                    for(double coef : contrainteBAS.getCoefs()){
                        contrainteBAS.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    variableCounterCoef = 1;
                    for(double coef : contrainteBAS.getCoefs_ecart()){
                        contrainteBAS.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    contrainteBAS.setValeur(contrainteBAS.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                }
                //Si le pivot se trouve dans les varibles d'écart
                else{
                    indexColonnePivot = contrainteBAS.getCoefs_ecart().indexOf(maxMin_ligneBas);

                    //Détermination de la ligne pivot
                    double maxValue = 0;
                    for(Contraintes contrainte : contraintesTab){
                        if(contrainte.getCoefs_ecart().get(indexColonnePivot)!=0) {
                            if (maxValue == 0) {
                                maxValue = contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            } else if ((contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot) > maxValue)) {
                                maxValue = contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            }
                        }
                    }

                    //Effectuer les opérations sur la matrice
                    variableCounterContrainte = 1;
                    variableCounterCoef = 1;
                    double multiplicateur=1;
                    double pivot = contraintesTab.get(indexLignePivot).getCoefs_ecart().get(indexColonnePivot);
                    System.out.println("pivot: "+pivot);

                    //Traitement des lignes des contraintes
                    //Cas de la ligne Pivot
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs().set(i,contraintesTab.get(indexLignePivot).getCoefs().get(i)/pivot);
                    }
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs_ecart().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs_ecart().set(i,contraintesTab.get(indexLignePivot).getCoefs_ecart().get(i)/pivot);
                    }

                    contraintesTab.get(indexLignePivot).setValeur(contraintesTab.get(indexLignePivot).getValeur()/pivot);

                    for(Contraintes contrainte : contraintesTab){

                        //Cas des autres lignes
                        if(variableCounterContrainte != (indexLignePivot+1)){

                            variableCounterCoef = 1;
                            multiplicateur = contrainte.getCoefs_ecart().get(indexColonnePivot);
                            for(double coef : contrainte.getCoefs()){
                                contrainte.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            variableCounterCoef = 1;
                            for(double coef : contrainte.getCoefs_ecart()){
                                contrainte.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            contrainte.setValeur(contrainte.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                        }

                        variableCounterContrainte++;
                    }

                    //Traitement de la ligne de la fonction objective
                    variableCounterCoef = 1;
                    multiplicateur = contrainteBAS.getCoefs_ecart().get(indexColonnePivot);
                    for(double coef : contrainteBAS.getCoefs()){
                        contrainteBAS.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    variableCounterCoef = 1;
                    for(double coef : contrainteBAS.getCoefs_ecart()){
                        contrainteBAS.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    contrainteBAS.setValeur(contrainteBAS.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                }
            }

            else{
                // Fin de la minimisation
                isOver = true;
            }
            isFirstIteration = false;
        }
        //Cas maximisation
        else{
            maxMin_ligneBas=getVariableEnteringBase(contrainteBAS);
            if(maxMin_ligneBas>0){
                //Si le pivot se trouve dans les varibles simples
                if(contrainteBAS.getCoefs().indexOf(maxMin_ligneBas)!=-1){
                    indexColonnePivot = contrainteBAS.getCoefs().indexOf(maxMin_ligneBas);

                    //Détermination de la ligne pivot
                    double minValue = 0;
                    for(Contraintes contrainte : contraintesTab){
                        if(contrainte.getCoefs().get(indexColonnePivot)!=0) {
                            if ((minValue == 0) && ((contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot)) > 0)) {
                                minValue = contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            } else if ((contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot) < minValue) && ((contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot)) > 0)) {
                                minValue = contrainte.getValeur() / contrainte.getCoefs().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            }
                        }
                    }

                    //Effectuer les opérations sur la matrice
                    variableCounterContrainte = 1;
                    variableCounterCoef = 1;
                    double multiplicateur=1;
                    double pivot = contraintesTab.get(indexLignePivot).getCoefs().get(indexColonnePivot);

                    //Traitement des lignes des contraintes
                    //Cas de la ligne Pivot
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs().set(i,contraintesTab.get(indexLignePivot).getCoefs().get(i)/pivot);
                    }
                    for(int i=0;i<contraintesTab.get(indexLignePivot).getCoefs_ecart().size();i++){
                        contraintesTab.get(indexLignePivot).getCoefs_ecart().set(i,contraintesTab.get(indexLignePivot).getCoefs_ecart().get(i)/pivot);
                    }

                    contraintesTab.get(indexLignePivot).setValeur(contraintesTab.get(indexLignePivot).getValeur()/pivot);

                    for(Contraintes contrainte : contraintesTab){

                    //Cas des autres lignes
                    if(variableCounterContrainte != (indexLignePivot+1)){

                        variableCounterCoef = 1;
                            multiplicateur = contrainte.getCoefs().get(indexColonnePivot);
                            for(double coef : contrainte.getCoefs()){
                                contrainte.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            variableCounterCoef = 1;
                            for(double coef : contrainte.getCoefs_ecart()){
                                contrainte.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            contrainte.setValeur(contrainte.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                    }

                        variableCounterContrainte++;
                    }
                    //Traitement de la ligne de la fonction objective
                    variableCounterCoef = 1;
                    multiplicateur = contrainteBAS.getCoefs().get(indexColonnePivot);
                    for(double coef : contrainteBAS.getCoefs()){
                        contrainteBAS.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    variableCounterCoef = 1;
                    for(double coef : contrainteBAS.getCoefs_ecart()){
                        contrainteBAS.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    contrainteBAS.setValeur(contrainteBAS.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));

                }
                else{
                    indexColonnePivot = contrainteBAS.getCoefs_ecart().indexOf(maxMin_ligneBas);

                    //Détermination de la ligne pivot
                    double minValue = 0;
                    for(Contraintes contrainte : contraintesTab){
                        if(contrainte.getCoefs_ecart().get(indexColonnePivot)!=0) {
                            if ((minValue == 0) && ((contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot)) > 0)) {
                                minValue = contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            } else if ((contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot) < minValue) && ((contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot)) > 0)) {
                                minValue = contrainte.getValeur() / contrainte.getCoefs_ecart().get(indexColonnePivot);
                                indexLignePivot = contraintesTab.indexOf(contrainte);
                            }
                        }
                    }

                    //Effectuer les opérations sur la matrice
                    variableCounterContrainte = 1;
                    variableCounterCoef = 1;
                    double multiplicateur=1;
                    double pivot = contraintesTab.get(indexLignePivot).getCoefs_ecart().get(indexColonnePivot);
                    //Traitement des lignes des contraintes
                    for(Contraintes contrainte : contraintesTab){

                        //Cas de la ligne Pivot
                        if(variableCounterContrainte == (indexLignePivot+1)){
                            for(int i=0;i<contrainte.getCoefs().size();i++){
                                contrainte.getCoefs().set(i,contrainte.getCoefs().get(i)/pivot);
                            }
                            for(int i=0;i<contrainte.getCoefs_ecart().size();i++){
                                contrainte.getCoefs_ecart().set(i,contrainte.getCoefs_ecart().get(i)/pivot);
                            }

                            contrainte.setValeur(contrainte.getValeur()/pivot);
                        }
                        //Cas des autres lignes
                        else{
                            variableCounterCoef = 1;
                            multiplicateur = contrainte.getCoefs_ecart().get(indexColonnePivot);
                            for(double coef : contrainte.getCoefs()){
                                contrainte.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            variableCounterCoef = 1;
                            for(double coef : contrainte.getCoefs_ecart()){
                                contrainte.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                                variableCounterCoef++;
                            }
                            contrainte.setValeur(contrainte.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                        }
                        variableCounterContrainte++;
                    }
                    //Traitement de la ligne de la fonction objective
                    variableCounterCoef = 1;
                    multiplicateur = contrainteBAS.getCoefs_ecart().get(indexColonnePivot);
                    for(double coef : contrainteBAS.getCoefs()){
                        contrainteBAS.getCoefs().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    variableCounterCoef = 1;
                    for(double coef : contrainteBAS.getCoefs_ecart()){
                        contrainteBAS.getCoefs_ecart().set((variableCounterCoef-1), (coef-multiplicateur*(contraintesTab.get(indexLignePivot).getCoefs_ecart().get(variableCounterCoef-1))));
                        variableCounterCoef++;
                    }
                    contrainteBAS.setValeur(contrainteBAS.getValeur()-multiplicateur*(contraintesTab.get(indexLignePivot).getValeur()));
                }

            }else{
                // Fin de la maximisation
                isOver = true;
            }
        }

    }

    public double getVariableEnteringBase(Contraintes contrainte){

        if(min_max.getValue().toString().equalsIgnoreCase("Maximisation")){
            double maxCoefs = Collections.max(contrainte.getCoefs());
            double maxCoefsEcart = Collections.max(contrainte.getCoefs_ecart());

            return Math.max(maxCoefs,maxCoefsEcart);
        }
        else{
            double minCoefs = 0;
            for(double coef : contrainte.getCoefs()){
                if(coef != 0 && minCoefs ==0 ){
                    minCoefs = coef;
                }else if(coef != 0 && coef < minCoefs){
                    minCoefs = coef;
                }
            }

            for(double coef : contrainte.getCoefs_ecart()){
                if(coef != 0 && coef < minCoefs){
                    minCoefs = coef;
                }
            }

            return minCoefs;
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
