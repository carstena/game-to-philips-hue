Epic Game Lighting for Hue
==========================

Extend your game experience with Philips Hue. Send your colors from your game to Philips Hue. The program works in combination with a screencapture program like Bandicam, Fraps etc.

![Interface](/img/interface.png?raw=true "Interface")

## Screencapture programs
Setup the screencapture program to take a screenshot every 1 second (or less) and save it to a folder. Use the defined folder path in the config.properties file.

Suggested programs:

* [Bandicam](http://www.bandicam.com/)
* [Fraps](http://www.fraps.com/) - one screenshot per 1 second max.
* [Dxtory](http://exkode.com/dxtory-features-en.html)
* [OS X terminal command](http://www.trickyways.com/2010/01/how-to-set-timer-to-take-screenshot-on-mac-using-terminal/)

## What does it do?
The program scans the specified folder for the most recent image. Then it calculates the most dominant color of the image. After that it sends the color to three Philips Hue lightbulbs. This program deletes the created screenshots folder to keep things cleaned up. 

## How to use
* Make sure you installed Java: http://java.com/
* Download and extract `EpicGameLightingforHue.zip`
* Run `EpicGameLightingforHue.jar`
* Click the button 'Find New Bridges' to connect to your Bridge
* Click 'Select folder' and select your screenshot folder
* Click the button 'Process Screenshots'
* Click 'Start'