import java.util.concurrent.Semaphore;

/**
 * 
 */

/**
 * @author Jonathon Davis
 *
 */
public class Simulation extends Thread {

	private static boolean running = true;
	private static boolean paused = false;
	private static boolean printGUI;
	private static double deltat = 0;
	private static int numberOfThreads;
	private static int maxIterations;
	private static int numberOfObjects;
	private static SimulationThread threads[];
	private static Semaphore wait;

	/**
	 * Constructor for a multithreaded simulation
	 * 
	 * @param numberOThreads
	 */
	public Simulation(int numberOfThreads, int numberOfObjects, int maxIterations, boolean printGUI) {
		Simulation.numberOfThreads = numberOfThreads;
		Simulation.maxIterations = maxIterations;
		Simulation.numberOfObjects = numberOfObjects;
		this.printGUI = printGUI;
		if (!printGUI)
			deltat = 0.5;
		int stages = (int) Math.ceil(Math.log(numberOfThreads) / Math.log(2 + 1e-10));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		int stages = (int) Math.ceil(Math.log(numberOfThreads) / Math.log(2 + 1e-10));
		int workasigned = 0;
		threads = new SimulationThread[numberOfThreads];
		wait = new Semaphore(0);
		// create the threads
		for (int i = 0; i < numberOfThreads; i++) {
			threads[i] = new SimulationThread(i, workasigned,
					workasigned + (numberOfObjects - workasigned) / (numberOfThreads - i), numberOfThreads, stages);
			workasigned += (numberOfObjects - workasigned) / (numberOfThreads - i);
			threads[i].start();
		}
		// start the threads
		wait.release(numberOfThreads);
		// join the threads
		for (int i = 0; i < numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static class SimulationThread extends Thread {
		private int threadID;
		private int start;
		private int end;
		private int currentIterations;
		private static int numberOfThreads;
		private static int stages;
		private Semaphore sem[];

		private SimulationThread(int threadID, int start, int end, int numberOfThreads, int stages) {
			// set up basic thread info
			this.threadID = threadID;
			this.start = start;
			this.end = end;
			this.currentIterations = 0;
			// setup static info
			SimulationThread.numberOfThreads = numberOfThreads;
			SimulationThread.stages = stages;
			// initialize semaphores
			sem = new Semaphore[stages];
			for (int i = 0; i < stages; i++)
				sem[i] = new Semaphore(0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			int loop = 0;
			int loopEnd = 200;
			super.run();
			try {
				wait.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (running) {
				if (!paused) {
					if (threadID == 0)
						Body.setDeltaTime(Simulation.deltat);
					barrier();
					Body.calculateForces(start, end);
					barrier();
					Body.moveBodies(start, end);
					barrier();
					Body.collisions(start, end);
					barrier();
					if (printGUI == true) {
						if (threadID == 0 && Gui.isActive)
							Gui.repaint();
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						if (threadID == 0) {
							if(loopEnd < loop)
								running = false;
							loop++;
							for (int i = 0; i < Body.getAllbodies().size(); i++) {
								System.out.println(i + ": " + Body.getAllbodies().get(i).getPosition());
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		private void barrier()
		{
			if (numberOfThreads != 1) {
				for (int i = 0; i < stages; i++) {
					try {
						threads[(threadID + (1 << i)) % threads.length].sem[i].release();
						sem[i].acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running)
	{
		Simulation.running = running;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused()
	{
		return paused;
	}

	/**
	 * @param paused
	 *            the paused to set
	 */
	public void setPaused(boolean paused)
	{
		Simulation.paused = paused;
	}

	/**
	 * @return the deltat
	 */
	public double getDeltat()
	{
		return deltat;
	}

	/**
	 * @param deltat
	 *            the deltat to set
	 */
	public void setDeltat(double deltat)
	{
		Simulation.deltat = deltat;
	}

}
