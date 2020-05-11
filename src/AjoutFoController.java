import Domain.FO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjoutFoController {

    @FXML
    private TextField textField;
    @FXML
    private Label contraint_label;
    @FXML
    private Button cButton;

    private FO fo;
    private graphique_Controller parentController;

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
        fo = new FO();
        parentController = new graphique_Controller();
        cButton.setDisable(true);
    }

    @FXML
    public void execute(){

        if(!textField.getText().equalsIgnoreCase("")) {
            if (cButton.isDisable()) {
                cButton.setDisable(false);
            }
            fo.getCoefs().add(Double.parseDouble(textField.getText()));
            if(!contraint_label.getText().equalsIgnoreCase("")) {
                contraint_label.setText(contraint_label.getText() + " + "+Double.parseDouble(textField.getText())+"x"+fo.getCoefs().size());
            }
            else{
                contraint_label.setText(""+Double.parseDouble(textField.getText())+"x"+fo.getCoefs().size());
            }
        }
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

    public void setParentController(graphique_Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void confirmer(){

        ((Stage) cButton.getScene().getWindow()).close();
        parentController.addFo(fo);

    }
}
