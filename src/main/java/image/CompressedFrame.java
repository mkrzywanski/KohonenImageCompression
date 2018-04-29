package image;

public class CompressedFrame {
    private int neuronNumber;
    private double brightness;

    public CompressedFrame(int neuronNumber, double brightness) {
        this.neuronNumber = neuronNumber;
        this.brightness = brightness;
    }

    public int getNeuronNumber() {
        return neuronNumber;
    }

    public void setNeuronNumber(int neuronNumber) {
        this.neuronNumber = neuronNumber;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}
