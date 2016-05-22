package by.bsu.rfe.hodghuxlapp.view;

import by.bsu.rfe.hodghuxlapp.view.service.NeuronGroupCreator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public class AnimationCreator {

    private XYChart.Series<Number,Number> mSecondsDataSeries = new XYChart.Series<>();
    private XYChart.Series<Number,Number> mSecondsDataSeries2 = new XYChart.Series<>();
    private XYChart.Series<Number,Number> mSecondsDataSeries3 = new XYChart.Series<>();
    private Timeline animation;
    private int timeCount = 0;
    private int timeInMiliSeconds = 0;
    private double[] voltages;
    private NeuronGroupCreator calcVoltage;
    private LineChart<Number,Number> lineChartA;
    private LineChart<Number,Number> lineChartB;
    private LineChart<Number,Number> lineChartC;

    public AnimationCreator(NeuronGroupCreator calcVoltage){
        this.calcVoltage=calcVoltage;
    }

    private void init() {
        animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(int count=0; count < 2; count++) {
                    nextTime();
                    plotTime();
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        lineChartA.setTitle("Group A");
        lineChartB.setTitle("Group B");
        lineChartC.setTitle("Group C");
    }

    protected LineChart<Number, Number> createChart(XYChart.Series<Number, Number> dataSeries) {
        NumberAxis xAxis = new NumberAxis(0,200,10);
        final NumberAxis yAxis = new NumberAxis(-90,50,10);
        final LineChart<Number,Number> lc = new LineChart<>(xAxis,yAxis);
        lc.setCreateSymbols(false);
        lc.setAnimated(false);
        lc.setLegendVisible(false);
        lc.setMaxSize(900,220);
        lc.setMinSize(900,220);
        xAxis.setLabel("Time");
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("Voltage");
        dataSeries.getData().add(new XYChart.Data<>(timeInMiliSeconds, -60));
        lc.getData().add(dataSeries);
        return lc;
    }

    private void nextTime() {
        timeCount++;
        if (timeCount%20==0)
            timeInMiliSeconds++;
    }

    private void plotTime() {
        if (!(timeInMiliSeconds > 0 & timeInMiliSeconds%200==0)) {
            voltages = calcVoltage.calcVoltage(timeCount);
            mSecondsDataSeries.getData().add(new XYChart.Data<>(timeCount * 0.05, voltages[0]));
            mSecondsDataSeries2.getData().add(new XYChart.Data<>(timeCount * 0.05, voltages[3]));
            mSecondsDataSeries3.getData().add(new XYChart.Data<>(timeCount * 0.05, voltages[6]));
        } else {
            timeCount = 0;
            timeInMiliSeconds = 0;

            calcVoltage.init();

            lineChartA.getData().removeAll(mSecondsDataSeries);
            mSecondsDataSeries = new XYChart.Series<>();
            mSecondsDataSeries.getData().add(new XYChart.Data<>(timeInMiliSeconds, -60));
            lineChartA.getData().add(mSecondsDataSeries);

            lineChartB.getData().removeAll(mSecondsDataSeries2);
            mSecondsDataSeries2 = new XYChart.Series<>();
            mSecondsDataSeries2.getData().add(new XYChart.Data<>(timeInMiliSeconds, -60));
            lineChartB.getData().add(mSecondsDataSeries2);

            lineChartC.getData().removeAll(mSecondsDataSeries3);
            mSecondsDataSeries3 = new XYChart.Series<>();
            mSecondsDataSeries3.getData().add(new XYChart.Data<>(timeInMiliSeconds, -60));
            lineChartC.getData().add(mSecondsDataSeries3);
        }
    }

    public void stop() {
        animation.pause();
    }

    public void start() {
        init();
        animation.play();
    }

    public XYChart.Series<Number, Number> getmSecondsDataSeries() {
        return mSecondsDataSeries;
    }

    public XYChart.Series<Number, Number> getmSecondsDataSeries2() {
        return mSecondsDataSeries2;
    }

    public XYChart.Series<Number, Number> getmSecondsDataSeries3() {
        return mSecondsDataSeries3;
    }

    public void setLineChartA(LineChart<Number, Number> lineChartA) {
        this.lineChartA = lineChartA;
    }

    public void setLineChartB(LineChart<Number, Number> lineChartB) {
        this.lineChartB = lineChartB;
    }

    public void setLineChartC(LineChart<Number, Number> lineChartC) {
        this.lineChartC = lineChartC;
    }

    public LineChart<Number, Number> getLineChartA() {
        return lineChartA;
    }

    public LineChart<Number, Number> getLineChartB() {
        return lineChartB;
    }

    public LineChart<Number, Number> getLineChartC() {
        return lineChartC;
    }
}
