import config.Configuration;
import image.CompressedFrame;
import image.ImageLoader;
import image.PixelFrame;
import kohonen.KohonenNetwork;
import utils.Utils;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String... args) throws IOException{
        ImageLoader imageLoader = new ImageLoader();
        int[][] image = imageLoader.loadImage(Configuration.FILE_NAME);

        KohonenNetwork kohonenNetwork = new KohonenNetwork(
                Configuration.NEURONS_NUMBER,
                (int)Math.pow(Configuration.FRAME_WIDTH_HEIGHT, 2),
                Configuration.TRAINING_STEP,
                Configuration.MINIMAL_WINNER_COUNTER
        );

        List<PixelFrame> pixelFrames = Utils.generatePatternsList(
                image,
                Configuration.PATTERNS_COUNT,
                Configuration.FRAME_WIDTH_HEIGHT
        );

        for(int i = 0; i < Configuration.AGES_COUNT; i++) {
            for(int j = 0; j < pixelFrames.size(); j++) {
                kohonenNetwork.processPixelFrame(pixelFrames.get(j));
            }
        }

        kohonenNetwork.deleteDeadNeurons();

        CompressedFrame[][] compressedFrames = kohonenNetwork.compressImage(image, Configuration.FRAME_WIDTH_HEIGHT);
        int[][] decompressedImage = kohonenNetwork.decompressImage(compressedFrames, Configuration.FRAME_WIDTH_HEIGHT);

        imageLoader.saveImageToFile(decompressedImage, "decompressedImage.jpg", Configuration.IMAGE_WIDTH_HEIGHT);
        imageLoader.saveImageToFile(image, "originalImage.jpg", Configuration.IMAGE_WIDTH_HEIGHT);

        double compressionRate = kohonenNetwork.calculateCompressionRate(
                Configuration.IMAGE_WIDTH_HEIGHT,
                Configuration.FRAME_WIDTH_HEIGHT,
                compressedFrames,
                Configuration.BITS_PER_WEIGHT
        );
        System.out.println("Compression rate = " + compressionRate);

        double psnr = kohonenNetwork.calculatePSNR(image, decompressedImage);
        System.out.println("PSNR = " + psnr);

    }
}
