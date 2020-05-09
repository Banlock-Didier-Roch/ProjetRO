import Domain.Contraintes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjoutContraintesController {

    @FXML
    private TextField textField;

    @FXML
    private Label contraint_label;

    @FXML
    private Button cButton;

    @FXML
    private TextField valeur;

    @FXML
    private Label signe;

    private Contraintes contrainte = new Contraintes();
    private graphique_Controller parentController;

    @FXML
    private void initialize() {
        // force the field to be numeric only
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        valeur.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    valeur.setText(newValue.replaceAll("[^\\d]", ""));
                }
                else{
                    if(!contraint_label.getText().equalsIgnoreCase("") && cButton.isDisable()){
                        cButton.setDisable(false);
                    }else if(valeur.getText().equalsIgnoreCase("")){
                        cButton.setDisable(true);
                    }
                }
            }
        });
        parentController = new graphique_Controller();
        cButton.setDisable(true);
    }


    @FXML
    void confirmer(ActionEvent event) {
        contrainte.setValeur(Integer.parseInt(valeur.getText()));
        ((Stage) cButton.getScene().getWindow()).close();
        parentController.addConstraint(contrainte);
    }

    @FXML
    void execute(ActionEvent event) {

        if(!textField.getText().equalsIgnoreCase("")) {
            if (cButton.isDisable() && !valeur.getText().toString().equalsIgnoreCase("")) {
                cButton.setDisable(false);
            }
            contrainte.getCoefs().add(Double.parseDouble(textField.getText()));
            if(!contraint_label.getText().equalsIgnoreCase("")) {
                contraint_label.setText(contraint_label.getText() + " + "+Integer.parseInt(textField.getText())+"x"+contrainte.getCoefs().size());
            }
            else{
                contraint_label.setText(""+Integer.parseInt(textField.getText())+"x"+contrainte.getCoefs().size());
            }
        }
    }

    public void setParentController(graphique_Controller parentController) {
        this.parentController = parentController;
    }

    public void setSigne(String txt){
        signe.setText(txt);
    }
}
