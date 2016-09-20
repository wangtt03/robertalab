/*
  RobertaFunctions.cpp - additional library for interfacing with Bot'n Roll ONE Arduino Compatible 
  from www.botnroll.com that allows using some extra functions from https://www.open-roberta.org/
  Created by Evgeniya Ovchinnikova, August 30, 2016.
  Released into the public domain.
*/

#ifndef RobertaFunctions_h
#define RobertaFunctions_h

#include <stdio.h>
#include <stdlib.h>
#include "Arduino.h"
#include "BnrOneA.h" 
#include "BnrRescue.h" 
#include "Wire.h" 


class RobertaFunctions
{
  public:   
		void moveTime(int speedL,int speedR, double time);
		void moveTimePID(int speedL,int speedR, double time);
		void lcdClear();
		int ultrasonicDistance(int port);
		bool buttonIsPressed(int button);
		byte *colorSensorRGB(byte colors[],int port);
		int colorSensorLight(byte colors[], int port);
		String colorSensorColor(byte colors[], int port);
		bool infraredSensorObstacle(int port);
		bool infraredSensorPresence(int port);
		float readBearing();
		char readRoll();
		char readPitch();
		int randomIntegerInRange(int val1, int val2);
		float randomFloat();
};
#endif

