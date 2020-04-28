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
        parentController = new Simplexe_Controller();
        cButton.setDisable(true);
    }

    @FXML
    public void execute(){

        if(!textField.getText().equalsIgnoreCase("")) {
            if (cButton.isDisable() && !valeur.getText().toString().equalsIgnoreCase("")) {
                cButton.setDisable(false);
            }
            contrainte.getCoefs().add(Integer.parseInt(textField.getText()));
            if(!contraint_label.getText().equalsIgnoreCase("")) {
                contraint_label.setText(contraint_label.getText() + " + "+Integer.parseInt(textField.getText())+"x"+contrainte.getCoefs().size());
            }
            else{
                contraint_label.setText(""+Integer.parseInt(textField.getText())+"x"+contrainte.getCoefs().size());
            }
        }
    }

    public void setParentController(Simplexe_Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void confirmer(){
        contrainte.setValeur(Integer.parseInt(valeur.getText()));
        ((Stage) cButton.getScene().getWindow()).close();
        parentController.addConstraint(contrainte);

    }

    public void setSigne(String txt){
        signe.setText(txt);
    }
}
