package by.bsu.rfe.hodghuxlapp.logic.entity;

import java.util.LinkedList;

public class NeuronalGroup {
    public LinkedList<Neuron> neuronsInGroup;
public String groupName;

    public NeuronalGroup(String groupName) {
        this.groupName = groupName;
        this.neuronsInGroup = new LinkedList<Neuron>();
    }

    public NeuronalGroup(int numberOfMembers, double V, String groupName) {
        neuronsInGroup = new LinkedList<Neuron>();
        this.groupName = groupName;
        for (int i = 0; i < numberOfMembers; i++) {
            neuronsInGroup.add(new Neuron(0, 0, 0, V));
        }
    }

    public void getInputFromNeuron(Neuron inputNeuron, double synapseStrength, double synapseLatency, boolean isExitatory, double dt) {
        for (Neuron neuronInGroup : neuronsInGroup) {
            neuronInGroup.addInputNeuron(inputNeuron, synapseStrength, synapseLatency, isExitatory, dt);
        }
    }

    public void getInputFromGroup(NeuronalGroup inputGroup, double synapseStrength, double synapseLatency, boolean isExitatory, double dt) {
        for (Neuron inputNeuron : inputGroup.neuronsInGroup) {
            this.getInputFromNeuron(inputNeuron, synapseStrength, synapseLatency, isExitatory, dt);
        }
    }


    public void addNeuron(Neuron newNeuron) {
        this.neuronsInGroup.add(newNeuron);
    }

    public void addAllNeuronsInOtherGroup(NeuronalGroup newGroup) {
        for (Neuron newNeuron : newGroup.neuronsInGroup) {
            this.addNeuron(newNeuron);
        }
    }

    public int size() {
        return neuronsInGroup.size();
    }
}
