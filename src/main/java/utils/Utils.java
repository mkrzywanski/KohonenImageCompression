package utils;

import image.PixelFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static double[] normalizeVector(double[] vector) {
        double[] normalizedVector = new double[vector.length];

        double sumSqrt = calculateSqrtSum(vector);

        for(int i = 0; i < normalizedVector.length; i++) {
            normalizedVector[i] = vector[i] / sumSqrt;
        }

        return normalizedVector;
    }

    public static double calculateSqrtSum(double[] vector) {
        double sum = 0;
        for(int i = 0; i < vector.length; i++) {
            sum += Math.pow(vector[i],2);
        }
        return Math.sqrt(sum);
    }

    public static PixelFrame generateRandomPattern(int[][] image, int frameWidthHeight) {
        int firstRowNumber = new Random().nextInt(image.length - frameWidthHeight);
        int firstColumnNumber = new Random().nextInt(image[0].length - frameWidthHeight);

        double[] vector = new double[frameWidthHeight * frameWidthHeight];
        int counter = 0;

        for(int i = firstRowNumber; i < firstRowNumber + frameWidthHeight; i++) {
            for(int j = firstColumnNumber; j < firstColumnNumber + frameWidthHeight; j++) {
                vector[counter] = image[i][j];
                counter++;
            }
        }
        vector = Utils.normalizeVector(vector);
        return new PixelFrame(vector);
    }

    public static double[] generateRandomWeights(int weightsCount) {
        double[] weights = new double[weightsCount];
        Random random = new Random();
        for(int i = 0; i < weightsCount; i++) {
            weights[i] = random.nextDouble();
        }
        return weights;
    }

    public static int minIndex(List<Double> list) {
        Integer i=0;
        Double min = null;
        int minIndex=-1;
        for (Double x : list) {
            if ((x!=null) && ((min==null) || (x<min))) {
                min = x;
                minIndex = i;
            }
            i++;
        }
        if(minIndex == -1)
            minIndex = 1;
        return minIndex;
    }

    public static int maxIndex(List<Double> list) {
        Integer i=0;
        Double max = null;
        int maxIndex=-1;
        for (Double x : list) {
            if ((x!=null) && ((max==null) || (x>max))) {
                max = x;
                maxIndex = i;
            }
            i++;
        }
        return maxIndex;
    }

    public static List<PixelFrame> generatePatternsList(int[][] image, int patternsCount, int frameWidthHeight) {
        List<PixelFrame> pixelFrames = new ArrayList<>();
        for(int i = 0; i < patternsCount; i++) {
            pixelFrames.add(generateRandomPattern(image,frameWidthHeight));
        }
        return pixelFrames;
    }

    public static int[] denormalizeVector(double[] vector, double value) {
        int[] vectorToReturn = new int[vector.length];
        for(int i = 0; i < vector.length; i++) {
            vectorToReturn[i] = (int) Math.round(vector[i] * value);
        }
        return vectorToReturn;
    }

    public static int[] convert2darrayto1d(int[][] array2D) {
        int[] arrayToReturn = new int[array2D.length * array2D.length];

        for(int i = 0; i < array2D.length; i++) {
            for(int j = 0; j < array2D[0].length; j++) {
                arrayToReturn[i*array2D.length+j] = array2D[i][j];
            }
        }
        return arrayToReturn;
    }

    public static int minimalNumberOfBits(double number) {
        return (int)Math.floor(Math.log(number))+1;
    }
}
