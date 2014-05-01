# Game to Philips Hue changelog

## 1.1
Changes:

	- Added a new interface
	- Added brightness calculation
	- Three lightbulbs are now supported. Screencolors are calculated in three parts (vertically).

## 1.0
Changes:

	- Replaced the function getAverageColor with getDominantColor. Which improves the matching of the color of the screenshot image.
	- Replaced the rgb to xy function (now using PHUtilities.calculateXYFromRGB from the PhilipsHueSDK)