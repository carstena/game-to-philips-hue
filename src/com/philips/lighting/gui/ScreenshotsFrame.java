package com.philips.lighting.gui;

import gameToPhilipsHue.EpicGameLighting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
/**
 *  ScreenshotsFrame.java                
 *
 */
public class ScreenshotsFrame extends JFrame  {

  private static final long serialVersionUID = -3830092035262367974L;
  private PHHueSDK phHueSDK;  
    
  private JList <String> lightIdentifiersList;
  private List<PHLight> allLights;
  static String defaultIndicator = "<html><body><span style='color:gray; font-size: 30px'>\u2022</span></body></html>";
  static JComboBox<String> comboBox_area_1 = new JComboBox<String>();
  static JComboBox<String> comboBox_area_2 = new JComboBox<String>();
  static JComboBox<String> comboBox_area_3 = new JComboBox<String>();
  static JLabel color1 = new JLabel(defaultIndicator, JLabel.CENTER);
  static JLabel color2 = new JLabel(defaultIndicator, JLabel.CENTER);
  static JLabel color3 = new JLabel(defaultIndicator, JLabel.CENTER);

	
  
  public ScreenshotsFrame() {
    super("Lights");
    
    // The the HueSDK singleton.
    phHueSDK = PHHueSDK.getInstance();
    
    Container content = getContentPane();
   
    // Get the selected bridge.
    PHBridge bridge = phHueSDK.getSelectedBridge(); 
    
    // To get lights use the Resource Cache.  
    allLights = bridge.getResourceCache().getAllLights();
   
    DefaultListModel <String> sampleModel = new DefaultListModel<String>();

    JScrollPane listPane = new JScrollPane(lightIdentifiersList);
    listPane.setPreferredSize(new Dimension(300,100));
    
    JPanel listPanel = new JPanel();
    listPanel.setBackground(Color.white);
    listPanel.add(listPane);
    content.add(listPanel);
    
    
    // First Area
    JLabel labelArea1 = new JLabel("Left area light");
    labelArea1.setHorizontalAlignment(SwingConstants.CENTER);
    labelArea1.setBounds(20, 40, 230, 16);
	content.add(labelArea1);

	comboBox_area_1.setBounds(20, 60, 230, 27);
	comboBox_area_1.addItem("");
	content.add(comboBox_area_1);
	
	color1.setBounds(20, 85, 230, 32);
	content.add(color1);
	
	// Second Area
	JLabel labelArea2 = new JLabel("Center area light");
	labelArea2.setHorizontalAlignment(SwingConstants.CENTER);
	labelArea2.setBounds(255, 40, 230, 16);
	content.add(labelArea2);

	comboBox_area_2.setBounds(255, 60, 230, 27);
	comboBox_area_2.addItem("");
	content.add(comboBox_area_2);
	
	color2.setBounds(255, 85, 230, 32);
	content.add(color2);
	
	// Third Area
	JLabel labelArea3 = new JLabel("Right area light");
	labelArea3.setHorizontalAlignment(SwingConstants.CENTER);
	labelArea3.setBounds(490, 40, 230, 16);
	content.add(labelArea3);

	comboBox_area_3.setBounds(490, 60, 230, 27);
	comboBox_area_3.addItem("");
	content.add(comboBox_area_3);
	
	color3.setBounds(490, 85, 230, 32);
	content.add(color3);
	
	// Fill lists with lights
	for (PHLight light : allLights) {
		comboBox_area_1.addItem(light.getIdentifier() + "  " + light.getName() );
		comboBox_area_2.addItem(light.getIdentifier() + "  " + light.getName() );
		comboBox_area_3.addItem(light.getIdentifier() + "  " + light.getName() );
    }
	
    // Start / Stop button
	JButton changeColourButton = new JButton("Start");
    changeColourButton.addActionListener(new ScreenshotProcessor());
    
	Border buttonPanelBorder = BorderFactory.createEmptyBorder();
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.white);
    buttonPanel.setBorder(buttonPanelBorder);
    buttonPanel.add(changeColourButton);
    
    content.add(buttonPanel, BorderLayout.SOUTH);
   
    setPreferredSize(new Dimension(740,600));
    pack();
    setVisible(true);
  }

  private class ScreenshotProcessor implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
    
  }
  
}