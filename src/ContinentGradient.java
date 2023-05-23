import java.util.Random;

public class ContinentGradient extends Gradient {

    public ContinentGradient(int DIM_X, int DIM_Y) {
        super(DIM_X, DIM_Y);
    }

    public double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange) {
        double[][] gradient = new double[DIM_X][DIM_Y];
        Random random = new Random(seed);
        double salt = (overallSaltLowerRange+1)/1000d-random.nextInt(overallSaltUpperRange+1)/1000d;
        double gradientStep = 2*(max-min)/DIM_X;
        for (int i = 0; i < Math.ceil(gradient.length / 2); i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                double gradientOut = min + salt;
                if (i > j)
                    gradientOut += gradientStep * j;
                else
                    gradientOut += gradientStep * i;
                gradient[i][j] = gradientOut;
            }
            for (int j = gradient[i].length-1; j >= 0; j--) {
                gradient[i][j] = gradient[i][gradient[i].length-1-j];
            }
            gradient[gradient.length-1-i] = gradient[i];
        }
        this.gradient = gradient;
        return gradient;
    }

    public double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, double exp) {
        double[][] gradient = new double[DIM_X][DIM_Y];
        Random random = new Random(seed);
        double salt = (overallSaltLowerRange+1)/1000d-random.nextInt(overallSaltUpperRange+1)/1000d;
        double gradientStep = 2*(max-min)/DIM_X;
        for (int i = 0; i < Math.ceil(gradient.length / 2); i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                double gradientOut = min + salt;
                if (i > j) {
                    double newGrad = Math.pow(Math.abs(1 + gradientStep * j), exp) - 1;
                    if (gradientOut + newGrad > max)
                        gradientOut = max;
                    else
                        gradientOut += newGrad;
                } else {
                    double newGrad = Math.pow(Math.abs(1 + gradientStep * i), exp) - 1;
                    if (gradientOut + newGrad > max)
                        gradientOut = max;
                    else
                        gradientOut += newGrad;
                }
                gradient[i][j] = gradientOut;
            }
            for (int j = gradient[i].length-1; j >= 0; j--) {
                gradient[i][j] = gradient[i][gradient[i].length-1-j];
            }
            gradient[gradient.length-1-i] = gradient[i];
        }
        this.gradient = gradient;
        return gradient;
    }

}