/*
  RobertaFunctions.cpp - additional library for interfacing with Bot'n Roll ONE Arduino Compatible 
  from www.botnroll.com that allows using some extra functions from https://www.open-roberta.org/
  Created by Evgeniya Ovchinnikova, August 30, 2016.
  Released into the public domain.
*/
#include <stdio.h>
#include <stdlib.h>
#include "SPI.h"
#include "BnrOneA.h" 
#include "BnrRescue.h" 
#include "RobertaFunctions.h" 
#include "Wire.h" 
#include "math.h"
#define SSPIN  2 
#define ADDRESS 0x60
#define MODULE_ADDRESS 0x2C
 
  

void RobertaFunctions::moveTime(int speedL,int speedR, double time)
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
	float toSeconds = 1000;
	one.move(speedL, speedR);
	delay(time*toSeconds);
    one.stop();
}

void RobertaFunctions::moveTimePID(int speedL,int speedR, double time)
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
	float toSeconds = 1000;
	one.movePID(speedL, speedR);
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
	port = port - 1;
	byte distances[3]={0,0,0};
	brm.i2cConnect(MODULE_ADDRESS);   
    brm.setModuleAddress(0x2C);      
	brm.readSonars(&distances[0],&distances[1],&distances[2]);
	return distances[port];
}

int RobertaFunctions::sonar()
{	
	BnrOneA one; 
	Serial.begin(9600);    
    one.spiConnect(SSPIN);  
    int echoPin = 7;
	int trigPin = 8; 
	int maximumRange = 200;
	int minimumRange = 0;  
	pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
    unsigned long duration;
    int distance;
    unsigned long tempo=micros();
    digitalWrite(trigPin, LOW); 
    delayMicroseconds(2); 
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);
    duration = pulseIn(echoPin, HIGH, 11640);
    delayMicroseconds(16000 - (micros()-tempo));
    distance = (int)(duration/58.2);
    if (distance >= maximumRange || distance <= minimumRange)
      distance=-1;
    return distance;
}

bool RobertaFunctions::buttonIsPressed(int button)
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
	if (one.readButton() == 0){
		return false;
	}
	else{
		return (one.readButton() == button || button == 123);
	}
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
{   BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);
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
	double r = colors[0];
	double g = colors[1];
	double b = colors[2];
	double min = fmin(r, fmin(g, b));
	double max = fmax(r, fmax(g, b));
	double delta = max - min;
	double h, s, v = max;
	
	v = max / 255.0 * 100.0;
	
	if (max != 0) {
		s = delta / max * 100;
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
	h = h * 60; 
	if (h < 0) {
		h += 360;
	}
	
	double hsv[3] = {h, s, v};
	if (hsv[2] <= 10) {
		color = "BLACK";
	}
	else if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 70 && hsv[2] > 50) {
		color = "RED";
	}
	else if (hsv[0] > 30 && hsv[0] < 70 && hsv[1] > 60 && hsv[2] > 50) {
		color = "YELLOW";
	}
	else if (hsv[0] < 50 && hsv[1] > 50 && hsv[1] < 100 && hsv[2] < 50) {
		color = "BROWN";
	}
	else if (hsv[1] < 10 && hsv[2] > 90) {
		color = "WHITE";
	}
	else if (hsv[0] > 70 && hsv[0] < 160 && hsv[1] > 40) {
		color = "GREEN";
	}
	else if (hsv[0] > 200 && hsv[0] < 250 && hsv[1] > 60 && hsv[2] > 50) {
		color = "BLUE";
	}
	else{
		color = "NONE";
	}
		
	Serial.print("hsv ");Serial.print(h);Serial.print("s");Serial.print(s);Serial.print("v");Serial.print(v);
	return color;
}

bool RobertaFunctions::infraredSensorObstacle(int port)
{   
	BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);  
	if (port == 1 && (one.obstacleSensors() == 1 || one.obstacleSensors() == 3)){
			return true;
	}
	else if (port == 2 && (one.obstacleSensors() == 2 || one.obstacleSensors() == 3)){
			return true;
	}
        else if (port == 3 && one.obstacleSensors() == 3){
                        return true;
        }
	else{
		return false;
	}
}

bool RobertaFunctions::infraredSensorPresence(int port)
{   
	BnrOneA one;
	Serial.begin(9600);
    one.spiConnect(SSPIN);  
	if (port == 1 && (one.readIRSensors() == 1 || one.readIRSensors() == 3)){
			return true;
	}
	else if (port == 2 && (one.readIRSensors() == 2 || one.readIRSensors() == 3)){
			return true;
	}
	else{
		return false;
	}
}

float RobertaFunctions::readBearing()
{
	Wire.begin();
    byte highByte, lowByte;    
    Wire.beginTransmission(ADDRESS);           //start communication with CMPS10
    Wire.write(2);                             //Send the register we wish to start reading from
    Wire.endTransmission();

    Wire.requestFrom(ADDRESS, 2);              // Request 4 bytes from CMPS10
    while(Wire.available() < 2);               // Wait for bytes to become available
    highByte = Wire.read();
    lowByte = Wire.read();
   
return (float)((highByte<<8)+lowByte)/10;
}

char RobertaFunctions::readRoll()
{   Wire.begin();
    char roll;                 // Store  roll values of CMPS10, chars are used because they support signed value
    Wire.beginTransmission(ADDRESS);           //start communication with CMPS10
    Wire.write(5);                             //Send the register we wish to start reading from
    Wire.endTransmission();

    Wire.requestFrom(ADDRESS, 1);              // Request 4 bytes from CMPS10
    while(Wire.available() < 1);               // Wait for bytes to become available
    roll = Wire.read();
	return roll;
}

