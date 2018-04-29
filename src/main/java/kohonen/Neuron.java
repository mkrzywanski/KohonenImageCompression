package kohonen;

import utils.Utils;

public class Neuron {
    private double[] weights;
    private int winnerCounter;

    public Neuron(double[] weights) {
        this.weights = weights;
        this.winnerCounter = 0;
    }

    public double calculateOutput(double[] inputs) {
        double sum = 0;
        for(int i = 0; i < weights.length; i++) {
            sum += Math.pow(inputs[i] - weights[i], 2);
        }
        return Math.sqrt(sum);
    }

    public void modifyWeights(double[] inputs, double learningStep) {
        this.winnerCounter++;
        for(int i = 0; i < weights.length; i++) {
            weights[i] += learningStep * (inputs[i] - weights[i]);
        }
        this.weights = Utils.normalizeVector(weights);
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public int getWinnerCounter() {
        return winnerCounter;
    }

    public void setWinnerCounter(int winnerCounter) {
        this.winnerCounter = winnerCounter;
    }
}
