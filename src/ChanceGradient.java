public interface ChanceGradient {

    double[][] generateGradient(double min, double max, double[] subsections, double[][] noise);
    //double[][] generateGradient(double min, double max, int saltLowerRange, int saltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, int[] subsections, double exp);

    static double[][] percentify(double[][] noise, double[][] gradient) {
        double[][] newNoise = new double[noise.length][noise[0].length];
        for (int i = 0; i < noise.length; i++) {
            for (int j = 0; j < noise[i].length; j++) {
                newNoise[i][j] = noise[i][j] * (gradient[i][j]/100d);
            }
        }
        return newNoise;
    }

}