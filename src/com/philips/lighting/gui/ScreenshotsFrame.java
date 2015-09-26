package com.philips.lighting.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.philips.lighting.ImageProcessor;
import com.philips.lighting.data.HueProperties;
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
  static JLabel log = new JLabel("", JLabel.CENTER);
  public static boolean isProcessing = false;
  static JButton changeColourButton = new JButton("Start");
  
  public ScreenshotsFrame() {
    super("Lights");
    
    // The the HueSDK singleton.
    phHueSDK = PHHueSDK.getInstance();
    
    Container content = getContentPane();
   
    // Get the selected bridge.
    PHBridge bridge = phHueSDK.getSelectedBridge(); 
    
    // To get lights use the Resource Cache.  
    allLights = bridge.getResourceCache().getAllLights();
   
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
	
	log.setBounds(490, 60, 230, 32);
	content.add(log);
	
	// Fill lists with lights
	for (PHLight light : allLights) {
		comboBox_area_1.addItem(light.getIdentifier() + "  " + light.getName() );
		comboBox_area_2.addItem(light.getIdentifier() + "  " + light.getName() );
		comboBox_area_3.addItem(light.getIdentifier() + "  " + light.getName() );
    }
	
    // Start / Stop button
	
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
		
		isProcessing = !isProcessing;
		
		if (isProcessing) {
			step();
			changeColourButton.setText("Stop");
		} else {
			changeColourButton.setText("Start");
		}
	}
  }

  public void step() {
	  
	  ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        	// Run event
        	
        	System.out.println("step");
      	  
      	  
      	  long startTime = System.currentTimeMillis();
      	  List<Object> colorAndSaturationArray = new ArrayList<Object>();
      		
      	  
      	  // Read directory
      	  String folderPath = HueProperties.getFolderPath();
      	
      	  final File folder = new File(folderPath);
      	  final File tempFolder = new File(folderPath + "/temp/");
      	
      	  String tempPath = null;
      	  BufferedImage image = null;
      	  File[] files = folder.listFiles();
      	
      	  Arrays.sort(files, new Comparator<File>() {
      		  public int compare(File a, File b) {
      			  return (int) (b.lastModified() - a.lastModified());
      		  }
      	  });	
      	
      	  int fileCount = 0;
      	  for (final File fileEntry : files) {
      		
      		  if (!fileEntry.isDirectory() && !fileEntry.isHidden()) {
      			fileCount = fileCount + 1;
      		  }
      	  }
      			
      	  if (fileCount > 0) {
      		  int i = 0;
      				
      			for (final File fileEntry : files) {
      				
      				if (!fileEntry.isDirectory() && !fileEntry.isHidden()) {
      					
      					if (i == 0) {
      						tempPath = folderPath + "/temp/" + fileEntry.getName();
      						
      						if(fileEntry.renameTo(new File(tempPath))){ // move file
      							
      							java.net.URL url = null;
      							try {
      								url = new File(tempPath).toURI().toURL();
      							} catch (MalformedURLException e1) {
      								e1.printStackTrace();
      							}

      							try {
      								image = ImageIO.read(url);
      							} catch (IOException e1) {
      								e1.printStackTrace();
      							}
      						}
      					}

      					i++;
      				}
      			}
      			
      			// Delete all files in folder
      			for (final File fileEntry : folder.listFiles()) {
      				if (!fileEntry.isDirectory() && fileEntry.exists()) {
      					 fileEntry.delete(); // Delete file
      				}
      			}
      		}
      		
      		if (image != null) {
      			
      			int i = 0;
      			
      			// Get Image color in three vertical parts
      			for (i = 1; i <= 3; i++) {

      				// Get Dominant color from image part
      				Object[] colorAndSaturation = ImageProcessor.getDominantColor(image, true, i);
      				colorAndSaturationArray.add(colorAndSaturation);
      			}
      			
      			// TODO improve this code, is not very efficient
      			for (int j = 0; j < 3; j++) { // 3 is the number of lights
      				
      				int selectedIndex = 0;
      				
      				if ((j + 1) == 1) {
      					selectedIndex = comboBox_area_1.getSelectedIndex() - 1;
      				}
      				if ((j + 1) == 2) {
      					selectedIndex = comboBox_area_2.getSelectedIndex() - 1;
      				}
      				if ((j + 1) == 3) {
      					selectedIndex = comboBox_area_3.getSelectedIndex() - 1;
      				}
      				
      				Object[] colorAndSaturation = (Object[]) colorAndSaturationArray.get(j);
      				System.out.println("index size:");
      				System.out.println(colorAndSaturation.length);
      				Color rgbcolor = (Color) colorAndSaturation[0];
      				
      				if (rgbcolor.getRed() > 0 && rgbcolor.getGreen() > 0 && rgbcolor.getBlue() > 0) {
      					
      					Object saturation = colorAndSaturation[1];
      					
      					String lightIdentifer = allLights.get(selectedIndex).getIdentifier();
      					
      					// Update state of LIght
      					PHLightState lightState = new PHLightState();
      	                float xy[] = PHUtilities.calculateXYFromRGB(rgbcolor.getRed(), rgbcolor.getGreen(), rgbcolor.getBlue(), allLights.get(selectedIndex).getModelNumber());
      	                lightState.setX(xy[0]);
      	                lightState.setY(xy[1]);
      	                lightState.setOn(true);
      	                lightState.setBrightness((Integer) saturation);
      	                
      	                phHueSDK.getSelectedBridge().updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
      	                
      	                // Update color indicators
      					if(j+1 == 1) {
      						color1.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
      					} else if(j+1 == 2) {
      						color2.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
      					} else if(j+1 == 3) {
      						color3.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
      					}	
      				}
      			}
      			
      			// Delete all files in temp folder
      			for (final File fileEntry : tempFolder.listFiles()) {
      				if (!fileEntry.isDirectory() && fileEntry.exists()) {
      					 fileEntry.delete(); // Delete file
      				}
      			}
      			 
      			long endTime = System.currentTimeMillis();
      			long duration = endTime - startTime;
      			String text = "Image processd in " + duration + "ms";
      			
      			log.setText(text);
      			System.out.println(text);
      			
      			if (isProcessing) {
      				step();
      			}
      		}
      		else {
      			if (isProcessing) {
      				step();
      			}
      		}
        }
	  };
	  
	  // Timeout
	  javax.swing.Timer t = new javax.swing.Timer(100, taskPerformer);
	  t.setRepeats(false);
	  t.start();
  }
}