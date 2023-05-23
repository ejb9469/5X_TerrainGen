public class SimplexNoise {

    public double[][] generateOctavedSimplexNoise(int width, int height, int octaves, float roughness, float scale) {
        double[][] totalNoise = new double[width][height];
        float layerFrequency = scale;
        float layerWeight = 1;
        float weightSum = 0;

        for (int octave = 0; octave < octaves; octave++) {
            //Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    totalNoise[x][y] += (float) SimpleSimplex.noise(x * layerFrequency, y * layerFrequency) * layerWeight;
                }
            }

            //Increase variables with each incrementing octave
            layerFrequency *= 2;
            weightSum += layerWeight;
            layerWeight *= roughness;

        }
        return totalNoise;
    }

}