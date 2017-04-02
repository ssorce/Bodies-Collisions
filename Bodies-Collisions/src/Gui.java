import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.Box;

/**
 * 
 */

/**
 * @author Jonathon Davis
 *
 */
public class Gui {

	private static Gui window;
	private static JFrame frame;
	private static JPanel drawingPanel;
	private static Simulation sim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try {
					window = new Gui();
					window.frame.setVisible(true);
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
			sim.running = false;
		JTextField numberOfBodies = new JTextField();
		JTextField sizeOfBodies = new JTextField();
		JTextField massOfBodies = new JTextField();
		final JComponent[] inputs = new JComponent[] { new JLabel("Number of Bodies"), numberOfBodies,
				new JLabel("Size of Bodies"), sizeOfBodies, new JLabel("Mass of Bodies"), massOfBodies };
		int result = JOptionPane.showConfirmDialog(window.frame, inputs, "New Simulation Setup",
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			double numBodies = Double.parseDouble(numberOfBodies.getText());
			double size = Double.parseDouble(sizeOfBodies.getText());
			double mass = Double.parseDouble(massOfBodies.getText());
			// Testing collisions and Visual
			/*
			 * for (int i = 0; i < numBodies; i++) { if (i % 2 == 0) new
			 * Body(new Point((100 * i), (100 * i)), new Point(i * 20, i * 20),
			 * new Point(30 * i, 30 * i), mass, size); else new Body(new
			 * Point((70 * i), (20 * i)), new Point(i * 20, i * -20), new
			 * Point(30 * i, -30 * i), mass, size); }
			 */
			/*
			 * Random rand = new Random(); for (int i = 0; i < numBodies; i++) {
			 * new Body(new Point(rand.nextInt((300) + 1), rand.nextInt((300) +
			 * 1)), new Point(rand.nextInt((20) + 1), rand.nextInt((20) + 1)),
			 * new Point(rand.nextInt((10) + 1), rand.nextInt((10) + 1)), mass,
			 * size); }
			 */
			new Body(new Point(50, 5), new Point(0, 2), new Point(300, 300), (400), 30);
			new Body(new Point(30, 300), new Point(0, -2), new Point(0, 0), (20), 10);
			new Body(new Point(60, 500), new Point(-1, -2), new Point(110, 110), (100), 50);
			sim = window.new Simulation();
			sim.start();
		}
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
		mnSettings.add(mntmResetSimulation);

		JMenuItem mntmPause = new JMenuItem("Pause");
		mnSettings.add(mntmPause);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnSettings.add(mntmSave);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnSettings.add(mntmLoad);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmAddObject = new JMenuItem("Add Object");
		mnEdit.add(mntmAddObject);

		JPanel contentPanel = new JPanel();
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel deltavPanel = new JPanel();
		contentPanel.add(deltavPanel, BorderLayout.SOUTH);

		JLabel lblDeltaV = new JLabel("Delta V");
		deltavPanel.add(lblDeltaV);

		JLabel deltavValue = new JLabel(".50");

		JSlider deltavSlider = new JSlider();
		deltavSlider.setMajorTickSpacing(10);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= 10; i++)
			labelTable.put(i * 10, new JLabel(String.format("%1$,.0f", Math.pow(i, 3))));
		deltavSlider.setLabelTable(labelTable);
		deltavSlider.addChangeListener(e -> {
			deltavValue.setText(String.format("%1$,.2f", Math.pow(deltavSlider.getValue() / 10.0, 3)));
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

		JSlider zoomSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
		zoomSlider.setPreferredSize(new Dimension(20, 375));
		verticalBox.add(zoomSlider);
	}

	private class DrawingPanel extends JPanel {
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
				g.fillOval((int) body.getPosition().getX(), (int) body.getPosition().getY(), (int) body.getRadius(),
						(int) body.getRadius());
			}
		}
	}

	private class Simulation extends Thread {
		private boolean running = true;
		private boolean paused = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			super.run();
			while (running) {
				if (!paused) {
					Body.calculateForces();
					Body.moveBodies();
					Body.collisions();
					drawingPanel.repaint();
					try {
						this.sleep(100); // This allows the User to visually see
											// what is happening, (decreasing
											// the faster things move)
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

}
