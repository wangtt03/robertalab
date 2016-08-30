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
#define SSPIN  2 
 
//BnrRescue brm; 

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
