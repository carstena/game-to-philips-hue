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
import java.awt.Font;

public class UI {

	JFrame frame;
	static JLabel lblProcessTimeValue = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	void initialize() {
		frame = new JFrame("Game to Philips Hue");
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new MigLayout("", "[600px]", "[249px][29px][][]"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "cell 0 0 1 2,grow");

		JPanel panelCommon = new JPanel();
		tabbedPane.addTab("Common", null, panelCommon, null);
		panelCommon.setLayout(null);

		JLabel lblArea_1 = new JLabel("Area 1");
		lblArea_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_1.setBounds(20, 40, 150, 16);
		panelCommon.add(lblArea_1);

		JLabel lblArea_2 = new JLabel("Area 2");
		lblArea_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_2.setBounds(199, 40, 150, 16);
		panelCommon.add(lblArea_2);

		JLabel lblArea_3 = new JLabel("Area 3");
		lblArea_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblArea_3.setBounds(368, 40, 150, 16);
		panelCommon.add(lblArea_3);

		JComboBox comboBox_area_1 = new JComboBox();
		comboBox_area_1.setBounds(20, 60, 150, 27);
		panelCommon.add(comboBox_area_1);

		JComboBox comboBox_area_2 = new JComboBox();
		comboBox_area_2.setBounds(199, 60, 150, 27);
		panelCommon.add(comboBox_area_2);

		JComboBox comboBox_area_3 = new JComboBox();
		comboBox_area_3.setBounds(368, 60, 150, 27);
		panelCommon.add(comboBox_area_3);

		JLabel lblProcessTime = new JLabel("Last image processed in:");
		lblProcessTime.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblProcessTime.setBounds(24, 135, 172, 16);
		panelCommon.add(lblProcessTime);

		lblProcessTimeValue.setBounds(20, 159, 450, 16);
		panelCommon.add(lblProcessTimeValue);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Settings", null, panel, null);
		panel.setLayout(null);

		final JButton toggleBtn = new JButton("Start");
		toggleBtn.setVisible(false);
		toggleBtn.setVisible(true);
		toggleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (!GameToPhilipsHue.is_activated) {
					GameToPhilipsHue.start();
					toggleBtn.setText("Stop");
				} else {
					GameToPhilipsHue.stop();
					toggleBtn.setText("Start");
				}
			}
		});
		frame.getContentPane().add(toggleBtn,
				"cell 0 3,alignx center,aligny top");
	}
}
