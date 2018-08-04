package kohonen;

import image.CompressedFrame;
import image.PixelFrame;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    private int findWinningNeuronIndex(double[] pixels) {
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

    public CompressedFrame[][] compressImage(int[][] image, int frameWidthHeight) {
        PixelFrame[][] pixelFrames = new PixelFrame[image.length / frameWidthHeight][image[0].length / frameWidthHeight];

        int imageXPixelPosition = 0, imageYPixelPosition;
        for (int i = 0; i < image.length; i += frameWidthHeight) {
            imageYPixelPosition = 0;

            for (int j = 0; j < image.length; j += frameWidthHeight) {
                double[] pixelVector = new double[frameWidthHeight * frameWidthHeight];
                int pixelVectorIndex = 0;

                for (int k = i; k < i + frameWidthHeight; k++) {
                    for (int l = j; l < j + frameWidthHeight; l++, pixelVectorIndex++) {
                        pixelVector[pixelVectorIndex] = image[k][l];
                    }
                }
                double[] normalizedPixelVector = Utils.normalizeVector(pixelVector);
                double vectorSqrtSum = Utils.calculateSqrtSum(pixelVector);
                pixelFrames[imageXPixelPosition][imageYPixelPosition] = new PixelFrame(normalizedPixelVector, vectorSqrtSum);

                imageYPixelPosition++;
            }

            imageXPixelPosition++;
        }

        return this.convertPixelFramesToCompressedFrames(pixelFrames);
    }

    private CompressedFrame[][] convertPixelFramesToCompressedFrames(PixelFrame[][] pixelFrames) {
        CompressedFrame[][] compressedFrames = new CompressedFrame[pixelFrames.length][pixelFrames[0].length];
        for (int i = 0; i < pixelFrames.length; i++) {
            for (int j = 0; j < pixelFrames[0].length; j++) {
                int winningNeuronIndex = this.findWinningNeuronIndex(pixelFrames[i][j].getPixels());
                compressedFrames[i][j] = new CompressedFrame(winningNeuronIndex, pixelFrames[i][j].getBrightness());
            }
        }
        return compressedFrames;
    }

    public int[][] decompressImage(CompressedFrame[][] compressedFrames, int frameWidthHeight) {
        int[][] decompressedImage = new int[compressedFrames.length * frameWidthHeight][compressedFrames[0].length * frameWidthHeight];
        int imageXPixelPosition = 0, imageYPixelPosition;

        for (int i = 0; i < compressedFrames.length; i++) {
            imageYPixelPosition = 0;

            for (int j = 0; j < compressedFrames[0].length; j++) {

                CompressedFrame compressedFrame = compressedFrames[i][j];
                int winningNeuronIndex = compressedFrame.getWinningNeuronIndex();
                Neuron neuron = neurons.get(winningNeuronIndex);
                double[] pixels = neuron.getWeights();
                int[] denormalizedPixels = Utils.denormalizeVector(pixels, compressedFrame.getBrightness());

                for (int k = imageXPixelPosition; k < imageXPixelPosition + frameWidthHeight; k++) {
                    int denormalizedPixelsVectorIndex = 0;

                    for (int l = imageYPixelPosition; l < imageYPixelPosition + frameWidthHeight; l++) {
                        decompressedImage[k][l] = denormalizedPixels[denormalizedPixelsVectorIndex];
                        denormalizedPixelsVectorIndex++;
                    }

                }
                imageYPixelPosition += frameWidthHeight;
            }
            imageXPixelPosition += frameWidthHeight;
        }
        return decompressedImage;
    }

    public double calculateCompressionRate(int imageWidthHeight, int frameWidthHeight, CompressedFrame[][] compressedFrames, int bitsPerWeight) {
        double originalImageBitsNumber = Math.pow(imageWidthHeight, 2) * 8;
        double compressedImageBits = Math.pow(frameWidthHeight, 2) * neurons.size() * bitsPerWeight;

        int neuronIndexBitNumber = Utils.minimalNumberOfBits(neurons.size());
        compressedImageBits += neuronIndexBitNumber * compressedFrames.length * compressedFrames.length;

        double maxBrightnessValue = Arrays.stream(compressedFrames)
                .flatMap(Arrays::stream)
                .max(Comparator.comparing(CompressedFrame::getBrightness))
                .get()
                .getBrightness();

        int bitsNumberForBrightness = Utils.minimalNumberOfBits(maxBrightnessValue);
        compressedImageBits += compressedFrames.length * compressedFrames.length * bitsNumberForBrightness;

        return originalImageBitsNumber / compressedImageBits;
    }

    public double calculatePSNR(int[][] originalImage, int[][] decompressedImage) {
        double mse = 0;

        for (int i = 0; i < originalImage.length; i++) {
            for (int j = 0; j < originalImage[0].length; j++) {
                mse += Math.pow(originalImage[i][j] - decompressedImage[i][j], 2);
            }
        }

        mse /= originalImage.length * originalImage.length;
        return 10 * Math.log10(Math.pow(255, 2) / mse);
    }

}
