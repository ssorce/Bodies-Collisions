import java.awt.Point;
import java.util.ArrayList;

/**
 * Object representing a body that will be simulated
 * 
 * @author Jonathon Davis
 * @author Scott Sorce
 * 
 * 
 * 
 *         Joke: What do you get when you cross a mosquito with a mountain
 *         climber? Nothing. You can't cross a vector and a scalar.
 * 
 *         (http://www.businessinsider.com/13-math-jokes-that-every-mathematician-finds-absolutely-hilarious-2013-5)
 * 
 * 
 */
public class Body {
	private Point position;
	private Point velocity;
	private Point force;
	private final double mass;
	private final double radius;
	private static final ArrayList<Body> allBodies = new ArrayList<>();
	private static final double gravity = 6.67e-11;// Jupiter Gravity 24.79 m/s^2
	private static double deltaTime; // deltaTime is the number of iterations
										// (time steps for the sim)

	public static void main(String[] args)
	{
		/*
		 * Create bodies
		 * 
		 * run program for(int i = startTime; i < endTime; i+=deltaTime){
		 * calculateForces(); moveBodies(); } end of program
		 */

		double i;
		int x, j;
		deltaTime = Double.parseDouble(args[0]);
		int EndTime = Integer.parseInt(args[1]);
		int numBodies = Integer.parseInt(args[2]);
		// for (x = 0; x < numBodies; x++) {
		// new Body(new Point((2 + x), (2 + x)), new Point(x - 1, x + 2), new
		// Point(1 + x, 1 + x), (20 - (x * 3)), 5);
		// }

		// Testing Collisions
		new Body(new Point(10, 20), new Point(0, 2), new Point(3, 3), (60), 30);
		new Body(new Point(10, 50), new Point(0, -2), new Point(1, 1), (20), 10);

		for (i = 0; i < EndTime; i = deltaTime + i) {
			calculateForces();
			moveBodies();
			collisions();
			for (j = 0; j < 2; j++) {
				System.out.println("I: " + i + " J: " + j + " Location: (" + allBodies.get(j).position.getX() + ", "
						+ allBodies.get(j).position.getY() + ")");
			}
			System.out.println();
		}
	}

	/**
	 * Constructor for a Body
	 * 
	 * @param position
	 *            The initial position of the body object
	 * @param velocity
	 *            The initial velocity of the body object
	 * @param force
	 *            The initial force of the body object
	 * @param mass
	 *            The mass of the body object
	 * @param radius
	 *            The radius of the body object
	 */
	public Body(Point position, Point velocity, Point force, double mass, double radius) {
		this.position = position;
		this.velocity = velocity;
		this.force = force;
		this.mass = mass;
		this.radius = radius;
		allBodies.add(this);
	}

	/*
	 * This is going to calculate forces for all the bodies
	 */
	public static void calculateForces()
	{
		double distance;
		double magnitude;
		Point direction = new Point();
		for (int i = 0; i < allBodies.size() - 1; i++) {
			for (int j = i + 1; j < allBodies.size(); j++) {
				// set the direction
				distance = Math.sqrt(Math.pow(allBodies.get(i).position.getX() - allBodies.get(j).position.getX(), 2)
						+ Math.pow(allBodies.get(i).position.getY() - allBodies.get(j).position.getY(), 2));
				// set the magnitude
				magnitude = (gravity * allBodies.get(i).mass * allBodies.get(j).mass) / Math.pow(distance, 2);
				// set the direction
				direction.setLocation(allBodies.get(j).position.getX() - allBodies.get(i).position.getX(),
						allBodies.get(j).position.getY() - allBodies.get(i).position.getY());
				// set the force of the current iteration
				allBodies.get(i).force.setLocation(
						allBodies.get(i).force.getX() + (magnitude * direction.getX() / distance),
						allBodies.get(i).force.getY() + (magnitude * direction.getY() / distance));
				// set the force of the next iteration
				allBodies.get(j).force.setLocation(
						allBodies.get(j).force.getX() - (magnitude * direction.getX() / distance),
						allBodies.get(j).force.getY() - (magnitude * direction.getY() / distance));
			}
		}
	}

