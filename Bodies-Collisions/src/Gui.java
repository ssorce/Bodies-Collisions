import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.Box;
import javax.swing.ButtonGroup;

/**
 * @author Jonathon Davis
 *
 */
public class Gui {

	private static JFrame frame;
	private static DrawingPanel drawingPanel;
	private static Simulation sim;
	private static int numBodies = 50;
	private static double sizeMin = 10;
	private static double sizeMax = 50;
	private static double massMin = 10;
	private static double massMax = 50;
	private static int numOfThreads = 1;
	public static boolean isActive = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try {
					new Gui();
					Gui.frame.setVisible(true);
					newSimulation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		isActive = true;
		initialize();
	}

	private static void newSimulation()
	{
		if (sim != null)
			sim.setRunning(false);
		Body.clear();
		JTextField numberOfThreads = new JTextField();
		JTextField numberOfBodies = new JTextField();
		JTextField sizeOfBodies = new JTextField();
		JTextField massOfBodies = new JTextField();
		ButtonGroup sp = new ButtonGroup();
		JRadioButton sequential = new JRadioButton();
		JRadioButton parallel = new JRadioButton();
		sp.add(sequential);
		sp.add(parallel);
		JPanel svp = new JPanel();
		svp.add(new JLabel("Sequential"));
		svp.add(sequential);
		svp.add(new JLabel("Parallel"));
		svp.add(parallel);
		final JComponent[] inputs = new JComponent[] { new JLabel("Number of Threads"), numberOfThreads,
				new JLabel("Number of Bodies"), numberOfBodies, new JLabel("Size of Bodies"), sizeOfBodies,
				new JLabel("Mass of Bodies"), massOfBodies, svp };
		int result = JOptionPane.showConfirmDialog(Gui.frame, inputs, "New Simulation Setup",
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (numberOfBodies.getText().length() > 0)
				numBodies = Integer.parseInt(numberOfBodies.getText());
			if (sizeOfBodies.getText().contains("-")) {
				String[] range = sizeOfBodies.getText().split("-");
				sizeMin = Double.parseDouble(range[0]);
				sizeMax = Double.parseDouble(range[1]);
			}
			else if (sizeOfBodies.getText().length() > 0) {
				sizeMin = Double.parseDouble(sizeOfBodies.getText());
				sizeMax = Double.parseDouble(sizeOfBodies.getText());
			}
			if (massOfBodies.getText().contains("-")) {
				String[] range = massOfBodies.getText().split("-");
				massMin = Double.parseDouble(range[0]);
				massMax = Double.parseDouble(range[1]);
			}
			else if (massOfBodies.getText().length() > 0) {
				massMin = Double.parseDouble(massOfBodies.getText());
				massMax = Double.parseDouble(massOfBodies.getText());
			}
			if (parallel.isSelected())
				numOfThreads = Integer.parseInt(numberOfThreads.getText());
			else
				numOfThreads = 1;

			// Adding making the bodies
			for (int i = 0; i < numBodies; i++) {
				double size = ThreadLocalRandom.current().nextDouble(sizeMin, sizeMax + 1);
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

			sim = new Simulation(numOfThreads, numBodies, -1);
			Gui.frame.repaint();
			sim.start();
		}
	}

	private void reset()
	{
		if (sim != null)
			sim.setRunning(false);
		Body.clear();
		for (int i = 0; i < numBodies; i++) {
			double size = ThreadLocalRandom.current().nextDouble(sizeMin, sizeMax + 1);
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
		sim = new Simulation(numOfThreads, numBodies, -1);
		Gui.frame.repaint();
		sim.start();
		sim.setRunning(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 734, 514);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenuItem mntmNewSimulation = new JMenuItem("New Simulation");
		mntmNewSimulation.addActionListener(e -> {
			newSimulation();
		});
		mnSettings.add(mntmNewSimulation);

		JMenuItem mntmResetSimulation = new JMenuItem("Reset Simulation");
		mntmResetSimulation.addActionListener(e -> {
			reset();
		});
		mnSettings.add(mntmResetSimulation);

		JPanel contentPanel = new JPanel();
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel deltavPanel = new JPanel();
		contentPanel.add(deltavPanel, BorderLayout.SOUTH);

		JLabel lblDeltaV = new JLabel("Delta V");
		deltavPanel.add(lblDeltaV);

		JLabel deltavValue = new JLabel("0");

		JSlider deltavSlider = new JSlider();
		deltavSlider.setMajorTickSpacing(10);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= 10; i++)
			labelTable.put(i * 10, new JLabel(String.format("%1$,.2f", i / 10.0)));
		deltavSlider.setLabelTable(labelTable);
		deltavSlider.setValue(0);
		deltavSlider.addChangeListener(e -> {
			double newdeltaT = deltavSlider.getValue() / 100.0;
			deltavValue.setText(String.format("%1$,.2f", newdeltaT));
			if (sim != null)
				sim.setDeltat(newdeltaT);
		});
		deltavSlider.setPaintTicks(true);
		deltavSlider.setPaintLabels(true);
		deltavSlider.setPreferredSize(new Dimension(550, 50));
		deltavPanel.add(deltavSlider);
		Body.setDeltaTime(Double.parseDouble(deltavValue.getText()));
		deltavPanel.add(deltavValue);

		drawingPanel = new DrawingPanel();
		contentPanel.add(drawingPanel, BorderLayout.CENTER);

		JPanel zoomPanel = new JPanel();
		contentPanel.add(zoomPanel, BorderLayout.EAST);

		Box verticalBox = Box.createVerticalBox();
		zoomPanel.add(verticalBox);

		JLabel lblZoom = new JLabel("Zoom");
		verticalBox.add(lblZoom);

		JSlider zoomSlider = new JSlider(JSlider.VERTICAL, 1, 11, 5);
		zoomSlider.addChangeListener(e -> {
			drawingPanel.zoom = zoomSlider.getValue();
		});
		zoomSlider.setPreferredSize(new Dimension(20, 375));
		verticalBox.add(zoomSlider);
	}

	private class DrawingPanel extends JPanel {
		private static final long serialVersionUID = 4036353244873565355L;
		private double zoom = 5;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g)
		{
			super.paint(g);
			for (int i = 0; i < Body.getAllbodies().size(); i++) {
				Body body = Body.getAllbodies().get(i);
				// fill starts at the top left of the Oval, position is at the
				// center
				g.fillOval((int) ((body.getPosition().getX() - body.getRadius() + 1) * 1 / zoom),
						(int) ((body.getPosition().getY() - body.getRadius() + 1) * 1 / zoom),
						(int) (2 * body.getRadius() * 1 / zoom), (int) (2 * body.getRadius() * 1 / zoom));
			}
		}
	}

	public static void repaint()
	{
		frame.repaint();
	}

}