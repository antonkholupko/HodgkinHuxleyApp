package by.bsu.rfe.hodghuxlapp.view;

import by.bsu.rfe.hodghuxlapp.view.service.NeuronGroupCreator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import by.bsu.rfe.hodghuxlapp.logic.entity.Neuron;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    @FXML
    Button btnStart;

    @FXML
    TextField Cm;
    @FXML
    TextField gNa;
    @FXML
    TextField gK;
    @FXML
    TextField gL;
    @FXML
    TextField ENa;
    @FXML
    TextField EK;
    @FXML
    TextField EL;

    @FXML
    Pane pane1;
    @FXML
    Pane pane2;
    @FXML
    Pane pane3;


    @FXML
    public void btnStartOnClick() {

        if (!Cm.getText().isEmpty()) {
            Neuron.setC(Double.valueOf(Cm.getText()));
        }
        if (!gNa.getText().isEmpty()) {
            Neuron.setgNa(Double.valueOf(gNa.getText()));
        }
        if (!gK.getText().isEmpty()) {
            Neuron.setgK(Double.valueOf(gK.getText()));
        }
        if (!gL.getText().isEmpty()) {
            Neuron.seteL(Double.valueOf(gL.getText()));
        }
        if (!ENa.getText().isEmpty()) {
            Neuron.seteL(Double.valueOf(ENa.getText()));
        }
        if (!EK.getText().isEmpty()) {
            Neuron.seteL(Double.valueOf(EK.getText()));
        }
        if (!EL.getText().isEmpty()) {
            Neuron.seteL(Double.valueOf(EL.getText()));
        }

        NeuronGroupCreator calcVoltage = new NeuronGroupCreator();
        calcVoltage.init();

        AnimationCreator animationCreator = new AnimationCreator(calcVoltage);
        animationCreator.setLineChartA(animationCreator.createChart(animationCreator.getmSecondsDataSeries()));
        pane1.getChildren().add(animationCreator.getLineChartA());
        animationCreator.setLineChartB(animationCreator.createChart(animationCreator.getmSecondsDataSeries2()));
        pane2.getChildren().add(animationCreator.getLineChartB());
        animationCreator.setLineChartC(animationCreator.createChart(animationCreator.getmSecondsDataSeries3()));
        pane3.getChildren().add(animationCreator.getLineChartC());
        animationCreator.start();
    }

}
