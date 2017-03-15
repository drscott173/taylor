//TODO: file header

import java.util.Random;
import java.util.Scanner;
import java.lang.Math;

//TODO: class header
public class GameOfLife {

	static int[] p_glider[] = {{1, 0, 0},
		       {0, 1, 1},
		       {1, 1, 0}};

	static int[] p_beacon[] = {{1, 1, 0, 0},
		       {1, 0, 0, 0},
		       {0, 0, 0, 1},
		       {0, 0, 1, 1}};

	static int[] p_beehive[] = {{0, 1, 1, 0},
			{1, 0, 0, 1},
			{0, 1, 1, 0}};

	static int[] p_r_pentomino[] = {{0, 1, 1},
			    {1, 1, 0},
			    {0, 1, 0}};

	static int[][] patterns[] = {p_glider, p_beacon, p_beehive, p_r_pentomino};

    
    //TODO: method header
    public static void main(String[] args){

	double p;
	int again,gen;
	boolean[][] world = new boolean[Config.WORLD_ROWS][Config.WORLD_COLUMNS];
	Scanner s = new Scanner(System.in);
	
	welcome();
	p = -1;
	while (p != 7) {
	    clearWorld(world);
	    p = get_pattern(s);
            initializeWorld(s, world, p);
	    if (p != 7) {
		runSimulation(s, world);
	    }
	}
	goodbye();
    }

