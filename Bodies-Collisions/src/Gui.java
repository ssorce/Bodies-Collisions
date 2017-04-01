import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Hashtable;

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
	private static double newdelta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
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

	private static void newSimulation() {
		if(sim!=null)
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
			for (int i = 0; i < numBodies; i++) {
				//TODO: generate Bodies that are not inside one another
				new Body(new Point((2 + i), (2 + i)), new Point(i-1, i + 2), new Point(1 + i, 1 + i), mass, size);
			}
			sim = window.new Simulation();
			sim.deltaTime = newdelta;
			sim.start();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		mntmPause.addActionListener(e->{
			if(mntmPause.getText().equals("Pause")){
				if(sim!=null){
					mntmPause.setText("Resume");
					sim.paused = true;
				}
			} else {
				if(sim!=null){
					mntmPause.setText("Pause");
					sim.paused = false;
				}
			}
		});
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

		JPanel deltatPanel = new JPanel();
		contentPanel.add(deltatPanel, BorderLayout.SOUTH);

		JLabel lblDeltaT = new JLabel("Delta T");
		deltatPanel.add(lblDeltaT);

		JLabel deltavValue = new JLabel("125.00");

		JSlider deltatSlider = new JSlider();
		deltatSlider.setMajorTickSpacing(10);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= 10; i++)
			labelTable.put(i * 10, new JLabel(String.format("%1$,.0f", Math.pow(i, 3))));
		deltatSlider.setLabelTable(labelTable);
		deltatSlider.addChangeListener(e -> {
			//TODO: This is the ActionListener for the ChangeDeltaT Slider
			newdelta = Math.pow(deltatSlider.getValue() / 10.0, 3);
			if(sim!=null){
				sim.deltaTime = newdelta;
			}
			deltavValue.setText(String.format("%1$,.2f", newdelta));
		});
		deltatSlider.setPaintTicks(true);
		deltatSlider.setPaintLabels(true);
		deltatSlider.setPreferredSize(new Dimension(550, 50));
		deltatPanel.add(deltatSlider);

		deltatPanel.add(deltavValue);

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
		zoomSlider.addChangeListener(e->{
			//TODO: Implement zoom
		});
		verticalBox.add(zoomSlider);
	}

	private class DrawingPanel extends JPanel {
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		@Override
		public void paint(Graphics g) {
			//TODO: This is where the drawingPanel is painted
			super.paint(g);
			Graphics2D g2 = (Graphics2D)g;
			for(int i = 0; i < Body.getAllbodies().size(); i++){
				Body body = Body.getAllbodies().get(i);
				g2.fillOval((int)body.getPosition().getX(), (int)body.getPosition().getY(), (int)body.getRadius(), (int)body.getRadius());
			}
		}
	}

	private class Simulation extends Thread {
		private boolean running = true;
		private boolean paused = false;
		private double deltaTime;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			super.run();
			//TODO: This is where the simulation loop is
			while (running) {
				if (!paused) {
					Body.setDeltaTime(deltaTime);
					Body.calculateForces();
					Body.moveBodies();
					Body.collisions();
					frame.repaint();
				}
			}
		}

	}

}
