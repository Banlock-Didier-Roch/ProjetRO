import Domain.Contraintes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class graphique_Controller {
    @FXML
    private VBox historique;
    @FXML
    private VBox vbox_contenu;
    @FXML
    private Button ajouter;
    @FXML
    private BorderPane border;
    @FXML
    private AnchorPane ancorePaneContenu;
    private int test = 0;
    Stage stage;
    Parent root;


    @FXML
    private void initialize() {
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

    @FXML
    public void optimiser() {

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Années");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2010);
        xAxis.setUpperBound(2015);
        xAxis.setTickUnit(1);
        xAxis.setMinorTickCount(0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'étudiants");
        yAxis.setMinorTickCount(0);

        LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
        chart.setTitle("Evolution des effectifs HEIA-FR");
        chart.setLegendSide(Side.LEFT);

        chart.setData(getHEIAdata());

        ancorePaneContenu.getChildren().add(chart);

    }

    private ObservableList<XYChart.Series<Number, Number>> getHEIAdata() {

        ObservableList<XYChart.Series<Number, Number>> chartData = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
        XYChart.Series<Number, Number> seriesInfo = new XYChart.Series<>();
        seriesInfo.setName("Informatique");
        seriesInfo.getData().addAll(new XYChart.Data<>(2010, 85), new XYChart.Data<>(2011, 85), new XYChart.Data<>(2012, 82), new XYChart.Data<>(2013, 84), new XYChart.Data<>(2014, 73), new XYChart.Data<>(2015, 88));
        XYChart.Series<Number, Number> seriesTelecom = new XYChart.Series<>();
        seriesTelecom.setName("Telecom");
        seriesTelecom.getData().addAll(new XYChart.Data<>(2010, 56), new XYChart.Data<>(2011, 67), new XYChart.Data<>(2012, 67),
                new XYChart.Data<>(2013, 79), new XYChart.Data<>(2014, 81), new XYChart.Data<>(2015, 78));
        XYChart.Series<Number, Number> seriesMeca = new XYChart.Series<>();
        seriesMeca.setName("Mécanique");

        seriesMeca.getData().addAll(
                                    new XYChart.Data<>(2010, 112),
                                    new XYChart.Data<>(2015, 109));
        chartData.addAll(seriesInfo, seriesTelecom, seriesMeca);
        return chartData;

    }
}