	/**
	 * Move of All Bodies
	 */
	public static void moveBodies()
	{
		Point deltav = new Point();
		Point deltap = new Point();
		for (int i = 0; i < allBodies.size(); i++) {
			deltav.setLocation((allBodies.get(i).force.getX() / allBodies.get(i).mass) * deltaTime,
					(allBodies.get(i).force.getY() / allBodies.get(i).mass) * deltaTime);
			deltap.setLocation(((allBodies.get(i).velocity.getX() + ((deltav.getX())) / 2)) * deltaTime,
					((allBodies.get(i).velocity.getY() + ((deltav.getY() / 2)))) * deltaTime);
			allBodies.get(i).velocity.setLocation(allBodies.get(i).velocity.getX() + deltav.getX(),
					allBodies.get(i).velocity.getY() + deltav.getY());
			allBodies.get(i).position.setLocation(allBodies.get(i).position.getX() + deltap.getX(),
					allBodies.get(i).position.getY() + deltap.getY());
			allBodies.get(i).force.setLocation(0, 0);
		}
	}

	/*
	 * Finds out if collision happened then switches the velocity of the bodies
	 * (currently it is inelastic)
	 */
	public static void collisions()
	{
		int i;
		boolean[] switched = new boolean[allBodies.size()];
		for (i = 0; i < allBodies.size(); i++) {
			switched[i] = false;
		}
		for (i = 0; i < allBodies.size() - 1; i++) {
			for (int j = i + 1; j < allBodies.size(); j++) {
				// Going to have to find the at which collision happened to be
				// more precise
				if (Math.abs(allBodies.get(i).position.distance(allBodies.get(j).position)) <= Math
						.max(allBodies.get(i).radius, allBodies.get(j).radius) && !switched[i] && !switched[j]) {
					System.out.println("A collision happened on i: " + i + " and j: " + j + "\nLocations:");
					System.out.println("i: " + allBodies.get(i).position + "\nj: " + allBodies.get(j).position);
					allBodies.get(i).velocity.setLocation(allBodies.get(i).velocity.getX() * -1,
							allBodies.get(i).velocity.getY() * -1);
					System.out.println("New velocity: " + allBodies.get(i).velocity);
					allBodies.get(j).velocity.setLocation(allBodies.get(j).velocity.getX() * -1,
							allBodies.get(j).velocity.getY() * -1);
					System.out.println("New velocity: " + allBodies.get(j).velocity);
					System.out.println();
					switched[i] = true;
					switched[j] = true;

				}
			}
		}
	}
	
	public static void clear(){
		for(int i = allBodies.size()-1; i >= 0; i--){
			allBodies.remove(i);
		}
	}

	/**
	 * @return the position
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * @return the velocity
	 */
	public Point getVelocity()
	{
		return velocity;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(Point velocity)
	{
		this.velocity = velocity;
	}

	/**
	 * @return the force
	 */
	public Point getForce()
	{
		return force;
	}

	/**
	 * @param force
	 *            the force to set
	 */
	public void setForce(Point force)
	{
		this.force = force;
	}

	/**
	 * @return the deltaTime
	 */
	public static double getDeltaTime()
	{
		return deltaTime;
	}

	/**
	 * @param deltaTime
	 *            the deltaTime to set
	 */
	public static void setDeltaTime(double deltaTime)
	{
		Body.deltaTime = deltaTime;
	}

	/**
	 * @return the mass
	 */
	public double getMass()
	{
		return mass;
	}

	/**
	 * @return the radius
	 */
	public double getRadius()
	{
		return radius;
	}

	/**
	 * @return the allbodies
	 */
	public static ArrayList<Body> getAllbodies()
	{
		return allBodies;
	}

	/**
	 * @return the gravity
	 */
	public static double getGravity()
	{
		return gravity;
	}

}
