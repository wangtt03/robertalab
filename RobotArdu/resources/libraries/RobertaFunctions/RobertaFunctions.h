/*
  RobertaFunctions.cpp - additional library for interfacing with Bot'n Roll ONE Arduino Compatible 
  from www.botnroll.com that allows using some extra functions from https://www.open-roberta.org/
  Created by Evgeniya Ovchinnikova, August 30, 2016.
  Released into the public domain.
*/

#ifndef RobertaFunctions_h
#define RobertaFunctions_h

#include "Arduino.h"
#include "BnrOneA.h" 
#include "BnrRescue.h" 
#include "Wire.h" 


class RobertaFunctions
{
  public:   
		void moveTime(int speedL,int speedR, long time);
		void lcdClear();
};
#endif

