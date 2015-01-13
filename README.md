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

## What does it do?
The program scans the specified folder for the most recent image. Then it calculates the most dominant color of the image. After that it sends the color to three Philips Hue lightbulbs. This program deletes the created screenshots folder to keep things cleaned up. 

## Install
* Make sure you installed Java: http://java.com/
* Download and extract `EpicGameLightingforHue.zip`
* Create a file named `config.properties` (see example below)
* Run `EpicGameLightingforHue.jar`
* Select a lightbulb for each area
* Click 'Start'

## config.properties (file)

config.properties is the configuration file. Create a file with the name`config.properties` in the same folder as `EpicGameLightingforHue.jar`. 

* `username` username that is authorized with your bridge (see: http://developers.meethue.com/gettingstarted.html)
* `ip` ip address of your bridge (discover ip here: http://www.meethue.com/api/nupnp)
* `refreshrate` refresh rate in milliseconds. Every 500 miliseconds should be fine. Try some values, minimal 100 ms.
* `path` path to the folder with images (screenshots)

Linux / OS X example:
```
username=yourusername
ip=192.168.0.100
refreshrate=1000
path=/Users/username/screenshots/
```

Windows example:

```
username=yourusername
ip=192.168.0.100
refreshrate=1000
path=D:\\foldername\\screenshots\\
```