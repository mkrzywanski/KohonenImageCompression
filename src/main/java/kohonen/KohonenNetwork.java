package kohonen;

import image.CompressedFrame;
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

        for(int i = 0; i < neuronsCount; i++) {
            double[] weights = Utils.generateRandomWeights(weightsCount);
            double[] normalizedWeights = Utils.normalizeVector(weights);
            neurons.add(new Neuron(normalizedWeights));
        }
    }

    public void processPixelFrame(PixelFrame pixelFrame) {
        int winningNeuronIndex = this.findWinningNeuronIndex(pixelFrame.getPixels());
        this.modifyNeuronWeights(winningNeuronIndex, pixelFrame.getPixels());
    }

    private int findWinningNeuronIndex(double[] pixels) {
        List<Double> neuronOutputs = new ArrayList<>();
        for(Neuron neuron : neurons) {
            neuronOutputs.add(neuron.calculateOutput(pixels));
        }
        return Utils.minIndex(neuronOutputs);
    }

    private void modifyNeuronWeights(int neuronIndex, double[] pixels) {
        Neuron neuron = this.neurons.get(neuronIndex);
        neuron.modifyWeights(pixels, this.trainingStep);
    }

    public void deleteDeadNeurons() {
        for(int i = 0; i < neurons.size(); i++) {
            if(neurons.get(i).getWinnerCounter() < this.minimalWinnerCount) {
                neurons.remove(neurons.get(i));
            }
        }
    }

    public CompressedFrame[][] compressImage(int[][] image, int frameWidthHeight) {
        PixelFrame[][] pixelFrames = new PixelFrame[image.length / frameWidthHeight][image[0].length / frameWidthHeight];
        int x = 0, y, z;
        for(int i = 0; i < image.length; i+=frameWidthHeight) {
            y=0;
            for(int j = 0; j < image.length; j+=frameWidthHeight) {
                double[] pixels = new double[frameWidthHeight * frameWidthHeight];
                z=0;
                for(int k = i; k < i+frameWidthHeight;k++) {
                    for(int l = j; l < j+frameWidthHeight; l++) {
                        pixels[z] = image[k][l];
                        z++;
                    }
                }
                pixelFrames[x][y] = new PixelFrame(Utils.normalizeVector(pixels), Utils.calculateSqrtSum(pixels));
                y++;
            }
            x++;
        }


        return this.convertPixelFramesToCompressedFrames(pixelFrames);
    }

    private CompressedFrame[][] convertPixelFramesToCompressedFrames(PixelFrame[][] pixelFrames) {
        CompressedFrame[][] compressedFrames = new CompressedFrame[pixelFrames.length][pixelFrames[0].length];
        for(int i = 0; i < pixelFrames.length; i++) {
            for(int j = 0; j < pixelFrames[0].length; j++) {
                int winningNeuronIndex = this.findWinningNeuronIndex(pixelFrames[i][j].getPixels());
                compressedFrames[i][j] = new CompressedFrame(winningNeuronIndex, pixelFrames[i][j].getBrightness());
            }
        }
        return compressedFrames;
    }

    public int[][] decompressImage(CompressedFrame[][] compressedFrames, int frameWidthHeight) {
        int[][] decompressedImage = new int[compressedFrames.length*frameWidthHeight][compressedFrames[0].length * frameWidthHeight];
        int x = 0 , y, z;

        for(int i = 0; i < compressedFrames.length; i++) {
            y=0;
            for(int j = 0; j < compressedFrames[0].length; j++) {
                CompressedFrame compressedFrame = compressedFrames[i][j];
                int number = compressedFrame.getNeuronNumber();
                Neuron neuron = neurons.get(number);
                double[] pixels = neuron.getWeights();
                int[] denormalizedPixels = Utils.denormalizeVector(pixels, compressedFrame.getBrightness());
                for(int k = x; k < x+frameWidthHeight; k++){
                    z=0;
                    for(int l = y; l < y+frameWidthHeight; l++) {
                        decompressedImage[k][l] = denormalizedPixels[z];
                        z++;
                    }
                }
                y+=frameWidthHeight;
            }
            x+=frameWidthHeight;
        }
        return decompressedImage;
    }

    public double calculateCompressionRate(int imageWidthHeight, int frameWidthHeight, CompressedFrame[][] compressedFrames, int bitsPerWeight) {
        double originalImageBits = Math.pow(imageWidthHeight, 2) * 8;
        double compressedImageBits = Math.pow(frameWidthHeight, 2) * neurons.size() * bitsPerWeight;

        int bitNumberForNeuronIndex = Utils.minimalNumberOfBits(neurons.size());
        compressedImageBits += bitNumberForNeuronIndex * compressedFrames.length * compressedFrames.length;

        List<Double> numbers = new ArrayList<>();

        for(int i=0; i < compressedFrames.length; i++) {
            for(int j = 0; j < compressedFrames[0].length; j++) {
                numbers.add(compressedFrames[i][j].getBrightness());
            }
        }

        double max = numbers.stream().max(Double::compareTo).get();
        int bitsNumberForBrightness = Utils.minimalNumberOfBits(max);
        compressedImageBits += compressedFrames.length * compressedFrames.length * bitsNumberForBrightness;

        return originalImageBits / compressedImageBits;
    }

    public double calculatePSNR(int[][] originalImage, int[][] decompressedImage) {
        double mse = 0;
        for(int i = 0; i < originalImage.length; i++) {
            for(int j = 0; j < originalImage[0].length; j++) {
                mse += Math.pow(originalImage[i][j] - decompressedImage[i][j], 2);
            }
        }
        mse /= originalImage.length * originalImage.length;
        return 10 * Math.log10(Math.pow(255,2) / mse);
    }

}
