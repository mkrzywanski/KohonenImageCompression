package kohonen;

import image.PixelFrame;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class KohonenNetwork {
    private List<Neuron> neurons;
    private double trainingStep;
    private int minimalWinnerCount;

    public KohonenNetwork(int neuronsCount, int weightsCount, double trainingStep, int minimalWinnerCount) {
        this.neurons = new ArrayList<>(neuronsCount);
        this.trainingStep = trainingStep;
        this.minimalWinnerCount = minimalWinnerCount;

        for (int i = 0; i < neuronsCount; i++) {
            double[] weights = Utils.generateRandomWeights(weightsCount);
            double[] normalizedWeights = Utils.normalizeVector(weights);
            neurons.add(new Neuron(normalizedWeights));
        }
    }

    public void processPixelFrame(PixelFrame pixelFrame) {
        int winningNeuronIndex = this.findWinningNeuronIndex(pixelFrame.getPixels());
        this.modifyNeuronWeights(winningNeuronIndex, pixelFrame.getPixels());
    }

    public int findWinningNeuronIndex(double[] pixels) {
        List<Double> neuronOutputs = new ArrayList<>();
        for (Neuron neuron : neurons) {
            neuronOutputs.add(neuron.calculateOutput(pixels));
        }
        return Utils.findMinValueIndex(neuronOutputs);
    }

    private void modifyNeuronWeights(int neuronIndex, double[] pixels) {
        Neuron neuron = this.neurons.get(neuronIndex);
        neuron.modifyWeights(pixels, this.trainingStep);
    }

    public void deleteDeadNeurons() {
        for (int i = 0; i < neurons.size(); i++) {
            if (neurons.get(i).getWinnerCounter() < this.minimalWinnerCount) {
                neurons.remove(neurons.get(i));
            }
        }
    }

    public double[] getNeuronWeights(int neuronIndex) {
        return this.neurons.get(neuronIndex).getWeights();
    }
}
