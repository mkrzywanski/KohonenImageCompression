package image;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPixelFrameGenerator {
    private int[][] image;
    private int imageWidthHeight;
    private int frameWidthHeight;
    private Random random;

    public RandomPixelFrameGenerator(int[][] image, int imageWidthHeight, int frameWidthHeight) {
        this.image = image;
        this.imageWidthHeight = imageWidthHeight;
        this.frameWidthHeight = frameWidthHeight;
        this.random = new Random();
    }

    public List<PixelFrame> generatePixelFramesList(int patternsAmount) {
        List<PixelFrame> pixelFrames = new ArrayList<>(patternsAmount);

        for (int i = 0; i < patternsAmount; i++) {
            pixelFrames.add(generateRandomPixelFrame());
        }

        return pixelFrames;
    }

    private PixelFrame generateRandomPixelFrame() {
        int bound = this.imageWidthHeight - this.frameWidthHeight;
        int firstRowIndex = this.random.nextInt(bound);
        int firstColumnIndex = this.random.nextInt(bound);

        double[] pixelVector = new double[this.frameWidthHeight * this.frameWidthHeight];

        for (int i = firstRowIndex; i < firstRowIndex + frameWidthHeight; i++) {
            for (int j = firstColumnIndex, pixelVectorIndex = 0; j < firstColumnIndex + this.frameWidthHeight; j++, pixelVectorIndex++) {
                pixelVector[pixelVectorIndex] = this.image[i][j];
            }
        }

        pixelVector = Utils.normalizeVector(pixelVector);
        return new PixelFrame(pixelVector);
    }
}
