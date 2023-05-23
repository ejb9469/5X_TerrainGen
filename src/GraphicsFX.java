import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphicsFX extends Application {

    /*
    1 - SNOW
    2 - TUNDRA
    3 - TAIGA
    4 - RAINFOREST
    5 - TEMPERATE DESERT
    6 - DESERT
    7 - FOREST
    8 - SAVANNA
    9 - GRASSLAND
    10 - SHRUBLAND
    */

    public static final int WINDOW_DIM_X = 1920;
    public static final int WINDOW_DIM_Y = 1080;

    private static final List<Color> BIOME_COLOR = new ArrayList<>();
    static {
        BIOME_COLOR.add(Color.WHITESMOKE);
        BIOME_COLOR.add(Color.rgb(208, 224, 227));
        BIOME_COLOR.add(Color.rgb(174, 234, 171));
        BIOME_COLOR.add(Color.rgb(0, 179, 0));
        BIOME_COLOR.add(Color.rgb(246, 178, 107));
        BIOME_COLOR.add(Color.rgb(255, 229, 153));
        BIOME_COLOR.add(Color.rgb(77, 139, 80));
        BIOME_COLOR.add(Color.rgb(211, 205, 112));
        BIOME_COLOR.add(Color.rgb(78, 196, 53));
        BIOME_COLOR.add(Color.rgb(100, 125, 100));
    }

    private static Interpreter interpreter = TerrainGen.interpreter;

    private static Random rand = new Random(new Random().nextInt(Integer.MAX_VALUE));

    public static void run(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test");
        primaryStage.setScene(map(WINDOW_DIM_X, WINDOW_DIM_Y, TerrainGen.TILE_SIZE, TerrainGen.tileNoise, TerrainGen.moistureNoise, TerrainGen.heatNoise));
        primaryStage.show();
    }

    private Scene map(int WINDOW_DIM_X, int WINDOW_DIM_Y, int TILE_SIZE, double[][] tileNoise, double[][] moistureNoise, double[][] heatNoise) {
        Group map = new Group();
        map.getChildren().add(buildMap(WINDOW_DIM_X/TILE_SIZE, WINDOW_DIM_Y/TILE_SIZE, TILE_SIZE, tileNoise, moistureNoise, heatNoise));
        return new Scene(map, WINDOW_DIM_X, WINDOW_DIM_Y);
    }

    public static Group buildMap(int DIM_X, int DIM_Y, int TILE_SIZE, double[][] tileNoise, double[][] moistureNoise, double[][] heatNoise) {
        Group out = new Group();
        double smallestVal = 0f;
        double largestVal = 0f;
        for (int i = 0; i < DIM_X; i++) {
            for (int j = 0; j < DIM_Y; j++) {
                if (tileNoise[i][j] < smallestVal)
                    smallestVal = tileNoise[i][j];
                else if (tileNoise[i][j] > largestVal)
                    largestVal = tileNoise[i][j];
                Rectangle tile = new Rectangle();
                tile.setX(i*TILE_SIZE-TILE_SIZE/2);
                tile.setY(j*TILE_SIZE-TILE_SIZE/2);
                tile.setWidth(TILE_SIZE);
                tile.setHeight(TILE_SIZE);
                if (tileNoise[i][j] < TerrainGen.interpreter.ocean) {
                    tile.setFill(Color.MEDIUMBLUE);
                } else if (tileNoise[i][j] < TerrainGen.interpreter.water) {
                    tile.setFill(Color.BLUE);
                } else if (tileNoise[i][j] < TerrainGen.interpreter.beach) {
                    tile.setFill(Color.rgb(194, 178, 128)); // Sand
                } else if (tileNoise[i][j] < 3) {
                    tile.setFill(Color.FORESTGREEN);
                }
                if (tileNoise[i][j] > TerrainGen.interpreter.beach) {
                    tile.setFill(BIOME_COLOR.get(TerrainGen.biomePheno[i][j]-1));
                }
                out.getChildren().add(tile);
            }
        }
        /*for (int i = 0; i < TerrainGen.mountainNoise.length; i++) {
            for (int j = 0; j < TerrainGen.mountainNoise[i].length; j++) {
                if (TerrainGen.mountainNoise[i][j] >= interpreter.mountainThreshold && tileNoise[i][j] > TerrainGen.beach) {
                    Rectangle tile = new Rectangle();
                    tile.setX(i * TILE_SIZE - TILE_SIZE / 2);
                    tile.setY(j * TILE_SIZE - TILE_SIZE / 2);
                    tile.setWidth(TILE_SIZE);
                    tile.setHeight(TILE_SIZE);
                    tile.setFill(Color.LIGHTGRAY);
                    out.getChildren().add(tile);
                }
            }
        }
        for (int i = 0; i < TerrainGen.lakeNoise.length; i++) {
            for (int j = 0; j < TerrainGen.lakeNoise[i].length; j++) {
                if (TerrainGen.lakeNoise[i][j] >= TerrainGen.lake && tileNoise[i][j] > TerrainGen.beach) {
                    Rectangle tile = new Rectangle();
                    tile.setX(i * TILE_SIZE - TILE_SIZE / 2);
                    tile.setY(j * TILE_SIZE - TILE_SIZE / 2);
                    tile.setWidth(TILE_SIZE);
                    tile.setHeight(TILE_SIZE);
                    tile.setFill(Color.SKYBLUE);
                    out.getChildren().add(tile);
                }
            }
        }
        for (int a = 0; a < TerrainGen.riverTiles.size(); a++) {
            int i = TerrainGen.riverTiles.get(a)[0];
            int j = TerrainGen.riverTiles.get(a)[1];
            if (TerrainGen.riverNoise[i][j] == 1 && tileNoise[i][j] > TerrainGen.beach) {
                Rectangle tile = new Rectangle();
                tile.setX(i * TILE_SIZE - TILE_SIZE / 2);
                tile.setY(j * TILE_SIZE - TILE_SIZE / 2);
                tile.setWidth(TILE_SIZE);
                tile.setHeight(TILE_SIZE);
                tile.setFill(Color.RED); // TODO: CHANGE COLOR
                out.getChildren().add(tile);
            }
        }*/
       /* for (int a = 0; a < TerrainGen.landmasses.size(); a++) {
            Color color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            for (int b = 0; b < TerrainGen.landmasses.get(a).size(); b++) {
                int[] cell = TerrainGen.landmasses.get(a).get(b);
                Rectangle tile = new Rectangle();
                tile.setX(cell[0] * TILE_SIZE - TILE_SIZE / 2);
                tile.setY(cell[1] * TILE_SIZE - TILE_SIZE / 2);
                tile.setWidth(TILE_SIZE);
                tile.setHeight(TILE_SIZE);
                tile.setFill(color); // TODO: CHANGE COLOR
                out.getChildren().add(tile);
            }
        }*/
        //System.out.println("Smallest Value: " + smallestVal);
        //System.out.println("Largest Value: " + largestVal);
        return out;
    }

}
