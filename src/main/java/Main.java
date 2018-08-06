import config.Configuration;
import image.*;
import kohonen.KohonenNetwork;
import kohonen.KohonenNetworkCompressor;
import kohonen.KohonenNetworkDecompressor;
import kohonen.evaluator.KohonenNetworkEvaluator;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String... args) throws IOException {
        ImageLoader imageLoader = new ImageLoader();
        int[][] originalImage = imageLoader.loadImage(Configuration.FILE_NAME);

        KohonenNetwork kohonenNetwork = new KohonenNetwork(
                Configuration.NEURONS_NUMBER,
                (int) Math.pow(Configuration.FRAME_WIDTH_HEIGHT, 2),
                Configuration.TRAINING_STEP,
                Configuration.MINIMAL_WINNER_COUNTER
        );

        RandomPixelFrameGenerator randomPixelFrameGenerator = new RandomPixelFrameGenerator(originalImage,
                Configuration.IMAGE_WIDTH_HEIGHT,
                Configuration.FRAME_WIDTH_HEIGHT);

        List<PixelFrame> randomPixelFramesList = randomPixelFrameGenerator.generatePixelFramesList(Configuration.PATTERNS_COUNT);

        for (int i = 0; i < Configuration.AGES_COUNT; i++) {
            for (PixelFrame pixelFrame : randomPixelFramesList) {
                kohonenNetwork.processPixelFrame(pixelFrame);
            }
        }

        kohonenNetwork.deleteDeadNeurons();

        ImageConverter imageConverter = new ImageConverter(originalImage, Configuration.FRAME_WIDTH_HEIGHT, Configuration.IMAGE_WIDTH_HEIGHT);
        PixelFrame[][] pixelFrames = imageConverter.convertImageToPixelFrames();

        KohonenNetworkCompressor kohonenNetworkCompressor = new KohonenNetworkCompressor(kohonenNetwork);
        CompressedFrame[][] compressedFrames = kohonenNetworkCompressor.compress(pixelFrames);

        KohonenNetworkDecompressor kohonenNetworkDecompressor = new KohonenNetworkDecompressor(kohonenNetwork, compressedFrames, Configuration.FRAME_WIDTH_HEIGHT);
        int[][] decompressedImage = kohonenNetworkDecompressor.decompressImage();

        imageLoader.saveImageToFile(decompressedImage, "decompressedImage.jpg", Configuration.IMAGE_WIDTH_HEIGHT);
        imageLoader.saveImageToFile(originalImage, "originalImage.jpg", Configuration.IMAGE_WIDTH_HEIGHT);

        KohonenNetworkEvaluator kohonenNetworkEvaluator = new KohonenNetworkEvaluator();

        double compressionRate = kohonenNetworkEvaluator.calculateCompressionRate(
                Configuration.IMAGE_WIDTH_HEIGHT,
                Configuration.FRAME_WIDTH_HEIGHT,
                compressedFrames,
                Configuration.BITS_PER_WEIGHT,
                Configuration.NEURONS_NUMBER
        );
        System.out.println("Compression rate = " + compressionRate);

        double psnr = kohonenNetworkEvaluator.calculatePSNR(originalImage, decompressedImage);
        System.out.println("PSNR = " + psnr);

    }
}
