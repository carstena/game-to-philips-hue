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

import nl.q42.jue.HueBridge;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;

public class GameToPhilipsHue {

	public static boolean is_running = false;

	static Runnable hueRunnable = new Runnable() {
		private float[] anArrays;

		@Override
		public void run() {
			try {

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

					// Avarage color
					getAvarageColor(image);
					
					// Dominant color
					Color rgbcolor = getDominantColor(image);

					// Convert RGB to XY
					float[] xyColor = rgb_to_xy(rgbcolor);

					// Set lights color
					HueBridge bridge = new HueBridge(Config.ip, Config.username);
					nl.q42.jue.Group all = bridge.getAllGroup();
					StateUpdate update = new StateUpdate().turnOn().setXY(
							xyColor[0], xyColor[1]);
					bridge.setGroupState(all, update);

					// for (Light light : bridge.getLights()) {
					// FullLight fullLight = bridge.getLight(light);
					// bridge.setLightState(fullLight, update);
					// }

					// end New
				}

				GameToPhilipsHue.is_running = false;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}

		private float[] rgb_to_xy(Color averageColor) {
			// Get the RGB values from your color object and convert them to
			// be between 0 and 1
			float red = (float) (averageColor.getRed() / 225.0);
			float green = (float) (averageColor.getGreen() / 225.0);
			float blue = (float) (averageColor.getBlue() / 225.0);

			// Apply a gamma correction to the RGB values, which makes the
			// color more vivid and more the like the color displayed on the
			// screen of your device.
			// This gamma correction is also applied to the screen of your
			// computer or phone, thus we need this to create the same color
			// on the light as on screen.
			// This is done by the following formulas:
			float red1 = (float) ((red > 0.04045f) ? Math.pow((red + 0.055f)
					/ (1.0f + 0.055f), 2.4f) : (red / 12.92f));
			float green1 = (float) ((green > 0.04045f) ? Math.pow(
					(green + 0.055f) / (1.0f + 0.055f), 2.4f)
					: (green / 12.92f));
			float blue1 = (float) ((blue > 0.04045f) ? Math.pow((blue + 0.055f)
					/ (1.0f + 0.055f), 2.4f) : (blue / 12.92f));

			// Convert the RGB values to XYZ using the Wide RGB D65
			// conversion formula
			float X = red1 * 0.649926f + green1 * 0.103455f + blue1 * 0.197109f;
			float Y = red1 * 0.234327f + green1 * 0.743075f + blue1 * 0.022598f;
			float Z = red1 * 0.0000000f + green1 * 0.053077f + blue1
					* 1.035763f;

			float x = X / (X + Y + Z);
			float y = Y / (X + Y + Z);

			// allocates memory for 10 integers
			anArrays = new float[2];

			anArrays[0] = x;
			anArrays[1] = y;

			return anArrays;
		}

		private Color getAvarageColor(BufferedImage image) {

			long redBucket = 0;
			long greenBucket = 0;
			long blueBucket = 0;
			long pixelCount = 0;

			// Loop trough all the pixels of the image
			for (int x = 0; x < image.getWidth(); x = x + 5) {
				for (int y = 0; y < image.getHeight(); y = y + 5) {

					Color c = new Color(image.getRGB(x, y));
					float af[] = Color.RGBtoHSB(c.getRed(), c.getGreen(),
							c.getBlue(), null);

					// Ignore darker values (sat / bri)
					if ((af[1] * 100) > 10 && (af[2] * 100) > 20) {
						redBucket += c.getRed();
						greenBucket += c.getGreen();
						blueBucket += c.getBlue();

					}

					pixelCount++;
				}
			}

			// Convert Long into Integer
			int r = (int) redBucket / (int) pixelCount;
			int g = (int) greenBucket / (int) pixelCount;
			int b = (int) blueBucket / (int) pixelCount;

			System.out.println("c");

			Color averageColor = new Color(r, g, b);

			System.out.println("RGB");
			System.out.println(averageColor);

			return averageColor;
		}

		private Color getDominantColor(BufferedImage image) {

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

			// Return a color with the average hue/saturation/value of
			// the bin with the most colors.
			hsv[0] = sumHue[maxBin] / colorBins[maxBin];
			hsv[1] = sumSat[maxBin] / colorBins[maxBin];
			hsv[2] = sumVal[maxBin] / colorBins[maxBin];

			int rgb = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
			Color rgbcolor = new Color(rgb);

			System.out.println("HSV");
			System.out.println(rgbcolor);

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
		JFrame guiFrame = new JFrame();

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Game to Philips Hue");
		guiFrame.setSize(400, 250);

		// Add label
		JLabel label1;
		label1 = new JLabel("<html>Running on bridge: " + Config.ip + " "
				+ Config.path + "<br>  every " + Config.refreshrate / 1000
				+ "s</html>");
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
