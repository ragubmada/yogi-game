package game;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

/**
 * The YogiGameMap class generates a random map for the Yogi game.
 * It reads map configurations from text files and creates a game map.
 */
public class YogiGameMap {
    private static final int ROWS = 15;
    private static final int COLS = 15;
    private static final int[][] randomMap = new int[ROWS][COLS];

    /**
     * Generates a random map by reading configurations from a text file.
     *
     * @return A randomly generated map for the Yogi game.
     */
    public static int[][] getRandomMap() {
        Random random = new Random();
        int mapNumber = random.nextInt(10) + 1; // Randomly select map between map1.txt to map10.txt
        //mapNumber = 10; // Randomly select map between map1.txt to map10.txt

        String fileName = "maps/map" + mapNumber + ".txt";
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            // Check if the map file format is correct
            int controlRows = 0;

            // Get the dimensions of the map
            while (scanner.hasNextLine()) {
                controlRows++;
                String[] parts = scanner.nextLine().split(" ");
                int controlCols = parts.length;

                if (controlCols != 15) {
                    throw new Exception("Invalid map dimensions.");
                }
            }

            if(controlRows != 15) {
                throw new Exception("Invalid map dimensions.");
            }

            // Reinitialize the Scanner to read from the start of the file
            scanner = new Scanner(file);

            // Read and parse the map file into the generatedMap array
            int row = 0;
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");

                for (int col = 0; col < COLS; col++) {
                    switch (parts[col]) {
                        case "t" -> randomMap[row][col] = 't';
                        case "m" -> randomMap[row][col] = 'm';
                        case "y" -> randomMap[row][col] = 'y';
                        case "b" -> randomMap[row][col] = 'b';
                        case "v" -> randomMap[row][col] = 'v';
                        case "h" -> randomMap[row][col] = 'h';
                        default -> randomMap[row][col] = 0;
                    }
                }
                row++;
            }
            scanner.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        /*
        printMap();
        System.out.println("Map " + mapNumber + " generated.");
        System.out.println();
        */
        return randomMap;
    }

    /**
     * Prints the generated map to the console.
     */
    private static void printMap() {
        for (int[] row : randomMap) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
