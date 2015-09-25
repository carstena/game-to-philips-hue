package com.philips.lighting;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessor {

	public static Object[] getDominantColor(BufferedImage image,
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

	/**
	 * @see http://alienryderflex.com/hsp.html
	 * @param c
	 * @return
	 */
	private static int Brightness(Color c) {
		return (int) Math.sqrt(c.getRed() * c.getRed() * .241 + c.getGreen()
				* c.getGreen() * .691 + c.getBlue() * c.getBlue() * .068);
	}
}
