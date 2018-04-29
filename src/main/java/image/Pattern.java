package image;

public class Pattern {
    private double[] pixels;
    private double brightness;

    public Pattern(double[] pixels) {
        this.pixels = pixels;
        this.brightness = 0;
    }

    public Pattern(double[] pixels, double brightness){
        this.pixels = pixels;
        this.brightness = brightness;
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
