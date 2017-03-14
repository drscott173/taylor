import java.util.Random;

/**
 * Contains constants used by GameOfLife.
 * 
 * Constants that are defined here must be used by name because we may 
 * change these values to ensure that your program works with any 
 * values.
 * 
 * @author Jim Williams jimw@cs.wisc.edu
 */
public class Config {
    
    /** 
     * The number of rows and columns of cells in the world. 
     * 
     * For full credit, use these named constants to create the world array
     * in GameOfLife.java rather than integer values such as 8 and 10.
     * A programmer should be able to change the number of rows and columns
     * in this file and, without any other changes, the world size should 
     * change and the program should work correctly.
     * 
     * For testing, we may set WORLD_ROWS and WORLD_COLUMNS to any value
     * between and including, 5 and 80.
     */    
    public static final int WORLD_ROWS = 8;
    public static final int WORLD_COLUMNS = 10;
    
    /** 
     * The character displayed in the world if that cell is alive.
     */    
    public static final char ALIVE = '@';
    
    /** 
     * The character displayed in the world if that cell is dead.
     */    
    public static final char DEAD = '.';
    
    /** Used to seed the java.util.Random object for generating
     * random numbers used by the initializeRandomWorld method.  
     * By seeding the Random generator, we can predict
     * the "pseudo-random" values that will be generated.
     * This predictability aids in program development 
     * and allows for automated grading.
     */    
    public static final int SEED = 428;
    
    /** CHANCE_ALIVE is used in the initializeRandomWorld method to 
     * initialize the cells that are alive in the initial generation. 
     * For example a value of 0.25 means about 25% of the cells should 
     * be alive.
     */
    public static final double CHANCE_ALIVE = 0.25;
    
}

