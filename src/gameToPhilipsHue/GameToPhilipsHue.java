package gameToPhilipsHue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;

import nl.q42.jue.FullLight;
import nl.q42.jue.HueBridge;
import nl.q42.jue.Light;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;

public class GameToPhilipsHue {

	public static boolean is_running = false;
	public static JLabel label1;

	static Runnable hueRunnable = new Runnable() {
		
		public void run() {

			try {

				long startTime = System.currentTimeMillis();

				int red = 0;
				int green = 0;
				int blue = 0;

				GameToPhilipsHue.is_running = true;

				// Read images
				String folder_path = Config.path;
				final File folder = new File(folder_path);

				String path = null;

				int number_of_files = folder.list().length;
				int i = 0;

				for (final File fileEntry : folder.listFiles()) {
					if (!fileEntry.isDirectory()) {

						if (i + 1 < number_of_files && number_of_files > 1) {
							fileEntry.delete();
						}

						i++;

						path = folder_path + fileEntry.getName();
					}
				}

				if (number_of_files > 0) {

					java.net.URL url = new File(path).toURI().toURL();
					BufferedImage image = ImageIO.read(url);

					// Average color
					// getAverageColor(image);

					// Dominant color
					Color rgbcolor = getDominantColor(image, true);

					// Convert RGB to XY
					float xyColor[] = PHUtilities.calculateXYFromRGB(
							rgbcolor.getRed(), rgbcolor.getGreen(),
							rgbcolor.getBlue(), "LCT001"); // @todo modelid must be a var

					if (rgbcolor.getRed() > 0 && rgbcolor.getGreen() > 0
							&& rgbcolor.getBlue() > 0) {

						red = rgbcolor.getRed();
						green = rgbcolor.getGreen();
						blue = rgbcolor.getBlue();

						// Set lights color
						HueBridge bridge = new HueBridge(Config.ip,
								Config.username);
						// Groups commands have a maximum of 1 per second
						// nl.q42.jue.Group all = bridge.getAllGroup();
						StateUpdate update = new StateUpdate().turnOn().setXY(
								xyColor[0], xyColor[1]);
						// bridge.setGroupState(all, update);

						// Set lights. Lights commands a have a max of around 10
						// commands per second
						for (Light light : bridge.getLights()) {
							FullLight fullLight = bridge.getLight(light);
							bridge.setLightState(fullLight, update);
						}
					}
				}

				GameToPhilipsHue.is_running = false;

				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;

				label1.setText("<html>&nbsp;&nbsp;" + duration
						+ " milliseconds<br>&nbsp;&nbsp;R:" + red + " G:"
						+ green + " B:" + blue + "<br></html>");

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}

		// private Color getAverageColor(BufferedImage image) {
		//
		// long redBucket = 0;
		// long greenBucket = 0;
		// long blueBucket = 0;
		// long pixelCount = 0;
		//
		// // Loop trough all the pixels of the image
		// for (int x = 0; x < image.getWidth(); x = x + 5) {
		// for (int y = 0; y < image.getHeight(); y = y + 5) {
		//
		// Color c = new Color(image.getRGB(x, y));
		// float af[] = Color.RGBtoHSB(c.getRed(), c.getGreen(),
		// c.getBlue(), null);
		//
		// // Ignore darker values (sat / bri)
		// if ((af[1] * 100) > 10 && (af[2] * 100) > 20) {
		// redBucket += c.getRed();
		// greenBucket += c.getGreen();
		// blueBucket += c.getBlue();
		//
		// }
		//
		// pixelCount++;
		// }
		// }
		//
		// // Convert Long into Integer
		// int r = (int) redBucket / (int) pixelCount;
		// int g = (int) greenBucket / (int) pixelCount;
		// int b = (int) blueBucket / (int) pixelCount;
		//
		// Color averageColor = new Color(r, g, b);
		//
		// return averageColor;
		// }

		private Color getDominantColor(BufferedImage image,
				boolean applyThreshold) {

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

			int height = image.getHeight();
			int width = image.getWidth();

			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {

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
			if (maxBin < 0)
				return new Color(0, 0, 0, 0);

			// Return a color with the average hue/saturation/value of
			// the bin with the most colors.
			hsv[0] = sumHue[maxBin] / colorBins[maxBin];
			hsv[1] = sumSat[maxBin] / colorBins[maxBin];
			hsv[2] = sumVal[maxBin] / colorBins[maxBin];

			int rgb = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
			Color rgbcolor = new Color(rgb);

			return rgbcolor;
		}
	};

	public static void main(String[] args) {

		Properties prop = new Properties();

		try {
			// load a properties file
			prop.load(new FileInputStream("config.properties"));

			// get the property value and save it to Config
			Config.username = prop.getProperty("username");
			Config.ip = prop.getProperty("ip");
			Config.refreshrate = Integer.parseInt(prop
					.getProperty("refreshrate"));
			Config.path = prop.getProperty("path");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		if (!GameToPhilipsHue.is_running) {

			appGui();

			// Schedule periodic task
			ScheduledExecutorService executor = Executors
					.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(hueRunnable, 0, Config.refreshrate,
					TimeUnit.MILLISECONDS);
		}
	}

	public static void appGui() {
		JFrame guiFrame = new JFrame("App");

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Game to Philips Hue");
		guiFrame.setSize(400, 250);

		// Add label

		label1 = new JLabel("hello");
		guiFrame.add(label1);

		// make sure the JFrame is visible
		guiFrame.setVisible(true);
	}

	public static class Config {
		public static String username;
		public static String ip;
		public static String path;
		public static int refreshrate;
	}
}
