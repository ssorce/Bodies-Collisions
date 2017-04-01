import java.awt.Point;
import java.util.ArrayList;

/**
 * Object representing a body that will be simulated
 * 
 * @author Jonathon Davis
 * @author Scott Source
 */
public class Body {
	private Point position;
	private Point velocity;
	private Point force;
	private final double mass;
	private final double radius;
	private static final ArrayList<Body> allBodies = new ArrayList<>();
	private static final double gravity = 6.67e-11;

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

	/**
	 * 
	 */
	public static void calculateForces() {
		double distance;
		double magnitude;
		Point direction = new Point();
		for (int i = 0; i < allBodies.size() - 1; i++) {
			// set the direction
			distance = Math.sqrt(Math.pow(allBodies.get(i).position.getX() - allBodies.get(i + 1).position.getX(), 2)
					+ Math.pow(allBodies.get(i).position.getY() - allBodies.get(i + 1).position.getY(), 2));
			// set the magnitude
			magnitude = (gravity * allBodies.get(i).mass * allBodies.get(i + 1).mass) / Math.pow(distance, 2);
			// set the direction
			direction.setLocation(allBodies.get(i + 1).position.getX() - allBodies.get(i).position.getX(),
					allBodies.get(i + 1).position.getY() - allBodies.get(i).position.getY());
			// set the force of the current iteration
			allBodies.get(i).force.setLocation(allBodies.get(i).force.getX() + magnitude * direction.getX() / distance,
					allBodies.get(i).force.getY() + magnitude * direction.getY() / distance);
			// set the force of the next iteration
			allBodies.get(i+1).force.setLocation(allBodies.get(i+1).force.getX() + magnitude * direction.getX() / distance,
					allBodies.get(i+1).force.getY() + magnitude * direction.getY() / distance);
		}
	}

	/**
	 * 
	 */
	public static void moveBody() {
		
	}

}
