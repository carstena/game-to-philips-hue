Game to Philips Hue
===================

Extend your game experience with Philips Hue. This is a program for Philips Hue that grabs of an image (screenshot) from a directory. It's build to work in combination with a screencapture programs like Bandicam, Fraps etc.

## Screencapture programs
Setup the screencapture program to take a screenshot every 1 second (or less) and save it to a folder. Use the defined folder path in the config.properties file.

Available programs:

* [Bandicam](http://www.bandicam.com/)
* [Fraps](http://www.fraps.com/) - one screenshot per 1 second max.
* [Dxtory](http://exkode.com/dxtory-features-en.html)

## What does it do?
The program reads the specified folder for the latest image. Then it calculates the average color of the image. After that it sends the color to all the Philips Hue lights. This program deletes the created screenshots folder to keep things cleaned up. 

## Install
* Make sure you installed Java: http://java.com/
* Download and extract `game-to-hue.zip`
* Create a file named `config.properties` (see example below)
* Run `game-to-hue.jar`

## config.properties (file)

config.properties is the configuration file. Create a file with the name`config.properties` in the same folder as `game-to-hue.jar`. 

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