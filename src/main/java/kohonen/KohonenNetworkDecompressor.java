package kohonen;

import image.CompressedFrame;
import utils.Utils;

public class KohonenNetworkDecompressor {
    private KohonenNetwork kohonenNetwork;
    private int frameWidthHeight;
    private int[][] decompressedImage;
    private CompressedFrame[][] compressedFrames;

    public KohonenNetworkDecompressor(KohonenNetwork kohonenNetwork, CompressedFrame[][] compressedFrames, int frameWidthHeight) {
        this.kohonenNetwork = kohonenNetwork;
        this.frameWidthHeight = frameWidthHeight;
        this.compressedFrames = compressedFrames;
        this.decompressedImage = new int[compressedFrames.length * frameWidthHeight][compressedFrames[0].length * frameWidthHeight];
    }

    public int[][] decompressImage() {
        int imageStartPixelPositionX = 0, imageStartPixelPositionY;
        for (int compressedFramePositionX = 0; compressedFramePositionX < this.compressedFrames.length; compressedFramePositionX++) {
            imageStartPixelPositionY = 0;

            for (int compressedFramePositionY = 0; compressedFramePositionY < this.compressedFrames[0].length; compressedFramePositionY++) {

                CompressedFrame compressedFrame = this.compressedFrames[compressedFramePositionX][compressedFramePositionY];
                int winningNeuronIndex = compressedFrame.getWinningNeuronIndex();
                double[] pixels = this.kohonenNetwork.getNeuronWeights(winningNeuronIndex);
                int[] denormalizedPixels = Utils.denormalizeVector(pixels, compressedFrame.getBrightness());

                this.savePixelsToImage(denormalizedPixels, imageStartPixelPositionX, imageStartPixelPositionY);

                imageStartPixelPositionY += this.frameWidthHeight;
            }
            imageStartPixelPositionX += this.frameWidthHeight;
        }
        return this.decompressedImage.clone();
    }

    private void savePixelsToImage(int[] denormalizedPixels, int imageStartPixelPositionX, int imageStartPixelPositionY) {
        for (int imagePixelPositionX = imageStartPixelPositionX; imagePixelPositionX < imageStartPixelPositionX + frameWidthHeight; imagePixelPositionX++) {
            int denormalizedPixelsVectorIndex = 0;

            for (int imagePixelPositionY = imageStartPixelPositionY; imagePixelPositionY < imageStartPixelPositionY + frameWidthHeight; imagePixelPositionY++) {
                int pixelValue = denormalizedPixels[denormalizedPixelsVectorIndex];
                decompressedImage[imagePixelPositionX][imagePixelPositionY] = pixelValue;
                denormalizedPixelsVectorIndex++;
            }

        }
    }
}
