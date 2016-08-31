/*
  RobertaFunctions.cpp - additional library for interfacing with Bot'n Roll ONE Arduino Compatible 
  from www.botnroll.com that allows using some extra functions from https://www.open-roberta.org/
  Created by Evgeniya Ovchinnikova, August 30, 2016.
  Released into the public domain.
*/

#include "SPI.h"
#include "BnrOneA.h" 
#include "BnrRescue.h" 
#include "RobertaFunctions.h" 
#include "Wire.h" 
#include "math.h"
#define SSPIN  2 
#define MODULE_ADDRESS 0x2C
 

void RobertaFunctions::moveTime(int speedL,int speedR, long time)
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
	float toSeconds = 1000;
	one.move(speedL, speedR);
	delay(time*toSeconds);
    one.stop();
}

void RobertaFunctions::lcdClear()
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
	one.lcd1("                   ");
	one.lcd2("                   ");
}

int RobertaFunctions::ultrasonicDistance(int port)
{   
	BnrRescue brm; 
	byte distances[3]={0,0,0};
	brm.i2cConnect(MODULE_ADDRESS);   
    brm.setModuleAddress(0x2C);      
	brm.readSonars(&distances[0],&distances[1],&distances[2]);
	return distances[port];
}

byte *RobertaFunctions::colorSensorRGB(byte colors[], int port)
{   
	BnrRescue brm; 
	brm.i2cConnect(MODULE_ADDRESS);   
    brm.setModuleAddress(0x2C);      
	if (port == 1){
		brm.readRgbL(&colors[0],&colors[1],&colors[2]);
	}
	else{
		brm.readRgbR(&colors[0],&colors[1],&colors[2]);
	}
	return colors;
}


int RobertaFunctions::colorSensorLight(byte colors[], int port)
{   
	BnrRescue brm; 
	brm.i2cConnect(MODULE_ADDRESS);   
    brm.setModuleAddress(0x2C);    
	int light;	
	if (port == 1){
		brm.readRgbL(&colors[0],&colors[1],&colors[2]);
	}
	else{
		brm.readRgbR(&colors[0],&colors[1],&colors[2]);
	}
	light = (colors[0] + colors[1] + colors[2]) / 3 / 2.55;
	return light;
}



String RobertaFunctions::colorSensorColor(byte colors[], int port)
{   
	BnrRescue brm; 
	brm.i2cConnect(MODULE_ADDRESS);   
    brm.setModuleAddress(0x2C);    
	String color;	
	if (port == 1){
		brm.readRgbL(&colors[0],&colors[1],&colors[2]);
	}
	else{
		brm.readRgbR(&colors[0],&colors[1],&colors[2]);
	}
	int r = colors[0];
	int g = colors[1];
	int b = colors[2];
	int min = fmin(r, fmin(g, b));
	int max = fmax(r, fmax(g, b));
	int delta = max - min;
	int h, s, v = max;
	
	v = floor(max / 255 * 100);
	
	if (max != 0) {
		s = floor(delta / max * 100);
	} else {
		h, s, v = 0;
	}
	if (r == max) {
		h = (g - b) / delta;
	} else if (g == max) {
		h = 2 + (b - r) / delta;
	} else {
		h = 4 + (r - g) / delta;
	}
	h = floor(h * 60); 
	if (h < 0) {
		h += 360;
	}
	
	int hsv[3] = {h, s, v};
	if (hsv[2] <= 10) {
		color = "BLACK";
	}
	else if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 90 && hsv[2] > 50) {
		color = "RED";
	}
	else if (hsv[0] > 40 && hsv[0] < 70 && hsv[1] > 90 && hsv[2] > 50) {
		color = "YELLOW";
	}
	else if (hsv[0] < 50 && hsv[1] > 50 && hsv[1] < 100 && hsv[2] < 50) {
		color = "BROWN";
	}
	else if (hsv[1] < 10 && hsv[2] > 90) {
		color = "WHITE";
	}
	else if (hsv[0] > 70 && hsv[0] < 160 && hsv[1] > 80) {
		color = "GREEN";
	}
	else if (hsv[0] > 200 && hsv[0] < 250 && hsv[1] > 90 && hsv[2] > 50) {
		color = "BLUE";
	}
	else{
		color = "NONE";
	}
	return color;
}

bool RobertaFunctions::infraredSensorObstacle(int port)
{   
	BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);  
	if (port == 1 || one.obstacleSensors() == 1 || one.obstacleSensors() == 3){
			return true;
	}
	else if (port == 2 || one.obstacleSensors() == 2 || one.obstacleSensors() == 3){
			return true;
	}
	else{
		return false;
	}
}