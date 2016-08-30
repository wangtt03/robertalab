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

