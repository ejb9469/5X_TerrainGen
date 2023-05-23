import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

    public static double[][] tileNoise;

    public static void generate() { // TODO: generate() should return int - unique seed
        int DIM_X = TerrainGen.DIM_X;
        int DIM_Y = TerrainGen.DIM_Y;

        Gradient continentGradient = new ContinentGradient(DIM_X, DIM_Y);

        ChanceGradient moistureGradientPerc = new MoistureGradient(DIM_X, DIM_Y);
        ChanceGradient heatGradientPerc = new HeatGradient(DIM_X, DIM_Y);

        double[][] moistureGradient = continentGradient.generateGradient(-3, 4, 350, 350, -50, 50);
        double[][] moistureNoise = TerrainGen.brownianNoise(9, .7f, .005f, 0, moistureGradient);
        TerrainGen.moistureNoise = moistureNoise;
        List<double[][]> moistureBiomes = new ArrayList<>();
        moistureBiomes.add(moistureGradientPerc.generateGradient(-50, 100, TerrainGen.moistureRainf, moistureNoise));
        moistureBiomes.add(moistureGradientPerc.generateGradient(-50, 100, TerrainGen.moistureFores, moistureNoise));
        moistureBiomes.add(moistureGradientPerc.generateGradient(-50, 100, TerrainGen.moistureFlatl, moistureNoise));
        moistureBiomes.add(moistureGradientPerc.generateGradient(-50, 100, TerrainGen.moistureBadla, moistureNoise));
        SimpleSimplex.randomSeed();
        continentGradient.randomSeed();

        double[][] heatGradient = new EquatorGradient(DIM_X, DIM_Y).generateGradient(-2, 4, -10, 10, -2, 2); // TODO: Remove equator?
        double[][] heatNoise = TerrainGen.brownianNoise(7, .6f, .01f, 0, heatGradient);
        TerrainGen.heatNoise = heatNoise;
        List<double[][]> heatBiomes = new ArrayList<>();
        heatBiomes.add(heatGradientPerc.generateGradient(-50, 100, TerrainGen.heatRainf, heatNoise));
        heatBiomes.add(heatGradientPerc.generateGradient(-50, 100, TerrainGen.heatFores, heatNoise));
        heatBiomes.add(heatGradientPerc.generateGradient(-50, 100, TerrainGen.heatFlatl, heatNoise));
        heatBiomes.add(heatGradientPerc.generateGradient(-50, 100, TerrainGen.heatBadla, heatNoise));
        SimpleSimplex.randomSeed();
        continentGradient.randomSeed();

        double[][] islandGradient = continentGradient.generateGradient(-1.585, 1.5, -100, 100, -200, 200, .9d);
        SimpleSimplex.randomSeed();

        double[][] tileNoise = TerrainGen.brownianNoise(8, .6f, .005f, 0, islandGradient, 2, -3.5, 3.5, 2000);
        TerrainGen.tileNoise = tileNoise;
        SimpleSimplex.randomSeed();
        continentGradient.randomSeed();

        // TODO: Terrain features - mountains, rivers, etc.

        // instantiateBiomes();
        Random random = new Random(new Random().nextInt(Integer.MAX_VALUE));

        double[][] moistureBiome = biomeChance(moistureBiomes);
        double[][] heatBiome = biomeChance(heatBiomes);
        double[] subsectionsM = TerrainGen.interpreter.moisture;
        double[][] subsectionsH = TerrainGen.interpreter.moistureHeat;

        boolean floor = random.nextBoolean();
        int[][] biomePheno = new int[DIM_X][DIM_Y];
        for (int i = 0; i < biomePheno.length; i++) {
            for (int j = 0; j < biomePheno[i].length; j++) {
                int average;
                if (floor)
                    average = (int)((moistureBiome[i][j]+heatBiome[i][j])/2);
                else
                    average = (int)(Math.ceil((moistureBiome[i][j]+heatBiome[i][j])/2));
                int indexM = 0;
                int indexH = 0;
                for (int a = 0; a < subsectionsM.length; a++) {
                    if (a == subsectionsM.length - 1)
                        break;
                    if (moistureNoise[i][j] < subsectionsM[a])
                        indexM = a;
                }
                for (int a = 0; a < subsectionsH[indexM].length; a++) {
                    if (a == subsectionsH[0].length - 1) {
                        indexH = subsectionsH[0].length - 2;
                        break;
                    }
                    if (heatNoise[i][j] < subsectionsH[indexM][a]) {
                        indexH = a;
                    }
                }
                biomePheno[i][j] = TerrainGen.BIOME_MH_MAP[average][indexH][indexM];
            }
        }
        TerrainGen.biomePheno = biomePheno;

        // detectLandmasses();
        TerrainGen.detectLandmasses(false);

        // From here - Graphics module

    }

    public static double[][] biomeChance(List<double[][]> biomeSpecs) {
        int DIM_X = TerrainGen.DIM_X;
        int DIM_Y = TerrainGen.DIM_Y;
        Random random = new Random(new Random().nextInt(Integer.MAX_VALUE));
        int randomPercent = random.nextInt(100)+1;
        double[][] biomesSect = new double[DIM_X][DIM_Y];
        for (int x = 0; x < biomeSpecs.size(); x++) { // Runs four times
            for (int a = 0; a < biomeSpecs.get(x).length; a++) {
                for (int b = 0; b < biomeSpecs.get(x)[a].length; b++) {
                    double actualPercent = biomeSpecs.get(x)[a][b];
                    int group = 0;
                    if (randomPercent <= actualPercent) {
                        group = x;
                    } else {
                        double cumulativePercent = actualPercent;
                        for (int y = 0; y < biomeSpecs.size(); y++) { // runs four times
                            if (x == y) continue;
                            double actualPercent2 = biomeSpecs.get(y)[a][b];
                            if (randomPercent <= actualPercent2 + cumulativePercent) {
                                group = y;
                                break;
                            } // else continue
                            cumulativePercent += actualPercent2;
                        }
                    }
                    biomesSect[a][b] = group;
                }
            }
        }
        return biomesSect;
    }

}