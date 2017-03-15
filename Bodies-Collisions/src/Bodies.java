import java.awt.Point;
/*
 * Each Bodies is like a planet, but at the current loc in space
 */
public class Bodies {
	private double mass; // kilograms
	private double velocity; // meters per secs (using rec cord. system)
	private Point loc;

	public Bodies(double weight, double speed, Point current) {
		mass = weight;
		velocity = speed;
		loc = current;
	}

	public Point getLoc()
	{
		return loc;
	}

	public double getMass()
	{
		return mass;
	}

	public double getVelocity()
	{
		return velocity;
	}

	public void SetLoc(Point loc)
	{
		this.loc = loc;
	}

	public void SetVelocity(double vel)
	{
		this.velocity = vel;
	}

}
