package by.bsu.rfe.hodghuxlapp.logic.util;

import java.util.ArrayList;
import by.bsu.rfe.hodghuxlapp.logic.entity.Neuron;
import by.bsu.rfe.hodghuxlapp.logic.entity.NeuronState;
import by.bsu.rfe.hodghuxlapp.logic.entity.NeuronalConnection;

public class NeuronMath {

	public static double calcIsynaptic (Neuron currentNeuron, double currentTime, double delta_t)
	{
		delta_t = 1;
		double Isyn = 0;
		double tauD = 10;
		double tauR = 1;
		
		if (currentNeuron.getInputSynapses() != null) {
			ArrayList<NeuronalConnection> input = currentNeuron.getInputSynapses();
			for (NeuronalConnection currentSynapse : input) {
				currentSynapse.neuronYrecursive = currentSynapse.neuronYrecursive * Math.exp (-delta_t /tauD) ;
				currentSynapse.neuronZrecursive = currentSynapse.neuronZrecursive * Math.exp (-delta_t /tauR) ;
				if(currentSynapse.lastSpikeTime.size() > 0  && (currentTime - currentSynapse.lastSpikeTime.peek() -
                        currentSynapse.synapseLatency) >= 0) {
					currentSynapse.lastSpikeTime.poll();
					currentSynapse.neuronYrecursive = currentSynapse.neuronYrecursive + 1 / (tauD - tauR);
					currentSynapse.neuronZrecursive = currentSynapse.neuronZrecursive + 1 / (tauD - tauR);
				}
				Isyn += -1 * currentSynapse.synapseStrength *(currentSynapse.neuronYrecursive - currentSynapse.neuronZrecursive ) *
                        (currentNeuron.getV() - currentSynapse.vSyn );
			}
		}
		return Isyn;
	}
    // создать внешний ток
    public static double[][] createExternalCurrent (int numOfNeurons, double currentAmplitude, int startNeuronIndex, int endNeuronIndex, int startTime, int endTime, int totalTime) {
        double[][] externalCurrentInTime = new double[totalTime][numOfNeurons];
        // если значения времени и номера нейрона корректны
        if (startTime >= 0 & startNeuronIndex >= 0 & endTime <= totalTime & endNeuronIndex <=numOfNeurons) {
            // то для всех временных точек и всех нейронов
            for (int j = startTime; j < endTime; j++)
                for (int i = startNeuronIndex; i < endNeuronIndex; i++) {
                    // записать в массив токов текущий ток
                    externalCurrentInTime[j][i] = currentAmplitude;
                }
        }
        return externalCurrentInTime;
    }

    //  алгоритм Рунге-Кутта 4-ого порядка(метод решения дифференциальных уравнений)
    private static void rk4(Neuron neuron, double t, double h, double Iext, double Isyn)
    {
        double hh = h*0.5;

        double tempN,tempM,tempH,tempV;
        NeuronState dydt,dyt,dym;

        dydt =  neuron.calcDotProduct(neuron.getN(), neuron.getM(), neuron.getH(),neuron.getV(), Iext, Isyn);

        tempN = neuron.getN() + hh*dydt.n;
        tempM = neuron.getM() + hh*dydt.m;
        tempH = neuron.getH() + hh*dydt.h;
        tempV = neuron.getV() + hh*dydt.V;
        dyt = neuron.calcDotProduct(tempN,tempM,tempH,tempV, Iext, Isyn);

        tempN = neuron.getN() + hh*dyt.n;
        tempM = neuron.getM() + hh*dyt.m;
        tempH = neuron.getH() + hh*dyt.h;
        tempV = neuron.getV() + hh*dyt.V;
        dym = neuron.calcDotProduct(tempN,tempM,tempH,tempV, Iext, Isyn);

        tempN = neuron.getN() + hh*dym.n;
        tempM = neuron.getM() + hh*dym.m;
        tempH = neuron.getH() + hh*dym.h;
        tempV = neuron.getV() + hh*dym.V;
        dym.add(dyt);
        dyt = neuron.calcDotProduct(tempN,tempM,tempH,tempV,Iext, Isyn);

        neuron.setN(neuron.getN() + (h/6) *(dydt.n + dyt.n + 2*dym.n));
        neuron.setM(neuron.getM() + (h/6) *(dydt.m + dyt.m + 2*dym.m));
        neuron.setH(neuron.getH() + (h/6) *(dydt.h + dyt.h + 2*dym.h));
        neuron.setV(neuron.getV() + (h/6) *(dydt.V + dyt.V + 2*dym.V));
    }

    public static double calcStates (Neuron neuron, double Iext, double t,double dt) {
        double oldDVdt = neuron.getdVdt();
        double Isyn = calcIsynaptic(neuron, t, dt);
        rk4(neuron, t, dt, Iext, Isyn);
        checkForSpike(neuron, oldDVdt, t);
        return neuron.getV();
    }

    // проверить наличие пика
    private static void checkForSpike(Neuron neuron, double oldVdot, double t)
    {
        if (oldVdot > 0 && neuron.getdVdt() < 0 && neuron.getV() > Neuron.threshold) {
            neuron.getSpikeTimes().add(t);
            neuron.setJustFired(true);
            for (NeuronalConnection synapse : neuron.getOutputSynapses())
                synapse.lastSpikeTime.add(t);
        }
        else
            neuron.setJustFired(false);
    }


}
