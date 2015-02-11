package gameToPhilipsHue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import nl.q42.jue.FullLight;
import nl.q42.jue.HueBridge;
import nl.q42.jue.Light;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;

public class HueConnector {

	protected static final BufferedImage image = null;
	public static boolean is_processing = false;
	public static boolean is_activated = false;
	public static String current_file;
	public static HueBridge bridge;

	static Runnable hueRunnable = new Runnable() {

		/**
		 * Get the current line number.
		 * 
		 * @return integer - Current line number.
		 */
		public int getLineNumber() {
			return Thread.currentThread().getStackTrace()[2].getLineNumber();
		}

		public void run() {

			try {

				if (HueConnector.is_processing == false
						&& HueConnector.is_activated == true) {
					HueConnector.is_processing = true;

					BufferedImage image = null;

					long startTime = System.currentTimeMillis();

					// Read images
					String folder_path = Config.path;
					final File folder = new File(folder_path);

					String path = null;
					File[] files = folder.listFiles();

					Arrays.sort(files, new Comparator<File>() {
						public int compare(File a, File b) {
							return (int) (b.lastModified() - a.lastModified());
						}
					});

					if (files.length > 0) {
						int i = 0;
						for (final File fileEntry : files) {
							if (!fileEntry.isDirectory()
									&& !fileEntry.isHidden()) {

								if (i == 0) {
									path = folder_path + fileEntry.getName();
									current_file = fileEntry.getName();
									java.net.URL url = new File(path).toURI()
											.toURL();

									image = ImageIO.read(url);
								}

								if (fileEntry.exists() // Check if file exists
										&& fileEntry.canRead()
										&& !fileEntry.getName().equals(
												current_file)) {
									try {
										fileEntry.delete(); // Delete used image
									} catch (Exception e) {
										String message = "l:" + getLineNumber()
												+ " - " + e.getMessage();
										EpicGameLighting.lblProcessError
												.setText(message);
									}
								}

								i++;
							}
						}

						if (image != null) { // Check if file is image

							// Set selected Lights in List
							// TODO: move to start function
							List<String> selectedLightsList = new ArrayList<String>();

							selectedLightsList.add(String
									.valueOf(EpicGameLighting.comboBox_area_1
											.getSelectedItem()));
							selectedLightsList.add(String
									.valueOf(EpicGameLighting.comboBox_area_2
											.getSelectedItem()));
							selectedLightsList.add(String
									.valueOf(EpicGameLighting.comboBox_area_3
											.getSelectedItem()));

							// Get Image color in three vertical parts
							List<Object> colorAndSaturationArray = new ArrayList<Object>();
							for (i = 1; i <= 3; i++) {

								// Get Dominant color from image part
								Object[] colorAndSaturation = getDominantColor(
										image, true, i);

								colorAndSaturationArray.add(colorAndSaturation);
							}

							// TODO improve this code, is not very efficient
							for (int j = 0; j < selectedLightsList.size(); j++) {

								Object[] colorAndSaturation = (Object[]) colorAndSaturationArray.get(j);

								Color rgbcolor = (Color) colorAndSaturation[0];
								Object saturation = colorAndSaturation[1];

								if (rgbcolor.getRed() > 0 && rgbcolor.getGreen() > 0 && rgbcolor.getBlue() > 0) {

									for (Light light : bridge.getLights()) {

										// Set state
										if (light.getName().equals(selectedLightsList.get(j).toString())) {
								
											FullLight fullLight = bridge.getLight(light);
											
											// Convert RGB to XY
											float xyColor[] = PHUtilities.calculateXYFromRGB(
															rgbcolor.getRed(),
															rgbcolor.getGreen(),
															rgbcolor.getBlue(),
															fullLight.getModelID()); // previous LCT001
											
											// Update color indicators
											if(j+1 == 1) {
												EpicGameLighting.color1.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
											} else if(j+1 == 2) {
												EpicGameLighting.color2.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
											} else if(j+1 == 3) {
												EpicGameLighting.color3.setText("<html><body><span style='color:rgb("+rgbcolor.getRed()+","+rgbcolor.getGreen()+","+rgbcolor.getBlue()+",); font-size: 30px'>\u2022</span></body></html>");
											}						

											// Set lights. Lights commands a have a max of around 10 commands per second
											StateUpdate update = new StateUpdate()
													.turnOn()
													.setXY(xyColor[0], xyColor[1])
													.setBrightness((Integer) saturation);
											
											
											bridge.setLightState(fullLight,update);
										}
									}
								}
							}
							
							EpicGameLighting.lblProcessError.setText("");
									
						} else {
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();
							EpicGameLighting.lblProcessError.setText("File is not an image - " + dateFormat.format(date));
						}
					} else {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						EpicGameLighting.lblProcessError
								.setText("<html><body>Error: no file found.<br>1. Is your screencapture program running?<br>2. Is your folderpath correct? "
										+ Config.path
										+ "<br>"
										+ dateFormat.format(date)
										+ "</body></html>");
					}

					// Reset

					HueConnector.is_processing = false;
					current_file = null;

					long endTime = System.currentTimeMillis();
					long duration = endTime - startTime;
					String text = "" + duration + "ms |" + path;

					Object labelText = String.format(
							"<html><div WIDTH=%d>%s</div><html>", 100, text);

					EpicGameLighting.lblProcessTimeValue
							.setText((String) labelText);
				}

			} catch (Exception e) {

				String message;
				if (e.getMessage() == null) {
					message = "Something went wrong. Check your path in config.properties.";
				} else {
					message = "l:" + getLineNumber() + " - " + e.getMessage();
				}
				EpicGameLighting.lblProcessError.setText(message);
			}
		}

		private Object[] getDominantColor(BufferedImage image,
				boolean applyThreshold, int part) {

			// Keep track of how many times a hue in a given bin appears
			// in the image.
			// Hue values range [0 .. 360), so dividing by 10, we get 36
			// bins.
			int[] colorBins = new int[36];

			// The bin with the most colors. Initialize to -1 to prevent
			// accidentally
			// thinking the first bin holds the dominant color.
			int maxBin = -1;

			// Keep track of sum hue/saturation/value per hue bin, which
			// we'll use to
			// compute an average to for the dominant color.
			float[] sumHue = new float[36];
			float[] sumSat = new float[36];
			float[] sumVal = new float[36];
			float[] hsv = new float[3];

			int total_parts = 3;
			int height = image.getHeight();
			int width = image.getWidth();
			int part_width = (int) Math.floor(width / total_parts);
			int col_nr = part_width * part - part_width;

			for (int row = 0; row < height; row++) {
				for (int col = col_nr; col < (part_width * part); col++) {

					Color c = new Color(image.getRGB(col, row));

					hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),
							null);

					// If a threshold is applied, ignore arbitrarily chosen
					// values for "white" and "black".
					if (applyThreshold && (hsv[1] <= 0.35f || hsv[2] <= 0.35f))
						continue;

					// We compute the dominant color by putting colors
					// in bins based on their hue.
					int bin = (int) Math.floor(hsv[0] / 10.0f);

					// Update the sum hue/saturation/value for this bin.
					sumHue[bin] = sumHue[bin] + hsv[0];
					sumSat[bin] = sumSat[bin] + hsv[1];
					sumVal[bin] = sumVal[bin] + hsv[2];

					// Increment the number of colors in this bin.
					colorBins[bin]++;

					// Keep track of the bin that holds the most colors.
					if (maxBin < 0 || colorBins[bin] > colorBins[maxBin])
						maxBin = bin;
				}
			}

