import Domain.Contraintes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Ajout_contrainte_controller {

    @FXML
    private TextField textField;
    @FXML
    private TextField valeur;
    @FXML
    private Label contraint_label;
    @FXML
    private Label signe;
    @FXML
    private Button cButton;

    private Contraintes contrainte = new Contraintes();
    private Simplexe_Controller parentController;

    @FXML
    private void initialize() {
        // force the field to be numeric only
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String oldValue, String newValue) {
                if(!isValid(newValue)) {
                    textField.setText(oldValue);
                }
            }
        });

        valeur.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String oldValue, String newValue) {
                if(!isValid(newValue)) {
                    valeur.setText(oldValue);
                }
                else{
                    if(!contraint_label.getText().equalsIgnoreCase("") &&cButton.isDisable() && !contraint_label.getText().equalsIgnoreCase("-") && !contraint_label.getText().equalsIgnoreCase(".")){
                        cButton.setDisable(false);
                    }if(valeur.getText().equalsIgnoreCase("")){
                        cButton.setDisable(true);
                    }if(valeur.getText().equalsIgnoreCase("-")){
                        cButton.setDisable(true);
                    }
                    if(valeur.getText().equalsIgnoreCase(".")){
                        cButton.setDisable(true);
                    }
                }
            }
        });

        parentController = new Simplexe_Controller();
        cButton.setDisable(true);
    }

    private boolean isValid(final String value) {
        if (value.length() == 0 || value.equals("-")) {
            return true;
        }

        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @FXML
    public void execute(){

        if(!textField.getText().equalsIgnoreCase("")) {
            if (cButton.isDisable() && !valeur.getText().toString().equalsIgnoreCase("")) {
                cButton.setDisable(false);
            }
            contrainte.getCoefs().add(Double.parseDouble(textField.getText()));
            if(!contraint_label.getText().equalsIgnoreCase("")) {
                contraint_label.setText(contraint_label.getText() + " + "+Double.parseDouble(textField.getText())+"x"+contrainte.getCoefs().size());
            }
            else{
                contraint_label.setText(""+Double.parseDouble(textField.getText())+"x"+contrainte.getCoefs().size());
            }
        }
    }

    public void setParentController(Simplexe_Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void confirmer(){
        contrainte.setValeur(Double.parseDouble(valeur.getText()));
        ((Stage) cButton.getScene().getWindow()).close();
        parentController.addConstraint(contrainte);

    }

    public void setSigne(String txt){
        signe.setText(txt);
    }
}
