/**
 * 
 */

/**
 * @author Jonathon Davis
 *
 */
public class Simulation extends Thread {

	private boolean running = true;
	private boolean paused = false;
	private double deltat = 1;
	
	/**
	 * Constructor for a single threaded simulation
	 */
	public Simulation(){
		
	}
	
	/**
	 * Constructor for a multithreaded simulation
	 * @param numberOThreads
	 */
	public Simulation(int numberOThreads){
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		while (running) {
			if (!paused) {
				Body.setDeltaTime(deltat);
				Body.calculateForces();
				Body.moveBodies();
				Body.collisions();
				Gui.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the deltat
	 */
	public double getDeltat() {
		return deltat;
	}

	/**
	 * @param deltat the deltat to set
	 */
	public void setDeltat(double deltat) {
		this.deltat = deltat;
	}
	
	
}
