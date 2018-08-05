package kohonen;

import image.CompressedFrame;
import utils.Utils;

public class KohonenNetworkDecompressor {
    private KohonenNetwork kohonenNetwork;

    public KohonenNetworkDecompressor(KohonenNetwork kohonenNetwork) {
        this.kohonenNetwork = kohonenNetwork;
    }

    public int[][] decompressImage(CompressedFrame[][] compressedFrames, int frameWidthHeight) {
        int[][] decompressedImage = new int[compressedFrames.length * frameWidthHeight][compressedFrames[0].length * frameWidthHeight];
        int imageXPixelPosition = 0, imageYPixelPosition;

        for (int i = 0; i < compressedFrames.length; i++) {
            imageYPixelPosition = 0;

            for (int j = 0; j < compressedFrames[0].length; j++) {

                CompressedFrame compressedFrame = compressedFrames[i][j];
                int winningNeuronIndex = compressedFrame.getWinningNeuronIndex();
                double[] pixels = kohonenNetwork.getNeuronWeights(winningNeuronIndex);
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
}
