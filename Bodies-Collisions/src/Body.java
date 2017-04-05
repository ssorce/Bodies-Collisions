import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
	private static double gravity = 6.67e-11;
	private static double deltaTime;

	public static void main(String args[])
	{
		int bodies, threads;
		Double massMin, massMax, radiusMin, radiusMax;
		Simulation sim;
		String str = new String();
		if (args.length < 4) {
			Scanner keyboard = new Scanner(System.in);
			System.out.print("How many bodies: ");
			str = keyboard.next();
			bodies = Integer.parseInt(str);
			System.out.print("What are the Sizes: ");
			str = keyboard.next();
			if (str.contains("-")) {
				String[] split = str.split("-");
				radiusMin = Double.parseDouble(split[0]);
				radiusMax = Double.parseDouble(split[1]);
			}
			else {
				radiusMin = Double.parseDouble(str);
				radiusMax = Double.parseDouble(str);
			}
			System.out.print("What are the Masses: ");
			str = keyboard.next();
			if (str.contains("-")) {
				String[] split = str.split("-");
				massMin = Double.parseDouble(split[0]);
				massMax = Double.parseDouble(split[1]);
			}
			else {
				massMin = Double.parseDouble(str);
				massMax = Double.parseDouble(str);
			}
			System.out.print("How many threads: ");
			threads = Integer.parseInt(keyboard.next());

		}
		else {
			str = args[0];
			bodies = Integer.parseInt(str);
			str = args[1];
			if (str.contains("-")) {
				String[] split = str.split("-");
				radiusMin = Double.parseDouble(split[0]);
				radiusMax = Double.parseDouble(split[1]);
			}
			else {
				radiusMin = Double.parseDouble(str);
				radiusMax = Double.parseDouble(str);
			}
			str = args[2];
			if (str.contains("-")) {
				String[] split = str.split("-");
				massMin = Double.parseDouble(split[0]);
				massMax = Double.parseDouble(split[1]);
			}
			else {
				massMin = Double.parseDouble(str);
				massMax = Double.parseDouble(str);
			}
			threads = Integer.parseInt(args[3]);
		}

		for (int i = 0; i < bodies; i++) {
			double size = ThreadLocalRandom.current().nextDouble(radiusMin, radiusMax + 1);
			new Body(
					new Point(
							(int) (size + ((i > 0) ? Body.getAllbodies().get(i - 1).getRadius()
									+ Body.getAllbodies().get(i - 1).getPosition().getX() : 0)),
							ThreadLocalRandom.current().nextInt(0, 2000)),
					new Point(ThreadLocalRandom.current().nextInt(-50, 50),
							ThreadLocalRandom.current().nextInt(-50, 50)),
					new Point(ThreadLocalRandom.current().nextInt(-100, 100),
							ThreadLocalRandom.current().nextInt(-100, 100)),
					ThreadLocalRandom.current().nextDouble(massMin * 10e10, massMax * 10e10 + 1), size);
		}

		sim = new Simulation(threads, bodies, -1, false);
		sim.start();
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
	public static void calculateForces(int start, int end)
	{
		double distance;
		double magnitude;
		Point direction = new Point();
		for (int i = start; i < end; i++) {
			for (int j = i + 1; j < allBodies.size(); j++) {
				// set the distance
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
	public static void moveBodies(int start, int end)
	{
		Point deltav = new Point();
		Point deltap = new Point();
		for (int i = start; i < end; i++) {
			deltav.setLocation((allBodies.get(i).force.getX() / allBodies.get(i).mass) * deltaTime,
					(allBodies.get(i).force.getY() / allBodies.get(i).mass) * deltaTime);
			deltap.setLocation(((allBodies.get(i).velocity.getX() + ((deltav.getX())) / 2)) * deltaTime,
					((allBodies.get(i).velocity.getY() + ((deltav.getY() / 2)))) * deltaTime);
			allBodies.get(i).velocity.setLocation(allBodies.get(i).velocity.getX() + deltav.getX(),
					allBodies.get(i).velocity.getY() + deltav.getY());
			allBodies.get(i).position.setLocation(allBodies.get(i).position.getX() + deltap.getX(),
					allBodies.get(i).position.getY() + deltap.getY());
		}
	}

	/*
	 * Finds out if collision happened then switches the velocity of the bodies
	 * (currently it is elastic)
	 */
	public static void collisions(int start, int end)
	{
		int i;
		double XTop, Bottom, YTop, X2MinusX1, Y2MinusY1, distance;
		for (i = start; i < end; i++) {
			for (int j = i + 1; j < allBodies.size(); j++) {

				// This finds the distance between points compares it to the
				// radius of both i and j
				distance = Math.sqrt(Math.pow(allBodies.get(i).position.getX() - allBodies.get(j).position.getX(), 2)
						+ Math.pow(allBodies.get(i).position.getY() - allBodies.get(j).position.getY(), 2));
				if (distance < (allBodies.get(i).radius + allBodies.get(j).radius)) {
					X2MinusX1 = allBodies.get(j).getPosition().getX() - allBodies.get(i).getPosition().getX();
					Y2MinusY1 = allBodies.get(j).getPosition().getY() - allBodies.get(i).getPosition().getY();
					XTop = (allBodies.get(j).getVelocity().getX() * Math.pow(X2MinusX1, 2))
							+ (allBodies.get(j).getVelocity().getY() * X2MinusX1 * Y2MinusY1)
							+ (allBodies.get(i).getVelocity().getX() * Math.pow(Y2MinusY1, 2))
							- (allBodies.get(i).getVelocity().getY() * X2MinusX1 * Y2MinusY1);
					YTop = (allBodies.get(j).getVelocity().getX() * X2MinusX1 * Y2MinusY1)
							+ (allBodies.get(j).getVelocity().getY() * Math.pow(Y2MinusY1, 2))
							- (allBodies.get(i).getVelocity().getX() * Y2MinusY1 * X2MinusX1)
							+ (allBodies.get(i).getVelocity().getY() * Math.pow(X2MinusX1, 2));
					Bottom = Math.pow(X2MinusX1, 2) + Math.pow(Y2MinusY1, 2);
					allBodies.get(i).velocity.setLocation(XTop / Bottom, YTop / Bottom);
					XTop = (allBodies.get(i).getVelocity().getX() * Math.pow(X2MinusX1, 2))
							+ (allBodies.get(i).getVelocity().getY() * X2MinusX1 * Y2MinusY1)
							+ (allBodies.get(j).getVelocity().getX() * Math.pow(Y2MinusY1, 2))
							- (allBodies.get(j).getVelocity().getY() * X2MinusX1 * Y2MinusY1);
					YTop = (allBodies.get(i).getVelocity().getX() * X2MinusX1 * Y2MinusY1)
							+ (allBodies.get(i).getVelocity().getY() * Math.pow(Y2MinusY1, 2))
							- (allBodies.get(j).getVelocity().getX() * Y2MinusY1 * X2MinusX1)
							+ (allBodies.get(j).getVelocity().getY() * Math.pow(X2MinusX1, 2));
					allBodies.get(j).velocity.setLocation(XTop / Bottom, YTop / Bottom);
					System.out.println("Collision with centers of:\n" + allBodies.get(i).getPosition() + "\n"
							+ allBodies.get(j).getPosition());
				}
			}
		}
	}

	public static void clear()
	{
		allBodies.clear();
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

	public static void setGravity(double g)
	{
		gravity = g;
	}

}
