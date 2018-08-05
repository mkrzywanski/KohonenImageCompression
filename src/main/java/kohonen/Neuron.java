package kohonen;

import utils.Utils;

public class Neuron {
    private double[] weights;
    private int winnerCounter;

    Neuron(double[] weights) {
        this.weights = weights;
        this.winnerCounter = 0;
    }

    double calculateOutput(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Math.pow(inputs[i] - weights[i], 2);
        }
        return Math.sqrt(sum);
    }

    void modifyWeights(double[] inputs, double learningStep) {
        this.winnerCounter++;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningStep * (inputs[i] - weights[i]);
        }

        this.weights = Utils.normalizeVector(weights);
    }

    double[] getWeights() {
        return weights;
    }

    int getWinnerCounter() {
        return winnerCounter;
    }

}
