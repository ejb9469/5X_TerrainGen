import java.util.Random;

public class Interpreter {

    private Random random = new Random();
    private long seed;

    public int moistureSalt = 200;
    public int heatSalt = 400;

    public double mountainThreshold = 1.05; // -2 to 2

    public int lakeRadius = 18;

    public double[] moisture = new double[5];
    public double[][] moistureHeat = new double[5][5];

    public static final double ocean = -.6;
    public static final double water = -.4;
    public static final double beach = -.3;
    public static final double mountains = 1.05;

    public Interpreter(long seed) { // TODO: FIX THIS
        setSeed(seed);
    }

    public Interpreter() {
        randomlySeed();
        /*moisture[0] = 0.5 + (random.nextInt(moistureSalt*2+1)-moistureSalt)/1000;
        moisture[1] = 0.0 + (random.nextInt(moistureSalt*2+1)-moistureSalt)/1000;
        moisture[2] = -0.5 + (random.nextInt(moistureSalt*2+1)-moistureSalt)/1000;*/
        moisture[0] = 2;
        moisture[1] = 1;
        moisture[2] = 0;
        moisture[3] = -1;
        moisture[4] = -2;

        moistureHeat[0][0] = 2 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[0][1] = 1 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[0][2] = 0 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[0][3] = -1;
        moistureHeat[0][4] = -2;

        moistureHeat[1][0] = 2 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[1][1] = 1 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[1][2] = 0 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[1][3] = -1;
        moistureHeat[1][4] = -2;

        moistureHeat[2][0] = 2 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[2][1] = 1 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[2][2] = 0 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[2][3] = -1;
        moistureHeat[2][4] = -2;

        moistureHeat[3][0] = 2 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[3][1] = 1 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[3][2] = 0 + (random.nextInt(heatSalt*2+1)-heatSalt)/1000;
        moistureHeat[3][3] = -1;
        moistureHeat[3][4] = -2;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public void randomlySeed() {
        setSeed(new Random().nextInt(Integer.MAX_VALUE));
    }

    public long getSeed() {
        return seed;
    }

}