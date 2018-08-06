package image;

import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public int[][] loadImage(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] imgArr = new int[width][height];
        Raster raster = img.getData();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imgArr[x][y] = raster.getSample(y, x, 0);
            }
        }
        return imgArr;
    }

    public void saveImageToFile(int[][] data, String fileName, int imageWidthHeight) throws IOException {
        BufferedImage outputImage = new BufferedImage(imageWidthHeight, imageWidthHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = outputImage.getRaster();
        int[] array1D = Utils.convert2darrayto1d(data);
        raster.setSamples(0, 0, imageWidthHeight, imageWidthHeight, 0, array1D);

        ImageIO.write(outputImage, "jpg", new File(fileName));
    }
}
