package by.bsu.rfe.hodghuxlapp.logic.entity;

public class NeuronState {
	
	public double n; // активация калиевого канала
	public double m; // активация натриевого канала
	public double h; // инактивация натриевого канала
	public double V; // входное напряжение, мВ

	public NeuronState(double n, double m, double h,double V) {
		this.n = n;
		this.m = m;
		this.h = h;
		this.V = V;
	}

	public void add(NeuronState otherNeuronState) {
		this.n +=  otherNeuronState.n;
		this.m +=  otherNeuronState.m;
		this.h +=  otherNeuronState.h;
		this.V +=  otherNeuronState.V;
	}
	
}
