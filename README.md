Game to Philips Hue
===================

This is a program for Philips Hue that grabs of an image from a directory. It's build to work in combination with the screencapture program Fraps (for games).

## Fraps
Setup Fraps to take a screenshot every 1 second and save it to a folder. Use the folder path in config.properties.

## What does it do?
The program reads the specified folder for the latest image. Then it calculates the average color of the image. After that it sends the color to all the Philips Hue lights. This program deletes the created screenshots folder to keep things cleaned up. 

## Usage
* Download and extract `game-to-hue.zip`
* Create a file named `config.properties` (see example below)
* Run `game-to-hue.jar`
* `username` username that is authorized with your bridge
* `ip` ip address of your bridge
* `refreshrate` refresh rate in milliseconds. Every 1000 miliseconds should be fine.
* `path` path to the folder with images (screenshots)

## config.properties
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