			// maxBin may never get updated if the image holds only transparent
			// and/or black/white pixels.
			if (maxBin < 0) {
				Color black_color = new Color(0, 0, 0, 0);
				Object[] objectArray = { black_color };
				return objectArray;
			}

			// Return a color with the average hue/saturation/value of
			// the bin with the most colors.
			hsv[0] = sumHue[maxBin] / colorBins[maxBin];
			hsv[1] = sumSat[maxBin] / colorBins[maxBin];
			hsv[2] = sumVal[maxBin] / colorBins[maxBin];

			int rgb = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
			Color rgbcolor = new Color(rgb);

			// TODO: brightness must be separated from color?
			Object[] objectArray = { rgbcolor, (Integer) Brightness(rgbcolor) };
			return objectArray;
		}
	};

	/**
	 * @see http://alienryderflex.com/hsp.html
	 * @param c
	 * @return
	 */
	private static int Brightness(Color c) {
		return (int) Math.sqrt(c.getRed() * c.getRed() * .241 + c.getGreen()
				* c.getGreen() * .691 + c.getBlue() * c.getBlue() * .068);
	}

	public static void start() {

		emptyFolder();
		HueConnector.is_activated = true;
		EpicGameLighting.lblProcessError.setText("");
	}

	public static void stop() {
		HueConnector.is_processing = false;
		HueConnector.is_activated = false;
	}

	public static void main(String[] args) {

	}

	/**
	 * Get the current line number.
	 * 
	 * @return integer - Current line number.
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	public static void initialize() throws ApiException {

		Properties prop = new Properties();

		try {

			// load a properties file
			prop.load(new FileInputStream("config.properties"));

			// get the property value and save it to configuration
			Config.username = prop.getProperty("username");
			Config.ip = prop.getProperty("ip");
			Config.refreshrate = Integer.parseInt(prop
					.getProperty("refreshrate"));
			Config.path = prop.getProperty("path");

			bridge = new HueBridge(Config.ip, Config.username);

			// Get Lights
			for (Light light : bridge.getLights()) {

				EpicGameLighting.comboBox_area_1.addItem(light.getName());
				EpicGameLighting.comboBox_area_2.addItem(light.getName());
				EpicGameLighting.comboBox_area_3.addItem(light.getName());
			}

		} catch (IOException e) {
			String message = "l:" + getLineNumber() + " - " + e.getMessage();
			EpicGameLighting.lblProcessError.setText(message);
		}

		// Schedule periodic task
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(hueRunnable, 0, Config.refreshrate,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Empty screenshot folder
	 */
	private static void emptyFolder() {

		try {

			String folder_path = Config.path;

			final File folder = new File(folder_path);
			String[] number_of_files = folder.list();

			if (number_of_files != null) {
				for (final File fileEntry : folder.listFiles()) {
					if (!fileEntry.isDirectory() && fileEntry.exists()) {
//						 fileEntry.delete(); // Delete file
					}
				}
			}
		} catch (Exception e) {
			String message = "l:" + getLineNumber() + " - " + e.getMessage();
			EpicGameLighting.lblProcessError.setText(message);
		}

		HueConnector.is_processing = false;
	}

	/**
	 * Configuration variables
	 */
	public static class Config {
		public static String username;
		public static String ip;
		public static String path;
		public static int refreshrate;
	}
}