# Watchman
### A basic window watcher android accessibility sevice 

## Features
* Dismiss Application not responding (ANR) popups
* Extract text dump of all non application types windows visited 

## How to start watching
* Start
```
adb shell settings put secure enabled_accessibility_services com.nurdtechie98.watchman/com.nurdtechie98.watchman.Watcher
```
* Stop
```
adb shell am force-stop com.nurdtechie98.watchman
```

## Extract dump file 
```
adb shell cat /storage/emulated/0/Documents/watchman_dump.txt
```
Sample dump file
```
===========19700101T000000Asia/Kolkata(0,0,0,-1,-19800)===========
 Text: Cast screen to device  Content-Description: null
  Text: Searching for devices…  Content-Description: null
 Text: Settings  Content-Description: null
===========19700101T000000Asia/Kolkata(0,0,0,-1,-19800)===========
 Text: Cast screen to device  Content-Description: null
  Text: Searching for devices…  Content-Description: null
 Text: Settings  Content-Description: null
```