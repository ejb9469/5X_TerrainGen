public class HeatGradient extends Gradient implements ChanceGradient {

    public HeatGradient(int DIM_X, int DIM_Y) {
        super(DIM_X, DIM_Y);
    }

    public double[][] generateGradient(double min, double max, double[] subsections, double[][] noise) {
        double[][] percentGradient = new double[DIM_X][DIM_Y];
        double[] subsectionsX = TerrainGen.interpreter.moisture;

        double[][] newNoise = new double[DIM_X][DIM_Y];
        for (int i = 0; i < newNoise.length; i++) {
            for (int j = 0; j < newNoise[i].length; j++) {
                newNoise[i][j] = noise[i][j];
            }
        }

        double[][] equatorModifier = new EquatorGradient(DIM_X, DIM_Y).generateGradient(min, max, 0, 0, 0, 0);
        for (int i = 0; i < equatorModifier.length; i++) {
            for (int j = 0; j < equatorModifier[i].length; j++) {
                newNoise[i][j] *= 1+(equatorModifier[i][j]/100d);
            }
        }

        double[] slopes = new double[subsections.length];
        for (int i = 0; i < slopes.length - 1; i++) {
            // SLOPE = (y2-y1)/(x2-x1)
            slopes[i] = subsections[i+1] - subsections[i];
        }
        //slopes[slopes.length - 1] = slopes[slopes.length - 2];

        double smallestP = 0;
        double largestP = 0;
        for (int i = 0; i < newNoise.length; i++) {
            for (int j = 0; j < newNoise[i].length; j++) {
                int index = 0;
                for (int k = 0; k < subsectionsX.length; k++) {
                    if (k == subsectionsX.length - 1)
                        break;
                    if (newNoise[i][j] < subsectionsX[k])
                        index = k;
                }
                // Y = MX+B
                if (index > 0)
                    percentGradient[i][j] = slopes[index] + subsections[index];
                else
                    percentGradient[i][j] = subsections[index];
                if (percentGradient[i][j] > largestP)
                    largestP = percentGradient[i][j];
                else if (percentGradient[i][j] < smallestP)
                    smallestP = percentGradient[i][j];
            }
        }

        System.out.println("SMALLEST PERCENT HEAT: " + smallestP);
        System.out.println("LARGEST PERCENT HEAT: " + largestP);
        /*for (int i = 0; i < percentGradient.length; i++) {
            for (int j = 0; j < percentGradient[i].length; j++) {
                System.out.print(percentGradient[i][j] + " ");
            }
            System.out.println();
        }*/
        return percentGradient;
    }

    public double[][] generateGradient(double min, double max, int saltLowerRange, int saltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange) {
        return new double[1][1];
    }
    public double[][] generateGradient(double min, double max, int overallSaltLowerRange, int overallSaltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, double exp) {
        return new double[1][1];
    }

    /*public double[][] generateGradient(double min, double max, int saltLowerRange, int saltUpperRange, int tileSaltLowerRange, int tileSaltUpperRange, int[] subsections, double exp) {
        // TODO: PLACEHOLDER
        return generateGradient(min, max, saltLowerRange, saltUpperRange, tileSaltLowerRange, tileSaltUpperRange, subsections);
    }*/

}