    public static void copyWorld( boolean[][] fromWorld, boolean[][] toWorld) {
	int rows = fromWorld.length;
	int cols = fromWorld[0].length;
        for (int i=0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		toWorld[i][j] = fromWorld[i][j];
	    }
	}
    }

    public static void welcome() {
	System.out.println("Welcome to Conway's Game Of Life");
	System.out.println("--------------------------------");
    }

    public static double get_pattern(Scanner s) {
	System.out.println("1)Glider 2)Beacon 3)Beehive 4)R-pentomino");
	System.out.println("5)Random 6)Custom or 7)Exit");
	System.out.printf("Choose a pattern: ");
	return getIntChoice(s, 1, 7);
    }

    public static void initializeWorld(Scanner s, boolean[][] world, double p) {

        clearWorld(world);

	if (p == 1) {
	    initializeGliderWorld(world);
	}
	else if (p == 2) {
	    initializeBeaconWorld(world);
	}
	else if (p == 3) {
	    initializeBeehiveWorld(world);
	}
	else if (p == 4) {
	    initializeRpentominoWorld(world);
	}
	else if (p == 5) {
	    initializeRandomWorld(world);
	}	
	else if (p == 6) {
	    initializeCustomWorld(s, world);
	}

    }

    public static void add_pattern(boolean[][] world, double p) {
    
	if (p  < 0 || p > patterns.length){
	    return;
	}
	int[][] pattern = patterns[(int) p-1];
	int rows = pattern.length;
	int cols = pattern[0].length;
	int wrows = world.length;
	int wcols = world[0].length;
	clearWorld(world);
	
	int i0=1, j0=1;
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
	    	if (((i0 + i) < wrows) && ((j0+ j) < wcols)) {
	    		world[i0+i][j0+j] = (pattern[i][j] == 1);
	    }
	}
    }
    }
    

    /**
     * Prints out the world showing each cell as alive or dead.
     * 
     * Loops through every cell of the world. If a cell is alive, print out
     * the Config.ALIVE character, otherwise print out the Config.DEAD 
     * character. 
     * 
     * Counts how many cells are alive and prints out the number of cells 
     * alive.  For 2 or more cells alive, for 1 cell alive and 0 cells alive
     * the following messages are printed:
     *    5 cells are alive.
     *    1 cell is alive.
     *    No cells are alive.
     * 
     * @param world The array representation of the current world. 
     */
    public static void printWorld( boolean[][] world, int generation) {
	int rows = world.length;
	int cols = world[0].length;
	int alive = 0;
        int index = 0;
	char[] cells = {Config.DEAD, Config.ALIVE};
	
	System.out.println("");
	System.out.printf("Generation: %d\n", generation);
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {	
		index = 0;
		if (world[i][j]) {
                    index = 1;
		    alive += 1;
		}
		System.out.printf("%c", cells[index]);
	    }
	    System.out.println("");
	}
	System.out.printf("%d cells are alive.\n", alive);
    }

    /**
     * This method clears the world by setting all the cells in the 
     * world to false (dead). This method uses array lengths, 
     * not constants, to determine the size of the world.
     * 
     * @param world the world to clear
     */
    public static void clearWorld( boolean[][]world) {
    	int rows = world.length;
    	int cols = world[0].length;
    	for (int i = 0; i < rows; i++){
    		for (int j = 0; j < cols; j++ ) {
    			world[i][j] = false;
	    }
	}
    }
    

    /**
     * This method expects an integer to be entered by the user
     * between and including the values of min and max.  If the value 
     * entered is not an integer or is an integer but less than
     * min or greater than max the following message is displayed:
     *         Enter a number between 1 and 5: 
     * Assuming that min was 1 and max was 5 when this method was
     * called.
     * 
     * @param input The Scanner instance for reading from System.in.
     * @param min  The minimum acceptable integer.
     * @param max  The maximum acceptable integer.
     * @return  An integer between and including min and max.
     */
    public static int getIntChoice(Scanner input, int min, int max) {
	double answer = input.nextDouble();
	input.nextLine();
	while (answer < min || answer > max) {
	    System.out.printf("Enter a number between %d and %d: ", min, max);
	    if (input.hasNextDouble()) {
		answer = input.nextDouble();
	    }
	    input.nextLine();
	}
	return (int) answer;
    }

    /**
     * Initializes the world to the Glider pattern.
     * <pre>
     * ..........
     * .@........
     * ..@@......
     * .@@.......
     * ..........
     * ..........
     * ..........
     * ..........
     * </pre>
     * 
     * The world may have any pattern within it when passed into this
     * method. After this method call, the only living cells in the
     * world is the Glider pattern as shown.
     * 
     * @param world  the existing double dimension array that will be 
     * reinitialized to the Glider pattern.
     */
    public static void initializeGliderWorld(boolean[][]world) {

	add_pattern(world, 1);
    }

    /**
     * Initializes the world to the Beacon pattern.
     * <pre>
     * ..........
     * .@@.......
     * .@........
     * ....@.....
     * ...@@.....
     * ..........
     * ..........
     * ..........
     * </pre> 
     * 
     * The world may have any pattern within it when passed into this
     * method. After this method call, the only living cells in the
     * world is the Beacon pattern as shown.
     *
     * @param world the existing 2-dimension array that will be 
     * reinitialized to the Beacon pattern.
     */
    public static void initializeBeaconWorld(boolean[][] world) {
	add_pattern(world, 2);
    }

    /**
     * Initializes the world to the Beehive pattern.
     * <pre>
     * ..........
     * ..@@......
     * .@..@.....
     * ..@@......
     * ..........
     * ..........
     * ..........
     * ..........
     * </pre> 
     * 
     * The world may have any pattern within it when passed into this
     * method. After this method call, the only living cells in the
     * world is the Beehive pattern as shown.
     *
     * @param world the existing double dimension array that will be 
     * reinitialized to the Beehive pattern.
     */
    public static void initializeBeehiveWorld(boolean[][] world) {
	add_pattern(world, 3);
    }

    /**
     * Initializes the world to the R-pentomino pattern.
     * <pre>
     * ..........
     * ..@@......
     * .@@.......
     * ..@.......
     * ..........
     * ..........
     * ..........
     * ..........
     * </pre> 
     * 
     * The world may have any pattern within it when passed into this
     * method. After this method call, the only living cells in the
     * world is the R-pentomino pattern as shown.
     *  
     * @param world the existing double dimension array that will be 
     * reinitialized to the R-pentomino pattern.
     */
    public static void initializeRpentominoWorld(boolean[][] world) {
	add_pattern(world, 4);
    }

    /**
     * Initialize the GameOfLife world with a random selection of 
     * cells alive. 
     * 
     * For testing purposes, implementations should
     * use the Config.CHANCE_ALIVE constant and Config.SEED. 
     * Create an instance of the java.util.Random class, setting
     * the seed to the SEED constant. For each cell, if the
     * returned value of the nextDouble() method is less than 
     * Config.CHANCE_ALIVE then the cell is alive.
     * 
     * @param world  the existing double dimension array that will be 
     * reinitialized to a Random pattern.
     */
    public static void initializeRandomWorld(boolean[][] world){
        Random rand = new Random(Config.SEED);
	int rows = world.length;
	int cols = world[0].length;
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
		world[i][j] = (rand.nextDouble() < Config.CHANCE_ALIVE);
	    }
	}
    }

    /**
     * Prompt for a pattern of cells. Each line of input corresponds
     * to one row in the world. Continue reading lines until
     * 'END' is entered on a line of its own. Ignore case and 
     * leading and trailing spaces when comparing to 'END'. (See String
     * methods such as trim() method.)
     * 
     * The maximum size is the size of the world passed into this method. 
     * Any additional characters typed by the user are ignored. 
     * When interpreting the characters typed in, only the Config.ALIVE 
     * character is used to determine which cells are alive. All other 
     * characters are interpreted as dead cells.
     * 
     * @param input The Scanner instance that reads from System.in.
     * @param world The world array that is filled with the pattern the
     *          user enters.
     */
    public static void initializeCustomWorld(Scanner input,
					     boolean [][]world) {
	boolean done = false;
        clearWorld(world);
        System.out.printf("Enter a pattern using %c for alive and %c as dead cells.\n", Config.ALIVE, Config.DEAD);
        System.out.println("To end the pattern, type END on its own line.");
	for (int i=0; i < Config.WORLD_ROWS && !done; i++) {
	    done = initializeCustomRow(input, i, world);
	}
    }

    public static boolean initializeCustomRow(Scanner input, int row,
					      boolean [][]world) {
        String rowInput = input.nextLine();
	int lastChar = Math.min(Config.WORLD_COLUMNS, rowInput.length());
	
	if (rowInput.trim().toLowerCase().equals("end")) {
	    return true;
	}

	for (int j=0; j < lastChar; j++) {
	    world[row][j] = (rowInput.charAt(j) == Config.ALIVE);
	}
	return false;
    }


    /**
     * Checks whether a specific cell is alive or dead.
     * 
     * Note that cellRow and cellColumn may not be valid indexes into 
     * the world array. Return false for any cell outside the 
     * world array. Checks the values of cellRow and cellColumn to make sure
     * they are valid prior to looking in the world array. Does not use 
     * try-catch blocks or other exception handling mechanisms.
     * 
     * @param world The current world.
     * @param cellRow The row of the cell which we are wanting to know
     *  whether it is alive.
     * @param cellColumn The column of the cell which we are wanting
     *  to know whether it is alive.
     * 
     * @return Whether the specified cell is alive.
     */
    public static boolean isCellAlive(boolean [][]world, int cellRow,
				      int cellColumn) {

	int rows = world.length;
	int cols = world[0].length;
	//        System.out.printf("checking (%d,%d) in [%d, %d]", cellRow, cellColumn, rows, cols);
	if ((cellRow < 0) || (cellRow >= rows)) {
	    return false;
	}
	if ((cellColumn < 0) || (cellColumn >= cols)) {
	    return false;
	}
        boolean val = world[cellRow][cellColumn];
	if (val) {
	}
	else {
	}
	return val;
    }

    /**
     * Counts the number of neighbors that are currently living around the 
     * specified cell.
     *
     * A cell has eight neighbors. The neighbors are the cells that are 
     * horizontally, vertically and diagonally adjacent.
     * 
     * Calls the isCellAlive method to determine whether any specific cell
     * is alive.
     * 
     * @param world The current world.
     * @param row The row of the cell for which we are looking for living 
     *             neighbors.
     * @param column The column of the cell for which we are looking for 
     *             living neighbors.
     * 
     * @return The number of neighbor cells that are currently living.
     */
    public static int numNeighborsAlive(boolean[][] world, int row,
					int column) {

        int count = 0;
	for (int i=row-1; i <= (row+1); i++) {
	    for (int j=column-1; j <= (column+1); j++) {
		if (isCellAlive(world, i, j)) {
		    if (!((i == row) && (j == column))) {
			count += 1;
		    }
		}
	    }
	}
	return count;
    }

    /** 
     * Whether a cell is living in the next generation of the game.
     * 
     * The rules of the game are as follows:
     * 1) Any live cell with fewer than two live neighbors dies, as if caused
     *    by under-population.
     * 2) Any live cell with two or three live neighbors lives on to the next 
     *    generation.
     * 3) Any live cell with more than three live neighbors dies, as if by 
     *    overcrowding.
     * 4) Any dead cell with exactly three live neighbors becomes a live cell, 
     *    as if by reproduction.
     * 
     * @param numLivingNeighbors The number of neighbors that are currently
     *        living.
     * @param cellCurrentlyLiving Whether the cell is currently living.
     * 
     * @return true if this cell is living in the next generation, otherwise
     *        false.   
     */
    public static boolean isCellLivingInNextGeneration( int numLivingNeighbors,
							boolean cellCurrentlyLiving) {

	// 1) Any live cell with fewer than two live neighbors dies, as if caused
	//    by under-population.
	if (cellCurrentlyLiving && (numLivingNeighbors < 2)) {
	    return false;
	}

	// 2) Any live cell with two or three live neighbors lives on to the next 
	//    generation.
	if (cellCurrentlyLiving && 
	    ((numLivingNeighbors == 2) || (numLivingNeighbors == 3))) {
	    return true;
	}

	// 3) Any live cell with more than three live neighbors dies, as if by 
	//    overcrowding.
	if (cellCurrentlyLiving && (numLivingNeighbors > 3)) {
	    return false;
	}

	// 4) Any dead cell with exactly three live neighbors becomes a live cell, 
	//    as if by reproduction.
	if (!cellCurrentlyLiving && (numLivingNeighbors == 3)) {
	    return true;
	}

	return cellCurrentlyLiving;
    }

    /**
     * Determines the cells living in the next generation of the world. 
     * The next generation is created by applying the 4 rules simultaneously 
     * to every cell in the previous generation. Births and deaths occur 
     * simultaneously. In other words, look only at whether cells are living
     * in the current generation to determine whether a cell lives in the new
     * generation. Don't look at other cells in the new generation.
     * 
     * For each cell in the current generation, determine whether the cell
     * at the same coordinates is living in the next generation using the 
     * numNeighborsAlive and the isCellLivingInNextGeneration methods.
     * 
     * @param currentWorld The world currently shown. 
     * @param newWorld The new world based on the rules of life.
     */
    public static void nextGeneration(boolean[][] currentWorld,
				      boolean[][] newWorld) {
	int rows = currentWorld.length;
	int cols = currentWorld[0].length;

	clearWorld(newWorld);
	for (int i=0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		int n = numNeighborsAlive(currentWorld, i, j);
		newWorld[i][j] = isCellLivingInNextGeneration(n, currentWorld[i][j]);
	    }
	}
    }

    /**
     * This shows each generation of the simulation starting with 
     * generation 0. The display of the world is by calling the 
     * printWorld method and then prompting the user for whether to 
     * calculate and show the next generation.
     * Then, for any input other then 'end', this calculates the next 
     * generation using the nextGeneration method and shows it. Any case
     * of 'end' is acceptable, and also ignore leading and trailing 
     * whitespace. (See String trim() method.)
     * 
     * Note that any number of generations are possible to implement 
     * with only the world passed as the parameter and one other 
     * 2-dimensional array the same size. Create the second world
     * the same size as the world passed in, by using the length 
     * attributes rather than using constant values. The world 
     * passed in will be rectangular and not irregular.
     * 
     * @param input  The Scanner object used to read from System.in.
     * @param world  The initialized world to show as generation 0.
     */
    public static void runSimulation(Scanner input, boolean[][] world) {
	//TODO: implement method
	int again=1, gen=0;
	boolean[][] new_world = new boolean[Config.WORLD_ROWS][Config.WORLD_COLUMNS];

	while (again == 1) {
	    if (gen > 0) {
		nextGeneration(world, new_world);
		copyWorld(new_world, world);
	    }
	    printWorld(world, gen);
	    gen += 1;
	    again = shall_we_continue(input);
	}
    }

    public static void goodbye() {
	System.out.println("----------------------------");
	System.out.println("End of Conway's Game Of Life");
	
    }
    
    public static int shall_we_continue(Scanner s) {
	System.out.printf("Press Enter for next generation, 'end' to stop: ");
	String in = s.nextLine();
	if (in.equals("end")) {
	    return 0;
	}
	return 1;
    }

}
