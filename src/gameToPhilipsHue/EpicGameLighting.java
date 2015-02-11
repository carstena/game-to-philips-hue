package gameToPhilipsHue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import nl.q42.jue.exceptions.ApiException;

import java.awt.Color;
import java.awt.Font;

public class EpicGameLighting {

	JFrame frame;
	static JLabel lblProcessTimeValue = new JLabel("");
	static JLabel lblProcessError = new JLabel("");
	static JButton toggleBtn = new JButton("Start");
	static JComboBox<String> comboBox_area_1 = new JComboBox<String>();
	static JComboBox<String> comboBox_area_2 = new JComboBox<String>();
	static JComboBox<String> comboBox_area_3 = new JComboBox<String>();

	/**
	 * Launch the application.
	 * 
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {

		EpicGameLighting gui = new EpicGameLighting();
		gui.frame.setVisible(true);

		try {
			HueConnector.initialize();
			EpicGameLighting.toggleBtn.setVisible(true);
		} catch (ApiException e) {
			String message = "l:" + getLineNumber() + " - " + e.getMessage();
			lblProcessError.setText(message);
		}
	}

	/**
	 * Get the current line number.
	 * 
	 * @return integer - Current line number.
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	/**
	 * Create the application.
	 */
	public EpicGameLighting() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	void initialize() {
		frame = new JFrame("Epic Game Lighting for Hue");
		frame.setBounds(100, 100, 800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new MigLayout("", "[800px]", "[600px][29px][][]"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "cell 0 0 1 2,grow");

		JPanel panelCommon = new JPanel();
		tabbedPane.addTab("Hue", null, panelCommon, null);
		panelCommon.setLayout(null);

		// Version
		JLabel lblVersion = new JLabel("Version: 1.2");
		lblVersion.setBounds(20, 280, 150, 16);
		lblVersion.setForeground(Color.gray);
		panelCommon.add(lblVersion);

		// Light 1

		JLabel lblArea_1 = new JLabel("Left area light");
		lblArea_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_1.setBounds(20, 40, 230, 16);
		panelCommon.add(lblArea_1);

		comboBox_area_1.setBounds(20, 60, 230, 27);
		comboBox_area_1.addItem("");
		panelCommon.add(comboBox_area_1);

		// Light 2

		JLabel lblArea_2 = new JLabel("Center area light");
		lblArea_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_2.setBounds(255, 40, 230, 16);
		panelCommon.add(lblArea_2);

		comboBox_area_2.setBounds(255, 60, 230, 27);
		comboBox_area_2.addItem("");
		panelCommon.add(comboBox_area_2);

		// Light 3

		JLabel lblArea_3 = new JLabel("Right area light");
		lblArea_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_3.setBounds(490, 40, 230, 16);
		panelCommon.add(lblArea_3);

		comboBox_area_3.setBounds(490, 60, 230, 27);
		comboBox_area_3.addItem("");
		panelCommon.add(comboBox_area_3);

		// Process time indicator

		JLabel lblProcessTime = new JLabel("Last image processed in:");
		lblProcessTime.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblProcessTime.setBounds(24, 135, 172, 16);
		panelCommon.add(lblProcessTime);

		EpicGameLighting.toggleBtn.setVisible(false);

		lblProcessTimeValue.setBounds(24, 159, 650, 40);
		panelCommon.add(lblProcessTimeValue);

		lblProcessError.setBounds(24, 200, 450, 64);
		lblProcessError.setForeground(Color.red);
		panelCommon.add(lblProcessError);

		// Listener for start / stop button
		EpicGameLighting.toggleBtn.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				if (!HueConnector.is_activated) {
					HueConnector.start();
					toggleBtn.setText("Stop");
				} else {
					HueConnector.stop();
					toggleBtn.setText("Start");
				}
			}
		});
		frame.getContentPane().add(toggleBtn,
				"cell 0 3,alignx center,aligny top");
	}
}
