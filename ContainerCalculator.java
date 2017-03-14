import java.util.Scanner;

public class ContainerCalculator {

    public static void main(String[] args) {
	double r, h, d, volume, area;
	Scanner s = new Scanner(System.in);

	System.out.println("Welcome to Taylor's Container Calculator!");
	System.out.println("=========================================");

	d = getDouble(s, "Enter the diameter of a cylinder (cm): ");
	h = getDouble(s, "Enter the height of a cylinder (cm): ");
	r = 0.5*d;
	volume = Math.PI*r*r*h;
        area = 2*Math.PI*r*h* + (2*Math.PI*r*r);
	
        System.out.printf("Volume: %.2f, ", volume);
        System.out.printf("Surface Area: %.2f\n", area);

    }

    private static double getDouble(Scanner s, String prompt) {

        int valid = 0;
	double val = 0.0;

        while (valid == 0) {
	    System.out.printf(prompt);
	    if (s.hasNextDouble()) {
		val = s.nextDouble();
		if (val < 0) {
		    s.nextLine();
		    System.out.println("Please enter a positive value.\n");
		}
		else {
		    valid = 1;
		}
	    }
	    else {
		s.nextLine();
		System.out.println("Please enter an integer value.\n");
	    }
	}
	return val;
    }
}		
