package by.bsu.rfe.hodghuxlapp.view.service;

import by.bsu.rfe.hodghuxlapp.logic.util.NeuronMath;
import by.bsu.rfe.hodghuxlapp.logic.entity.Neuron;
import by.bsu.rfe.hodghuxlapp.logic.entity.NeuronalGroup;

public class NeuronGroupCreator {

    final int numberOfNeurons = 9;   // кол-во нейронов
    // массив, содержащий потенциалы нейронов в определенный момент времени
    double[] voltageInTime;

    final double step = 0.05;         // шаг (мсек)

    Neuron[] neurons;
    double[][] Iext;



    public void init(){

        final double simulationTime = 200; // время симуляции 200 мсек
        final int timesCount = (int)(simulationTime / step);     // кол-во временных итераций

        int IextStartTime = 15;     // время начала добавления внешнего тока (мсек)
        int IextEndTime = 20;       // конец добавления внешнего тока
        IextEndTime = (int)(IextEndTime / step) ; // конец с учетом шага (т.е. сколько итераций)
        // массив токов для всех нейронов, ток - 9.7 мА, ток для нейронов номер 0-3
        Iext = NeuronMath.createExternalCurrent(numberOfNeurons, 9.7, 0, 3, IextStartTime, IextEndTime, timesCount);

        double V = -64.8; // начальный потенциал

        // три группы нейронов из 3 штук с начальным потенциалом V
        NeuronalGroup groupA = new NeuronalGroup(3, V, "group A");
        NeuronalGroup groupB = new NeuronalGroup(3, V, "group B");
        NeuronalGroup groupC = new NeuronalGroup(3, V, "group C");

        double synapseStrength; // прочность синапса

        synapseStrength = (double)3 / (double)groupC.size();
        groupA.getInputFromGroup(groupC, synapseStrength, 15, true, step);

        synapseStrength = (double)3 / (double)groupA.size();
        groupB.getInputFromGroup(groupA, synapseStrength, 15, true, step);

        synapseStrength = (double)3 / (double)groupB.size();
        groupC.getInputFromGroup(groupB, synapseStrength, 15, true, step);

        // добавляем нейроны из трех групп в одну группу
        NeuronalGroup allNeurons = new NeuronalGroup("all neurons");
        allNeurons.addAllNeuronsInOtherGroup(groupA);
        allNeurons.addAllNeuronsInOtherGroup(groupB);
        allNeurons.addAllNeuronsInOtherGroup(groupC);

        // переписываем группу из всех нейронов в массив нейронов
        neurons = new Neuron[allNeurons.size()];
        allNeurons.neuronsInGroup.toArray(neurons);

        // массив, содержащий потенциалы нейронов в определенный момент времени
        voltageInTime = new double[numberOfNeurons];
    }

    public double[] calcVoltage(int t){
        // для всех нейронов
        for (int index = 0; index < numberOfNeurons; index++) {
            // нахождение потенциала
            voltageInTime[index] = NeuronMath.calcStates(neurons[index], Iext[t][index], t, step);
        }
        return voltageInTime;
    }

}
