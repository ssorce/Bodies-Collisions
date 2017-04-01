import java.awt.EventQueue;
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
	private JFrame frame;

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
	
	private static void newSimulation(){
		JTextField numberOfBodies = new JTextField();
		JTextField sizeOfBodies = new JTextField();
		JTextField massOfBodies = new JTextField();
		final JComponent[] inputs = new JComponent[] {
		        new JLabel("Number of Bodies"),
		        numberOfBodies,
		        new JLabel("Size of Bodies"),
		        sizeOfBodies,
		        new JLabel("Mass of Bodies"),
		        massOfBodies
		};
		int result = JOptionPane.showConfirmDialog(window.frame, inputs, "New Simulation Setup", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
		    System.out.println("You entered " +
		    		numberOfBodies.getText() + ", " +
		    		sizeOfBodies.getText() + ", " +
		    		massOfBodies.getText());
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
		mntmNewSimulation.addActionListener(e->{
			newSimulation();
		});
		mnSettings.add(mntmNewSimulation);
		
		JMenuItem mntmResetSimulation = new JMenuItem("Reset Simulation");
		mnSettings.add(mntmResetSimulation);
		
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
		
		JLabel deltavValue = new JLabel("125.00");
		
		JSlider deltavSlider = new JSlider();
		deltavSlider.setMajorTickSpacing(10);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for(int i = 0; i <= 10; i++)
			labelTable.put(i*10, new JLabel(String.format("%1$,.0f", Math.pow(i,3))) );
		deltavSlider.setLabelTable(labelTable);
		deltavSlider.addChangeListener(e->{
			deltavValue.setText(String.format("%1$,.2f", Math.pow(deltavSlider.getValue()/10.0,3)));
		});
		deltavSlider.setPaintTicks(true);
		deltavSlider.setPaintLabels(true);
		deltavSlider.setPreferredSize(new Dimension(550,50));
		deltavPanel.add(deltavSlider);
		
		deltavPanel.add(deltavValue);
		
		JPanel drawingPanel = new JPanel();
		contentPanel.add(drawingPanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.EAST);
		
		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);
		
		JLabel lblZoom = new JLabel("Zoom");
		verticalBox.add(lblZoom);
		
		JSlider zoomSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
		zoomSlider.setPreferredSize(new Dimension(20,375));
		verticalBox.add(zoomSlider);
	}
	
	private class DrawingPanel extends JPanel{
		
	}

}
