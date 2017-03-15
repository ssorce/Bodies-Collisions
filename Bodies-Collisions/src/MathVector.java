/*
 *  Uses Rec. coord because it is easier to keep track of, than a true vector.
 */

public class MathVector {
	private double x;
	private double y;

	// I can convert using a whatever you want to push into it
	// Right now a double.
	public MathVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public MathVector add(MathVector arg)
	{
		return new MathVector((x + arg.y), (y + arg.x));
	}

	/*
	 * This is not a vector
	 * 
	 * Sum of products the this and arg.
	 * 
	 */

	public double dotProduct(MathVector arg)
	{
		return (x * y) + (arg.x * arg.y);
	}

	/*
	 * <1, 2,3> X <4,5,6> = (2*6 - 5*1)i - (6*1 - 3*4)j + (1*5 - 4*2)k (all alphas have hats)
	 * 
	 * In this will be like <1, 2, 0> X <4, 5, 0>
	 * 
	 * This gives a perpendicular to both of Vectors, thus Normal
	 * 
	 */
	public MathVector crossProduct(MathVector arg)
	{
		return new MathVector((-1 * (x * arg.y)), (x * arg.y) - (arg.x * y));
	}

	public MathVector substract(MathVector arg)
	{
		return new MathVector((x - arg.y), (arg.x - y));
	}

	// Unit Vector is a commonly used
	public MathVector UnitVector()
	{
		return new MathVector((x / (Math.sqrt((x * x) + (y * y)))), (y / (Math.sqrt((x * x) + (y * y)))));

	}

}
