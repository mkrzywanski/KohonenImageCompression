package image;

public class CompressedFrame {
    private int winningNeuronIndex;
    private double brightness;

    public CompressedFrame(int winningNeuronIndex, double brightness) {
        this.winningNeuronIndex = winningNeuronIndex;
        this.brightness = brightness;
    }

    public int getWinningNeuronIndex() {
        return winningNeuronIndex;
    }

    public void setWinningNeuronIndex(int winningNeuronIndex) {
        this.winningNeuronIndex = winningNeuronIndex;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}
