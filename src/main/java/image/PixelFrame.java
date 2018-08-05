package image;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PixelFrame {
    private double[] pixels;
    private double brightness;

    private PixelFrame(double[] pixels) {
        this.pixels = pixels;
        this.brightness = 0;
    }

    public PixelFrame(double[] pixels, double brightness) {
        this.pixels = pixels;
        this.brightness = brightness;
    }

    public static List<PixelFrame> generatePixelFramesList(int[][] image, int patternsAmount, int frameWidthHeight) {
        List<PixelFrame> pixelFrames = new ArrayList<>(patternsAmount);
        for (int i = 0; i < patternsAmount; i++) {
            pixelFrames.add(generateRandomPixelFrame(image, frameWidthHeight));
        }
        return pixelFrames;
    }

    private static PixelFrame generateRandomPixelFrame(int[][] image, int frameWidthHeight) {
        int firstRowIndex = new Random().nextInt(image.length - frameWidthHeight);
        int firstColumnIndex = new Random().nextInt(image[0].length - frameWidthHeight);

        double[] pixelVector = new double[frameWidthHeight * frameWidthHeight];

        for (int i = firstRowIndex; i < firstRowIndex + frameWidthHeight; i++) {
            for (int j = firstColumnIndex, pixelVectorIndex = 0; j < firstColumnIndex + frameWidthHeight; j++, pixelVectorIndex++) {
                pixelVector[pixelVectorIndex] = image[i][j];
            }
        }

        pixelVector = Utils.normalizeVector(pixelVector);
        return new PixelFrame(pixelVector);
    }

    public double[] getPixels() {
        return pixels;
    }

    public void setPixels(double[] pixels) {
        this.pixels = pixels;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}
