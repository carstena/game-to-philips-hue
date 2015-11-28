# Game to Philips Hue changelog

## 2.0
    - New interface
	- Fix errors with darker colors
    - Fix image reading errors

## 1.2
Changes:

  - Better error messages
  - Interface improvements
  - Added color indicators for each light

## 1.1
Changes:

	- Added a new interface
	- Added brightness calculation
	- Three lightbulbs are now supported. Screencolors are calculated in three parts (vertically).

## 1.0
Changes:

	- Replaced the function getAverageColor with getDominantColor. Which improves the matching of the color of the screenshot image.
	- Replaced the rgb to xy function (now using PHUtilities.calculateXYFromRGB from the PhilipsHueSDK)