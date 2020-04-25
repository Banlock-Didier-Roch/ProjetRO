import Domain.Contraintes;
import Domain.FO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Ajout_FO_controller {

    @FXML
    private TextField textField;
    @FXML
    private Label contraint_label;
    @FXML
    private Button cButton;

    private FO fo;
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
        fo = new FO();
        parentController = new Simplexe_Controller();
        cButton.setDisable(true);
    }

    @FXML
    public void execute(){

        if(!textField.getText().equalsIgnoreCase("")) {
            if (cButton.isDisable()) {
                cButton.setDisable(false);
            }
            fo.getCoefs().add(Integer.parseInt(textField.getText()));
            if(!contraint_label.getText().equalsIgnoreCase("")) {
                contraint_label.setText(contraint_label.getText() + " + "+Integer.parseInt(textField.getText())+"x"+fo.getCoefs().size());
            }
            else{
                contraint_label.setText(""+Integer.parseInt(textField.getText())+"x"+fo.getCoefs().size());
            }
        }
    }

    public void setParentController(Simplexe_Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void confirmer(){

        ((Stage) cButton.getScene().getWindow()).close();
        parentController.addFo(fo);

    }
}
