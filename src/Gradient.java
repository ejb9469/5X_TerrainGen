import java.util.Random;

public abstract class Gradient {

    protected double[][] gradient;
    protected long seed;

    protected int DIM_X;
    protected int DIM_Y;

    public Gradient(int DIM_X, int DIM_Y) {
        this.DIM_X = DIM_X;
        this.DIM_Y = DIM_Y;
        randomSeed();
    }

    public abstract double[][] generateGradient(double min, double max, int saltLowerRange, int saltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange);
    public abstract double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, double exp);

    public static double[][] averageGradients(double[][] grad1, double[][] grad2) {
        double[][] grad3 = new double[grad1.length][grad1[0].length];
        for (int i = 0; i < grad1.length; i++) {
            for (int j = 0; j < grad1[i].length; j++) {
                grad3[i][j] = (int)(grad1[i][j]+grad2[i][j]);
            }
        }
        return grad3;
    }

    public double[][] getGradient() {
        return gradient;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        //System.out.println("Seed: " + seed);
        Random r = new Random(this.seed);
    }

    public void randomSeed() {
        setSeed(new Random().nextInt(Integer.MAX_VALUE));
    }

    public long getSeed() {
        return seed;
    }

}