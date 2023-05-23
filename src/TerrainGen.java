import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TerrainGen {

    // PURE CONSTANTS

    public static final int TILE_SIZE = 4;
    public static final int DIM_X = GraphicsFX.WINDOW_DIM_X/TILE_SIZE;
    public static final int DIM_Y = GraphicsFX.WINDOW_DIM_Y/TILE_SIZE;

    public static final int ABSURD_INT = 99999999;

    public static final int TINY_LANDMASS = 100;

    //

    // BIOME CONSTANTS

    public static final double[] moistureRainf = {1, 2.5, 5, 10, 70};
    public static final double[] moistureFores = {5, 15, 35, 50, 20};
    public static final double[] moistureFlatl = {14, 25, 40, 35, 9};
    public static final double[] moistureBadla = {80, 57.5, 20, 5, 1};

    public static final double[] heatRainf = {1, 2.5, 5, 10, 40};
    public static final double[] heatFores = {45, 35, 30, 25, 5};
    public static final double[] heatFlatl = {43, 43, 43, 25, 0};
    public static final double[] heatBadla = {1, 19.5, 22, 40, 45};

    public static final int[][][] BIOME_MH_MAP = {
            { // RAINFORESTED
                    {1, 1, 1, 2},
                    {3, 3, 3, 2},
                    {4, 4, 4, 5},
                    {4, 4, 4, 6}
            },

            { // FORESTED
                    {1, 1, 1, 2},
                    {3, 3, 3, 2},
                    {7, 7, 7, 8},
                    {4, 7, 8, 6}
            },

            { // FLATLANDS
                    {1, 1, 1, 2},
                    {7, 9, 10, 10},
                    {7, 9, 9, 8},
                    {7, 9, 8, 5}
            },

            { // BADLANDS
                    {3, 3, 3, 2},
                    {10, 10, 10, 5},
                    {5, 5, 6, 6},
                    {6, 6, 6, 6}
            }
    };

    // FIELDS

    public static Interpreter interpreter = new Interpreter();
    public static double[][] tileNoise = new double[DIM_X][DIM_Y];
    public static double[][] moistureNoise = new double[DIM_X][DIM_Y];
    public static double[][] heatNoise = new double[DIM_X][DIM_Y];
    public static int[][] biomePheno = new int[DIM_X][DIM_Y];
    public static List<List<int[]>> landmasses = new ArrayList<>();

    // BROWNIAN NOISE FUNCTIONS

    public static double[][] brownianNoise(int octaves, float roughness, float scale, int borderWidth, double superMin, double rate, int indvSalt) {
        double[][] heightMap = simplexNoise(octaves, roughness, scale, borderWidth, superMin, rate, indvSalt);
        printLowHigh(heightMap);
        return heightMap;
    }

    public static double[][] brownianNoise(int octaves, float roughness, float scale, double noiseConstant, double[][] gradient, int borderWidth, double superMin, double rate, int indvSalt) {
        double[][] heightMap = simplexNoise(octaves, roughness, scale, borderWidth, superMin, rate, indvSalt);
        printLowHigh(heightMap);
        heightMap = applyGradient(heightMap, gradient, noiseConstant);
        return heightMap;
    }

    public static double[][] brownianNoise(int octaves, float roughness, float scale, double noiseConstant, double[][] gradient) {
        double[][] heightMap = simplexNoise(octaves, roughness, scale);
        printLowHigh(heightMap);
        heightMap = applyGradient(heightMap, gradient, noiseConstant);
        return heightMap;
    }

    public static double[][] brownianNoise(int octaves, float roughness, float scale) {
        double[][] heightMap = simplexNoise(octaves, roughness, scale);
        printLowHigh(heightMap);
        return heightMap;
    }

    // SIMPLEX NOISE FUNCTIONS

    private static double[][] simplexNoise(int octaves, float roughness, float scale, int borderWidth, double superMin, double rate, int indvSalt) {
        SimplexNoise sng = new SimplexNoise();
        Random rand = new Random(new Random().nextInt(Integer.MAX_VALUE));
        double[][] noise = sng.generateOctavedSimplexNoise(DIM_X - borderWidth * 2, DIM_Y - borderWidth * 2, octaves, roughness, scale);
        double[][] newNoise = new double[DIM_X][DIM_Y];
        int softBorderWidth = Math.abs((int)(superMin/rate));
        for (int i = 0; i < newNoise.length; i++) {
            for (int j = 0; j < newNoise[i].length; j++) {
                double salt = (rand.nextInt(indvSalt*2+1)-indvSalt)/1000;
                if (i < borderWidth || j < borderWidth || i > newNoise.length - borderWidth - 1 || j > newNoise[i].length - borderWidth - 1) {
                    newNoise[i][j] = ABSURD_INT*-1; // HARD BORDER
                    //continue;
                } else if (i < borderWidth + softBorderWidth || j < borderWidth + softBorderWidth) {
                    double gradientOut;
                    if (i > j)
                        gradientOut = superMin + (rate * (j - borderWidth)) + salt;
                    else
                        gradientOut = superMin + (rate * (i - borderWidth)) + salt;
                    newNoise[i][j] = gradientOut;
                } else if (i > newNoise.length - borderWidth - softBorderWidth - 1 || j > newNoise[i].length - borderWidth - softBorderWidth - 1){
                    double gradientOut;
                    if (i > j)
                        gradientOut = -(rate * (j - borderWidth)) + salt;
                    else
                        gradientOut = -(rate * (i - borderWidth)) + salt;
                    newNoise[i][j] = gradientOut;
                } else {
                    newNoise[i][j] = noise[i - borderWidth][j - borderWidth];
                }
            }
        }
        return newNoise;
    }

    private static double[][] simplexNoise(int octaves, float roughness, float scale) {
        SimplexNoise sng = new SimplexNoise();
        return sng.generateOctavedSimplexNoise(DIM_X, DIM_Y, octaves, roughness, scale);
    }

    // GRADIENT FUNCTION

    private static double[][] applyGradient(double[][] to, double[][] gradient, double noiseConstant) {
        for (int i = 0; i < to.length; i++) {
            for (int j = 0; j < to[i].length; j++) {
                to[i][j] += gradient[i][j] + noiseConstant;
            }
        }
        return to;
    }

    // LANDMASS DETECTION

    public static void detectLandmasses(boolean strict) {
        List<int[]> commonTotal = new ArrayList<>();
        for (int i = 0; i < tileNoise.length; i++) {
            for (int j = 0; j < tileNoise[i].length; j++) {
                if (tileNoise[i][j] <= Interpreter.water)
                    continue;
                int[] tile = {i,j};
                boolean invalid = false;
                for (int[] commonTile : commonTotal) {
                    if (commonTile[0] == i && commonTile[1] == j) {
                        invalid = true;
                        break;
                    }
                }
                if (invalid)
                    continue;
                List<List<int[]>> nested = new ArrayList<>();
                List<int[]> common = new ArrayList<>();
                common.add(tile);
                List<int[]> nestedZero = new ArrayList<>();
                nestedZero.add(tile);
                nested.add(nestedZero);
                for (int a = 0; a < nested.size(); a++) {
                    List<int[]> branch = new ArrayList<>();
                    for (int b = 0; b < nested.get(a).size(); b++) {
                        int[] nextTile = nested.get(a).get(b);
                        boolean adjacentToWater = false;
                        waterAdj:
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (m == 0 && n == 0) continue;
                                if (tileNoise[nextTile[0] + m][nextTile[1] + n] <= Interpreter.water) {
                                    adjacentToWater = true;
                                    break waterAdj;
                                }
                            }
                        }
                        int radius = 1;
                        if (adjacentToWater && !strict)
                            radius = 8;
                        mLabel:
                        for (int m = -radius; m <= radius; m++) {
                            for (int n = -radius; n <= radius; n++) {
                                if (m == 0 && n == 0)
                                    continue;
                                if (m != 0) {
                                    if (nextTile[0] + m < 0 || nextTile[0] + m >= DIM_X)
                                        continue mLabel;
                                }
                                if (n != 0) {
                                    if (nextTile[1] + n < 0 || nextTile[1] + n > DIM_Y)
                                        continue;
                                }
                                int[] adjTile = {nextTile[0] + m, nextTile[1] + n};
                                if (nextTile[0] + m >= tileNoise.length)
                                    continue;
                                if (nextTile[1] + n >= tileNoise[nextTile[0] + m].length)
                                    continue;
                                if (tileNoise[nextTile[0] + m][nextTile[1] + n] <= Interpreter.water)
                                    continue;
                                boolean valid = true;
                                for (int[] commonTile : common) {
                                    if (commonTile[0] == adjTile[0] && commonTile[1] == adjTile[1]) {
                                        valid = false;
                                        break;
                                    }
                                }
                                if (valid) {
                                    branch.add(adjTile);
                                    common.add(adjTile);
                                }
                            }
                        }
                    }
                    if (branch.size() > 0)
                        nested.add(branch);
                }
                List<int[]> land = new ArrayList<>();
                for (List<int[]> branch : nested) {
                    land.addAll(branch);
                }
                landmasses.add(land);
                commonTotal.addAll(common);
            }
        }
        for (int i = 0; i < landmasses.size(); i++) {
            List<int[]> landmass = landmasses.get(i);
            if (landmass.size() <= TINY_LANDMASS) {
                int jAdjusted;
                if (i - 1 < 0) {
                    jAdjusted = 1;
                } else if (i + 1 >= landmasses.size()) {
                    jAdjusted = -1;
                } else {
                    if (Math.random() < .5)
                        jAdjusted = -1;
                    else
                        jAdjusted = 1;
                }
                List<int[]> landmass2 = landmasses.get(i + jAdjusted);
                if (landmass2.size() > TINY_LANDMASS) {
                    landmass2.addAll(landmass);
                }
            }
        }
    }

    // DEV FUNCTIONS

    private static void printLowHigh(double[][] heightMap) {
        double low = 0;
        double high = 0;
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                if (heightMap[i][j] > high)
                    high = heightMap[i][j];
                else if (heightMap[i][j] < low)
                    low = heightMap[i][j];
                //System.out.print(heightMap[i][j] + " ");
            }
            //System.out.println();
        }
        System.out.println("LOW: " + low);
        System.out.println("HIGH: " + high);
    }

}