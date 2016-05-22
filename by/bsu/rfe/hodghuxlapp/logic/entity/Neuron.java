package by.bsu.rfe.hodghuxlapp.logic.entity;
import static java.lang.Math.*;

import java.util.ArrayList;

public class Neuron {
    private static double E_Na = 50; // потенциал покоя натрия
    private static double E_K = -77; // потенциал покоя калия
    private static double E_L = -54.5; // потенциал покоя утечки
	private static double gNa  = 120; // максимальная проводимость натриевого канала
	private static double gK = 36; // максимальная проводимость калиевого канала
	private static double gL = 0.3; // максимальная проводимость канала утечки
	private static double C = 1; // электроемкость мембраны

    public static final double threshold = 23 ;// пороговое значение спайка

	private double n; // активация калиевого канала
	private double m; // активация натриевого канала
	private double h; // инактивация натриевого канала
	private double V; // входное напряжение

	private ArrayList<Double> spikeTimes;    // список времен соответсвующих спайкам
	private ArrayList<NeuronalConnection> inputSynapses;     // список входных соединений
	private ArrayList<NeuronalConnection> outputSynapses;    // список выходных соединений
	private boolean justFired = false;

	private double dndt; // активация калиевого канала
	private double dmdt; // активация натриевого канала
	private double dhdt; // инактивация натриевого канала
	private double dVdt; // входное напряжение

	public Neuron (double n, double m, double h, double V) {
		this.n = n;
		this.m = m;
		this.h = h;
		this.V = V;
		this.spikeTimes = new ArrayList<Double>();
		this.inputSynapses = new ArrayList<NeuronalConnection>();
		this.outputSynapses = new ArrayList<NeuronalConnection>();
	}

	// excitatory == true - синапс возбуждающий и потенциал синапса 0 mV
    // false - синапс ингибирующий и потенциал синапса -80 mV
	public void addInputNeuron (Neuron neuron, double synapseStrength, double synapseLatency, boolean excitatory, double dt) {
		synapseLatency = synapseLatency / dt;
		NeuronalConnection newConnection;
		if (excitatory)
			newConnection = new NeuronalConnection(neuron, synapseStrength, synapseLatency, 0);
		else
			newConnection = new NeuronalConnection(neuron, synapseStrength, synapseLatency, -80);
		this.inputSynapses.add(newConnection);
		neuron.outputSynapses.add(newConnection);
	}

	public NeuronState calcDotProduct (double n, double m, double h, double V, double Iext, double Isyn) {
		double alpha_m, beta_m, minf, tau_m;
        double alpha_n, beta_n, ninf, tau_n;
        double alpha_h, beta_h, hinf, tau_h;
        double I_L, I_Na, I_K;

 		alpha_m = 0.1*(V+40) / (1 - exp(-0.1*(V+40)));
		beta_m = 4*exp(-(V+65)/18);
		minf = alpha_m / (alpha_m + beta_m);
		tau_m = 1 / (alpha_m + beta_m);
		dmdt = (minf - m) / tau_m;

		alpha_n = 0.01*(V+55)/(1 - exp(-0.1*(V+55)));
		beta_n = 0.125*exp(-(V+65)/80);
		ninf = alpha_n / (alpha_n + beta_n);
		tau_n = 1 / (alpha_n + beta_n);
		dndt = (ninf - n) / tau_n;

		alpha_h = 0.07*exp(-(V+65)/20);
		beta_h = 1/(1+exp(-0.1*(V+35)));
		hinf = alpha_h / (alpha_h + beta_h);
		tau_h = 1 / (alpha_h + beta_h);
		dhdt = (hinf - h) / tau_h;

		I_L = gL * (V - E_L);
		I_Na = gNa * pow(m,3) * h * (V - E_Na);
		I_K = gK * pow(n,4) * (V - E_K);

		dVdt = (Iext - I_Na - I_K - I_L + Isyn) / C;
		NeuronState neuronState  = new NeuronState(dndt, dmdt, dhdt, dVdt);
        return neuronState;
	}

	public static double getE_Na() {
		return E_Na;
	}

	public static void setE_Na(double e_Na) {
		E_Na = e_Na;
	}

	public static double geteK() {
		return E_K;
	}

	public static void seteK(double eK) {
		E_K = eK;
	}

	public static double geteL() {
		return E_L;
	}

	public static void seteL(double eL) {
		E_L = eL;
	}

	public static double getgNa() {
		return gNa;
	}

	public static void setgNa(double gNa) {
		Neuron.gNa = gNa;
	}

	public static double getgK() {
		return gK;
	}

	public static void setgK(double gK) {
		Neuron.gK = gK;
	}

	public static double getgL() {
		return gL;
	}

	public static void setgL(double gL) {
		Neuron.gL = gL;
	}

	public static double getC() {
		return C;
	}

	public static void setC(double c) {
		C = c;
	}

	public double getV() {
		return V;
	}

	public void setV(double v) {
		V = v;
	}

	public double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

    public double getDndt() {
        return dndt;
    }

    public void setDndt(double dndt) {
        this.dndt = dndt;
    }

    public double getdVdt() {
        return dVdt;
    }

    public void setdVdt(double dVdt) {
        this.dVdt = dVdt;
    }

    public double getDhdt() {
        return dhdt;
    }

    public void setDhdt(double dhdt) {
        this.dhdt = dhdt;
    }

    public double getDmdt() {
        return dmdt;
    }

    public void setDmdt(double dmdt) {
        this.dmdt = dmdt;
    }

    public ArrayList<Double> getSpikeTimes() {
        return spikeTimes;
    }

    public void setSpikeTimes(ArrayList<Double> spikeTimes) {
        this.spikeTimes = spikeTimes;
    }

    public ArrayList<NeuronalConnection> getInputSynapses() {
        return inputSynapses;
    }

    public void setInputSynapses(ArrayList<NeuronalConnection> inputSynapses) {
        this.inputSynapses = inputSynapses;
    }

    public ArrayList<NeuronalConnection> getOutputSynapses() {
        return outputSynapses;
    }

    public void setOutputSynapses(ArrayList<NeuronalConnection> outputSynapses) {
        this.outputSynapses = outputSynapses;
    }

    public boolean isJustFired() {
        return justFired;
    }

    public void setJustFired(boolean justFired) {
        this.justFired = justFired;
    }
}
