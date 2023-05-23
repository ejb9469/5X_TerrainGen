public class MoistureGradient extends Gradient implements ChanceGradient {

    public MoistureGradient(int DIM_X, int DIM_Y) {
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

        /*double[][] waterModifier = aroundWater(min, max, 5);
        for (int i = 0; i < waterModifier.length; i++) {
            for (int j = 0; j < waterModifier[i].length; j++) {
                newNoise[i][j] *= 1+(waterModifier[i][j]/100d);
            }
        }*/

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

        System.out.println("SMALLEST PERCENT MOISTURE: " + smallestP);
        System.out.println("LARGEST PERCENT MOISTURE: " + largestP);
        /*for (int i = 0; i < percentGradient.length; i++) {
            for (int j = 0; j < percentGradient[i].length; j++) {
                System.out.print(percentGradient[i][j] + " ");
            }
            System.out.println();
        }*/
        return percentGradient;
        /*
        double[][] waterGradient = aroundWater(min, max, 5);
        for (int i = 0; i < percentGradient.length; i++) {
            for (int j = 0; j < percentGradient[i].length; j++) {
                percentGradient[i][j] *= (waterGradient[i][j]/100d);
            }
        }
        return percentGradient;*/
    }

    /*private double[][] aroundWater(double min, double max, int radius) {
        double[][] gradient = new double[DIM_X][DIM_Y];
        //Random random = new Random(seed);
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                gradient[i][j] = min;
            }
        }
        for (int i = 0; i < TerrainGen.lakeNoise.length; i++) {
            for (int j = 0; j < TerrainGen.lakeNoise[i].length; j++) {
                if (TerrainGen.lakeNoise[i][j] >= TerrainGen.lake || TerrainGen.tileNoise[i][j] < TerrainGen.water) {
                    double gradientStepModifier = .3;
                    if (TerrainGen.lakeNoise[i][j] >= TerrainGen.lake)
                        gradientStepModifier = 1;
                    double gradientStep = (max-min)*gradientStepModifier/((double)radius+1);
                    for (int a = -radius; a <= radius; a++) {
                        if (i + a < 0 || i + a >= gradient.length)
                            continue;
                        for (int b = -radius; b <= radius; b++) {
                            double gradientOut = min;
                            if (j + b < 0 || j + b >= gradient[i+a].length)
                                continue;
                            if (a >= b)
                                gradientOut += gradientStep * (radius - Math.abs(a) + 1);
                            else
                                gradientOut += gradientStep * (radius - Math.abs(b) + 1);
                            if (gradient[i + a][j + b] == 0)
                                gradient[i + a][j + b] = gradientOut;
                            else
                                gradient[i + a][j + b] = (gradient[i + a][j + b] + gradientOut)/2;
                        }
                    }
                }
            }
        }
        double s = min;
        double b = min;
        for (int i = 0; i < gradient.length; i++) {
            for (int j = 0; j < gradient[i].length; j++) {
                if (gradient[i][j] > b)
                    b = gradient[i][j];
                else if (gradient[i][j] < s)
                    s = gradient[i][j];
                //System.out.print(gradient[i][j] + " ");
            }
            //System.out.println();
        }
        System.out.println("SMALLEST LAKAGE: " + s);
        System.out.println("LARGEST LAKAGE: " + b);
        return gradient;
    }*/

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