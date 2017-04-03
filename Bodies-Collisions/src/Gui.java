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
	private static double numBodies = 0;
	private static double sizeMin = 0;
	private static double sizeMax = 0;
	private static double massMin = 0;
	private static double massMax = 0;

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
			numBodies = Double.parseDouble(numberOfBodies.getText());
			sizeMin = 0;
			sizeMax = 0;
			if (sizeOfBodies.getText().contains("-")) {
				String[] range = sizeOfBodies.getText().split("-");
				sizeMin = Double.parseDouble(range[0]);
				sizeMax = Double.parseDouble(range[1]);
			}
			else {
				sizeMin = Double.parseDouble(sizeOfBodies.getText());
				sizeMax = Double.parseDouble(sizeOfBodies.getText());
			}
			massMin = 0;
			massMax = 0;
			if (massOfBodies.getText().contains("-")) {
				String[] range = massOfBodies.getText().split("-");
				massMin = Double.parseDouble(range[0]);
				massMax = Double.parseDouble(range[1]);
			}
			else {
				massMin = Double.parseDouble(massOfBodies.getText());
				massMax = Double.parseDouble(massOfBodies.getText());
			}
			// Testing collisions and Visual
			for (int i = 0; i < numBodies; i++) {
				double size = ThreadLocalRandom.current().nextDouble(sizeMin, sizeMax + 1);
				new Body(
						new Point(
								(int) (size + ((i > 0) ? Body.getAllbodies().get(i - 1).getRadius()
										+ Body.getAllbodies().get(i - 1).getPosition().getX() : 0)),
								ThreadLocalRandom.current().nextInt(-50, 50)),
						new Point(ThreadLocalRandom.current().nextInt(-50, 50),
								ThreadLocalRandom.current().nextInt(-50, 50)),
						new Point(ThreadLocalRandom.current().nextInt(-50, 50),
								ThreadLocalRandom.current().nextInt(-50, 50)),
						ThreadLocalRandom.current().nextDouble(massMin, massMax + 1), size);
			}

			// new Body(new Point(50, 5), new Point(0, 2), new Point(300, 300),
			// (400), 30);
			// new Body(new Point(30, 300), new Point(0, -2), new Point(1, 2),
			// (20), 10);
			// new Body(new Point(90, 500), new Point(-1, -5), new Point(110,
			// 110), (100), 50);
			sim = new Simulation();
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
							ThreadLocalRandom.current().nextInt(-50, 50)),
					new Point(ThreadLocalRandom.current().nextInt(-50, 50),
							ThreadLocalRandom.current().nextInt(-50, 50)),
					new Point(ThreadLocalRandom.current().nextInt(-50, 50),
							ThreadLocalRandom.current().nextInt(-50, 50)),
					ThreadLocalRandom.current().nextDouble(massMin, massMax + 1), size);
		}
		sim = new Simulation();
		Gui.frame.repaint();
		sim.start();
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

		JLabel deltavValue = new JLabel("1");

		JSlider deltavSlider = new JSlider();
		deltavSlider.setMajorTickSpacing(10);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= 10; i++)
			labelTable.put(i * 10, new JLabel(String.format("%1$,.0f", Math.pow(i, 3))));
		deltavSlider.setLabelTable(labelTable);
		deltavSlider.setValue(10);
		deltavSlider.addChangeListener(e -> {
			double newdeltaT = Math.pow(deltavSlider.getValue() / 10.0, 3);
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
				g.fillOval((int) ((body.getPosition().getX() - body.getRadius()) * 1 / zoom),
						(int) ((body.getPosition().getY() + body.getRadius()) * 1 / zoom),
						(int) (body.getRadius() * 1 / zoom), (int) (body.getRadius() * 1 / zoom));
			}
		}
	}

	public static void repaint()
	{
		frame.repaint();
	}

}
