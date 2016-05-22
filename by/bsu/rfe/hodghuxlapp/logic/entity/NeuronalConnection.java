package by.bsu.rfe.hodghuxlapp.logic.entity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NeuronalConnection {

	public Neuron neuron;
	public double synapseStrength;
	public double synapseLatency;
	public double vSyn;
	public double neuronYrecursive;
	public double neuronZrecursive;
	public ConcurrentLinkedQueue<Double> lastSpikeTime ;


	public NeuronalConnection(Neuron neuron,double synapseStrength,double synapseLatency, double vSyn) {
		this.neuron = neuron;
		this.synapseStrength = synapseStrength;
		this.synapseLatency = synapseLatency;
		this.vSyn = vSyn;
		this.neuronYrecursive = 0;
		this.neuronZrecursive = 0;
		this.lastSpikeTime = new ConcurrentLinkedQueue<Double>();

	}
}