char RobertaFunctions::readPitch()
{
	Wire.begin();
	char pitch;                // Store pitch values of CMPS10, chars are used because they support signed value
    Wire.beginTransmission(ADDRESS);           //start communication with CMPS10
    Wire.write(4);                             //Send the register we wish to start reading from
    Wire.endTransmission();

    Wire.requestFrom(ADDRESS, 1);              // Request 4 bytes from CMPS10
    while(Wire.available() < 1);               // Wait for bytes to become available
    pitch = Wire.read();

	return pitch;
}

int RobertaFunctions::randomIntegerInRange(int val1, int val2){
	int min = fmin(val1, val2);
	int max = fmax(val1, val2);
	return min + (rand()%(min - max));
}

float RobertaFunctions::randomFloat(){
	return (float)rand()/(float)RAND_MAX;
}

double RobertaFunctions::clamp(double val, double min, double max){
	return fmin(fmax(val, min), max);
}

bool RobertaFunctions::isPrime(double number) {
    if ((fmod(number, 2) == 0) || (number == 1)) return false;
    //if not, then just check the odds
    for(int i = 3; i * i <= number; i += 2) {
        if(fmod(number, i) == 0)
            return false;
    }
    return true;
}

bool RobertaFunctions::isWhole(double val){
  int intPart = val;
  return ((val - intPart) == 0);
}

int RobertaFunctions::arrFindFirst(int len, double arr[], double item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  } else {
    do {
      i++;
    } while((arr[i] != item) && (i != len));
    return i;
  }
}

int RobertaFunctions::arrFindFirst(int len, bool arr[], bool item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  } else {
    do {
      i++;
    } while((arr[i] != item) && (i != len));
    return i;
  }
}

int RobertaFunctions::arrFindFirst(int len, String arr[], String item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  } else {
    do {
      i++;
    } while((arr[i] != item) && (i != len));
    return i;
  }
}


int RobertaFunctions::arrFindLast(int len, double arr[], double item) {
  int i = 0;
  if (arr[len - 1] == item){
    return len - 1 - i;
  } else {
    do {
      i++;
    } while((arr[len - 1 - i] != item)&&(i != 0));
      return len - 1 - i;
  }
}

int RobertaFunctions::arrFindLast(int len, bool arr[], bool item) {
  int i = 0;
  if (arr[len - 1] == item){
    return len - 1 - i;
  } else {
    do {
      i++;
    } while((arr[len - 1 - i] != item)&&(i != 0));
      return len - 1 - i;
  }
}
int RobertaFunctions::arrFindLast(int len, String arr[], String item) {
  int i = 0;
  if (arr[len - 1] == item){
    return len - 1 - i;
  } else {
    do {
      i++;
    } while((arr[len - 1 - i] != item)&&(i != 0));
      return len - 1 - i;
  }
}




double RobertaFunctions::arrSum(int len, double arr[]) {
  float sum = 0;
  for(int i = 0; i < len; i++) {
    sum += arr[i];
  }
  return sum;
}
double RobertaFunctions::arrMin(int len, double arr[]) {
  double min = arr[0];
  for(int i = 1; i < len; i++) {
    if (arr[i] < min){
      min = arr[i];
    }
  }
  return min;
}
double RobertaFunctions::arrMax(int len, double arr[]) {
  double max = arr[0];
  for(int i = 1; i < len; i++) {
    if (arr[i] > max){
      max = arr[i];
    }
  }
  return max;
}
double RobertaFunctions::arrMean(int len, double arr[]) {
  double sum = 0;
  for(int i = 0; i < len; i++) {
    sum += arr[i];
  }
  return sum/len;
}
void RobertaFunctions::arrInsertionSort(int len,  double *arr) {
  for (int i=1; i < len; i++) {
    int index = arr[i];
    int j = i;
    while (j > 0 && arr[j-1] > index) {
      arr[j] = arr[j-1];
      j--;
    }
    arr[j] = index;
  }
}
double RobertaFunctions::arrMedian(int len, double arr[]) {
  if (len == 0) {
    return 0;
  }
  arrInsertionSort(len, arr);
  double median;
  if (len % 2 == 0) {
    median = (arr[len / 2] + arr[len / 2 - 1]) / 2;
  } else {
    median = arr[len / 2];
  }
  return median;
}

double RobertaFunctions::arrStandardDeviatioin(int len, double arr[]) {
  if (len == 0) {
    return 0;
  }
  double variance = 0;
  double mean = arrMean(len, arr);
  for (int i = 0; i < len; i++) {
    variance += pow(arr[i] - mean, 2);
  }
  variance /= len;
  return sqrt(variance);
}

double RobertaFunctions::arrRand(int len, double arr[]) {
  int arrayInd = len * (randomFloat()*100)/100;
  return arr[arrayInd - 1];
}

double RobertaFunctions::arrMode(int len, double arr[]){
  arrInsertionSort(len, arr);
  double element = arr[0];
  double max_seen = element;
  int count = 1;
  int mode_count = 1;
  for (int i = 1; i < len; i++){
    if (arr[i] == element){
      count++;
      if (count > mode_count) {
        mode_count = count;
        max_seen = element;
      }
    } else {
      element = arr[i];
      count = 1;
    }
  }
  return max_seen;
}
