package kohonen;

import image.CompressedFrame;
import image.PixelFrame;
import utils.Utils;

public class KohonenNetworkCompressor {
    private KohonenNetwork kohonenNetwork;

    public KohonenNetworkCompressor(KohonenNetwork kohonenNetwork) {
        this.kohonenNetwork = kohonenNetwork;
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
                int winningNeuronIndex = kohonenNetwork.findWinningNeuronIndex(pixelFrames[i][j].getPixels());
                compressedFrames[i][j] = new CompressedFrame(winningNeuronIndex, pixelFrames[i][j].getBrightness());
            }
        }
        return compressedFrames;
    }
}
