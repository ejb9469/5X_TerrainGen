import java.util.Random;

public class EquatorGradient extends Gradient {

    public EquatorGradient(int DIM_X, int DIM_Y) {
        super(DIM_X, DIM_Y);
    }

    public double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, double exp) {
        double[][] gradient = new double[DIM_X][DIM_Y];
        Random random = new Random(seed);
        double salt = (overallSaltLowerRange)/1000d-random.nextInt(overallSaltUpperRange+1)/1000d;
        double gradientStep = 2*(max-min)/DIM_Y;
        for (int i = 0; i < Math.ceil(gradient.length / 2); i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                double gradientOut = min + salt;
                double miniSalt = (tileSaltLowerRange)/1000d + random.nextInt(overallSaltUpperRange+1)/1000d;
                gradientOut += gradientStep * j + miniSalt;
                gradient[i][j] = gradientOut;
            }
            for (int j = gradient[i].length-1; j >= 0; j--) {
                gradient[i][j] = gradient[i][gradient[i].length-1-j];
            }
            gradient[gradient.length-1-i] = gradient[i];
        }
        /*for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                System.out.print(gradient[i][j] + " ");
            }
            System.out.println();
        }*/
        this.gradient = gradient;
        return gradient;
    }

    public double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange) {
        double[][] gradient = new double[DIM_X][DIM_Y];
        Random random = new Random(seed);
        double salt = (overallSaltLowerRange)/1000d-random.nextInt(overallSaltUpperRange+1)/1000d;
        double gradientStep = 2*(max-min)/DIM_Y;
        for (int i = 0; i < Math.ceil(gradient.length / 2); i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                double gradientOut = min + salt;
                double miniSalt = (tileSaltLowerRange)/1000d + random.nextInt(overallSaltUpperRange+1)/1000d;
                gradientOut += gradientStep * j + miniSalt;
                gradient[i][j] = gradientOut;
            }
            for (int j = gradient[i].length-1; j >= 0; j--) {
                gradient[i][j] = gradient[i][gradient[i].length-1-j];
            }
            gradient[gradient.length-1-i] = gradient[i];
        }
        /*for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                System.out.print(gradient[i][j] + " ");
            }
            System.out.println();
        }*/
        this.gradient = gradient;
        return gradient;
    }

}