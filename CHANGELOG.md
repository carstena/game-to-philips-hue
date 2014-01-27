# Game to Philips Hue changelog

## 1.0 beta
Changes:

	- Replaced the function getAverageColor with getDominantColor. Which improves the matching of the color of the screenshot image.
	- Replaced the rgb to xy function (now using PHUtilities.calculateXYFromRGB from the PhilipsHueSDK)