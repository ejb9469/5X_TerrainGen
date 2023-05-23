import java.util.List;

public class BorderMaker {

    public static final int MIN_AREA = 900;
    public static final int MAX_AREA = 1000;
    public static final double ROUND_MOD = 1.25;

    public void /*int[][]*/ drawBorders() {
        // Takes in:
            // Biomes
            // Heat & Moisture
            // Landmasses
            // Mountains
        int[][] biomes = TerrainGen.biomePheno;
        double[][] heat = TerrainGen.heatNoise;
        double[][] moisture = TerrainGen.moistureNoise;
        List<List<int[]>> landmasses = TerrainGen.landmasses;

        // Draw borders so that:
            // 1. No land mass is smaller than MIN_AREA or larger than MAX_AREA
            // 2. Borders cannot go across
            // 3. Rules 1-2 can be broken if circumstances force it - like if there are less than MIN_AREA tiles or there are less than MAX_AREA*ROUND_MOD and more than MAX_AREA tiles

    }

}