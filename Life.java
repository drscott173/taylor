import java.util.Scanner;

public class Life {

    static int[] p_glider[] = {{0, 1, 0},
			       {0, 0, 1},
			       {1, 1, 1}};

    static int[] p_beacon[] = {{1, 1, 0, 0},
			       {1, 1, 0, 0},
			       {0, 0, 1, 1},
			       {0, 0, 1, 1}};

    static int[] p_beehive[] = {{0, 1, 1, 0},
				{1, 0, 0, 1},
				{0, 1, 1, 0}};

    static int[] p_r_pentomino[] = {{0, 1, 1},
				    {1, 1, 0},
				    {0, 1, 0}};

    static int[][] patterns[] = {p_glider, p_beacon, p_beehive, p_r_pentomino};

    public static void main(String[] args) {
        double p;
	int again,gen;
	int[][] world;
        Scanner s = new Scanner(System.in);

        welcome();
	p = -1;
	while (p != 7) {
	    world = new_world();
	    p = get_pattern(s);
	    add_pattern(world, p);
	    again = 1;
	    gen = 0;
	    while (again == 1) {
		if (gen > 0) update(world);
		show_world(world, gen);
		gen += 1;
		again = shall_we_continue(s);
	    }
	}
    }

    public static void welcome() {
        System.out.println("Welcome to Conway's Game of Life");
        System.out.println("--------------------------------");
    }

    public static int[][] new_world() {
	int[][] world = new int[Config.WORLD_ROWS][Config.WORLD_COLUMNS];
	for (int i=0; i < Config.WORLD_ROWS; i++) {
	    for (int j=0; j < Config.WORLD_COLUMNS; j++) {
		world[i][j] = 0;
	    }
	}
	return world;
    }

    public static double get_pattern(Scanner s) {
	System.out.println("1)Glider 2)Beacon 3)Beehive 4)R-pentomino");
	System.out.println("5)Random 6)Custom or 7)Exit");
	System.out.printf("Choose a pattern: ");
	double answer = s.nextDouble();
	s.nextLine();
	return answer;
    }

    public static void add_pattern(int[][] world, double p) {
	int[][] pattern = patterns[(int) p-1];
	int rows = pattern.length;
	int cols = pattern[0].length;
	int i0=1, j0=1;
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
		world[i0+i][j0+j] = pattern[i][j];
		System.out.printf("%d", pattern[i][j]);
	    }
	    System.out.println("");
	}
    }

    public static void show_world(int[][] world, int generation) {
	int rows = world.length;
	int cols = world[0].length;
	int alive = 0;
	String[] cells = {".", "@"};
	
	System.out.println("");
	System.out.printf("Generation: %d\n", generation);
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
		alive += world[i][j];
		System.out.printf("%s", cells[world[i][j]]);
	    }
	    System.out.println("");
	}
	System.out.printf("%d cells are alive.\n", alive);
    }


    public static void update(int[][] world) {
	int rows = world.length;
	int cols = world[0].length;
	int[][] counts = count_grid(world);

	// figure out who lives and dies after we counted
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
		int count = counts[i][j];
		System.out.printf("%d ", count);

		// Any live cell with fewer than two live neighbors dies,
		// as if caused by under-population.
		if ((world[i][j] == 1) && (count < 2)) world[i][j] = 0;

		// Any live cell with two or three live neighbors lives
		// on to the next generation.
		if ((world[i][j] == 1) && ((count == 2) || (count == 3)))
		    world[i][j] = 1;

		// Any live cell with more than three live neighbors dies,
		// as if by overcrowding.
		if ((world[i][j] == 1) && (count > 3)) 
		    world[i][j] = 0;

		// Any dead cell with exactly three live neighbors becomes
		// a live cell, as if by reproduction.
		if ((world[i][j] == 0) && (count == 3))
		    world[i][j] = 1;
	    }
	    System.out.println("");
	}
    }

    public static int index_wrap(int i, int limit) {
	int mod = (mod = i % limit) < 0 ? (mod + limit) : mod;
	return mod;
    }

    public static int[][] count_grid(int[][] world) {
	int rows = world.length;
	int cols = world[0].length;
	int[][] counts = new int[rows][cols];
	
        // take a snapshot of counts before we decide who lives or dies
	for (int i=0; i < rows; i++) {
	    for (int j=0; j < cols; j++) {
		counts[i][j] = count_neighbors(world, i, j);
	    }
	}
	return counts;
    }

    public static int count_neighbors(int[][] world, int row, int col) {
	int rows = world.length;
	int cols = world[0].length;
	int count = 0;
        //System.out.printf("Scanning for (%d, %d):\n", row, col);

	for (int i = (row-1); i <= (row+1); i++) {
	    int wrap_i = index_wrap(i, rows);
	    for (int j = (col-1); j <= (col+1); j++) {
		int wrap_j = index_wrap(j, cols);
		if (world[wrap_i][wrap_j] == 1) {
		    count += 1;
		}
	    }
	}
	int result = count - world[row][col];
	//System.out.printf("We counted %d live cells\n\n",  result);

	return count - world[row][col];
    }

    public static int shall_we_continue(Scanner s) {
	System.out.printf("Press Enter for next generation, 'end' to stop: ");
	String in = s.nextLine();
	System.out.printf("You entered '%s'\n", in);
	if (in.equals("end")) {
	    System.out.println("Ending");
	    return 0;
	}
	return 1;
    }

}

