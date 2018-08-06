package image;

import utils.Utils;

public class ImageConverter {
    private int[][] image;
    private int frameWidthHeight;
    private int imageWidthHeight;

    public ImageConverter(int[][] image, int frameWidthHeight, int imageWidthHeight) {
        this.image = image;
        this.frameWidthHeight = frameWidthHeight;
        this.imageWidthHeight = imageWidthHeight;
    }

    public PixelFrame[][] convertImageToPixelFrames() {
        PixelFrame[][] pixelFrames = new PixelFrame[this.imageWidthHeight / this.frameWidthHeight][this.imageWidthHeight / this.frameWidthHeight];

        int pixelFramePositionX = 0, pixelFramePositionY;
        for (int pixelFrameStartPositionX = 0; pixelFrameStartPositionX < this.imageWidthHeight; pixelFrameStartPositionX += this.frameWidthHeight) {
            pixelFramePositionY = 0;
            for (int pixelFrameStartPositionY = 0; pixelFrameStartPositionY < this.image.length; pixelFrameStartPositionY += this.frameWidthHeight) {
                pixelFrames[pixelFramePositionX][pixelFramePositionY] = this.createSinglePixelFrame(pixelFrameStartPositionX, pixelFrameStartPositionY);
                pixelFramePositionY++;
            }
            pixelFramePositionX++;
        }

        return pixelFrames;
    }

    private PixelFrame createSinglePixelFrame(int pixelFrameStartPositionX, int pixelFrameStartPositionY) {
        double[] pixelVector = new double[this.frameWidthHeight * this.frameWidthHeight];
        int pixelVectorIndex = 0;

        for (int imagePixelPositionX = pixelFrameStartPositionX; imagePixelPositionX < pixelFrameStartPositionX + this.frameWidthHeight; imagePixelPositionX++) {
            for (int imagePixelPositionY = pixelFrameStartPositionY; imagePixelPositionY < pixelFrameStartPositionY + this.frameWidthHeight; imagePixelPositionY++, pixelVectorIndex++) {
                pixelVector[pixelVectorIndex] = this.image[imagePixelPositionX][imagePixelPositionY];
            }
        }

        double[] normalizedPixelVector = Utils.normalizeVector(pixelVector);
        double vectorSqrtSum = Utils.calculateSqrtSum(pixelVector);
        return new PixelFrame(normalizedPixelVector, vectorSqrtSum);
    }
}
