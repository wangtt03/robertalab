//Constants

#ifndef E
#define E 2.718281828459045
#endif

#ifndef GOLDEN_RATIO
#define GOLDEN_RATIO 1.61803398875
#endif


#ifndef SQRT2
#define SQRT2 1.41421356237
#endif

#ifndef SQRT1_2
#define SQRT1_2 0.707106781187
#endif

#ifndef INFINITY
#define INFINITY 0x7f800000
#endif

//Bluetooth functions and constants

#ifndef BT_CONN
#define BT_CONN 1
#endif

#ifndef OUTBOX
#define OUTBOX 5
#endif

#ifndef INBOX
#define INBOX 1
#endif

#ifndef OUT_MBOX
#define OUT_MBOX 1
#endif

#ifndef IN_MBOX
#define IN_MBOX 5
#endif

sub BTCheck(int conn){
  if (!BluetoothStatus(conn)==NO_ERR){
    TextOut(5,LCD_LINE2,"Error");
    Wait(1000);
    Stop(true);
  }
}

inline string bluetooth_get_msg(){
  TextOut(5,LCD_LINE1,"Slave receiving");
  string in = "NO MESSAGE";
  if (BTCheck(0) == true){
    SendResponseNumber(OUT_MBOX,0xFF); //unblock master
    while(true){
      if (ReceiveRemoteString(IN_MBOX,true,in) != STAT_MSG_EMPTY_MAILBOX) {
        TextOut(0,LCD_LINE3," ");
        TextOut(5,LCD_LINE3,in);
        SendResponseNumber(OUT_MBOX,0xFF);
      }
      Wait(10);
    }
  }
}

inline void bluetooth_send_msg(string msg , int connection){
  TextOut(10,LCD_LINE1,"Master sending");
  int ack = 0;
  TextOut(0,LCD_LINE3," ");
  TextOut(5,LCD_LINE3,msg);
  SendRemoteString(connection,OUTBOX,msg);
  until(ack==0xFF) {
    until(ReceiveRemoteNumber(INBOX,true,ack) == NO_ERR);
  }
  Wait(250);
}


//math Functions

inline int math_floor(float val) {
  int temp = val;
  return temp;
}
inline bool math_is_whole(float val){
  int intPart = val;
  return ((val - intPart) == 0);
}
inline float math_pow(float first_value, float second_value) {
  float result = 1;
  for (int i = 0; i < second_value; i++) {
    result = result * first_value;
  }
  return result;
}
inline float math_min(float first_value, float second_value) {
  if (first_value < second_value){
    return first_value;
  }
  else{
    return second_value;
  }
}
inline float math_max(float first_value, float second_value) {
  if (first_value > second_value){
    return first_value;
  }
  else{
    return second_value;
  }
}
inline bool math_prime(float number){
  for ( int i = 2; i <= sqrt(number); i++ ) {
    if ((number % i) == 0 ) {
      return false;
    }
    else{
      return true;
    }
  }
}
inline float math_ln(float val) {
  if (val > 1){
    float values[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 10000};
    float results[] = {0, 0.693147, 1.098612, 1.386294, 1.609438, 1.791759, 1.945910, 2.079442, 2.197225, 2.302585, 2.995732, 3.401197, 3.688879, 3.912023, 4.094345, 4.248495, 4.382027, 4.499810, 4.605170, 5.298317, 5.703782, 5.991465, 6.214608, 6.396930, 6.551080, 6.684612, 6.802395, 6.907755, 9.210340};
    int i = 1;
    while(values[i] < abs(val)){
      i++;
    }
    float result =  results[i - 1] + (abs(val) - values[i - 1]) * (results[i] - results[i - 1]) / (values[i] - values[i - 1]);
    if (val <= 100000){
      return result;
    }
    else{
      return 9.22;
    }
  }
  else if ((val > 0) && (val <= 1)){
    float summ = 0;
    for (int n = 1; n < 10; n++){
      summ += math_pow(-1, (n + 1)) * math_pow((val - 1), n)/n;
    }
    return summ;
  }
  else{
    TextOut(0, LCD_LINE1, "invalid value");
    Wait(1000);
    ClearScreen();
    return NULL;
  }
}
inline float math_log(float val) {
  return math_ln(val)/math_ln(2.71828);
}

inline float math_factorial(float val){
  float result = val;
  if (val == 0){
    return 1;
  }
  else{
    for (int i = 1; i < val; i++){
      result = result*(val - i);
    }
    return result;
  }
}

inline float math_sin(float val) {
  float angle = PI*val/180;
  float summ = 0;
  for (int n = 0; n < 10; n++){
    summ += math_pow(-1, n) * math_pow(angle, (2 * n + 1))/math_factorial(2 * n + 1);
  }
  return summ;
}

inline float math_cos(float val) {
  float angle = PI*val/180;
  float summ = 0;
  for (float n = 0; n < 10; n++){
    summ += (math_pow(-1, n)/math_factorial(2 * n)) * math_pow(angle, (2 * n));
  }
  return summ;
}

inline float math_tan(float val) {
  return math_sin(val)/math_cos(val);
}

inline float math_asin(float val) {
  if (abs(val) > 1){
    TextOut(0, LCD_LINE1, "invalid value");
    Wait(1000);
    ClearScreen();
    return NULL;
  }
  else{
    float summ = 0;
    for (float n = 0; n < 15; n++){
      summ += math_factorial(2 * n) * math_pow(val, (2 * n + 1)) / math_pow(4, n) / math_pow(math_factorial(n), 2)/(2* n + 1);
    }
    return summ * 180 / PI;
  }
}


inline float math_acos(float val) {
  if (abs(val) > 1){
    TextOut(0, LCD_LINE1, "invalid value");
    Wait(1000);
    ClearScreen();
    return NULL;
  }
  else{
    return 90 - math_asin(val);
  }
}

inline float math_atan(float val) {
  if (abs(val) > 1){
    float values[] = {1, sqrt(3), 2, 3, 0x7f800000};
    float results[] = {45, 60, 63.435, 71.565, 90};
    int i = 1;
    while(values[i] < abs(val)){
      i++;
    }
    float result =  results[i - 1] + (abs(val) - values[i - 1]) * (results[i] - results[i - 1]) / (values[i] - values[i - 1]);
    if (val > 0){
      return result;
    }
    else{
      return -result;
    }
  }
  else{
    float summ = 0;
    for (float n = 1; n < 15; n++){
      summ += math_pow(-1, (n - 1)) * math_pow(val, (2 * n - 1)) / (2 * n - 1);
    }
    return summ * 180 / PI;
  }
}

//numerical array functions

inline float array_sum(float arr[]) {
  float sum = 0;
  for(int i = 0; i < ArrayLen(arr); i++) {
    sum += arr[i];
  }
  return sum;
}
inline float array_min(float arr[]) {
  float min = arr[0];
  for(int i = 1; i < ArrayLen(arr); i++) {
    if (arr[i] < min){
      min = arr[i];
    }
  }
  return min;
}
inline float array_max(float arr[]) {
  float max = arr[0];
  for(int i = 1; i < ArrayLen(arr); i++) {
    if (arr[i] > max){
      max = arr[i];
    }
  }
  return max;
}
float array_mean(float arr[]) {
  float sum = 0;
  for(int i = 0; i < ArrayLen(arr); i++) {
    sum += arr[i];
  }
  return sum/ArrayLen(arr);
}
inline void array_insertion_sort(float &arr[]) {
  for (int i=1; i < ArrayLen(arr); i++) {
      int index = arr[i];
      int j = i;
      while (j > 0 && arr[j-1] > index){
           arr[j] = arr[j-1];
           j--;
      }
      arr[j] = index;
  }
}
inline float array_median(float arr[]) {
   int n = ArrayLen(arr);
   if ( n == 0 ) {
     return 0;
   }
   array_insertion_sort(arr);
   float median;
   if ( n % 2 == 0 ) {
      median = (arr[n/2] + arr[n / 2 - 1]) / 2;
   }
   else {
     median = arr[n / 2];
   }
   return median;
}
inline float array_standard_deviatioin(float arr[]) {
        int n = ArrayLen(arr);
        if ( n == 0 ) {
            return 0;
        }
        float variance = 0;
        float mean = array_mean(arr);
        for ( int i = 0; i < ArrayLen(arr); i++) {
            variance += math_pow(arr[i] - mean, 2);
        }
        variance /= n;
        return sqrt(variance);
}
inline float array_rand(float arr[]) {
  int arrayInd = ArrayLen(arr) * Random(100) / 100;
  return arr[arrayInd - 1];
}
inline float array_mode(float arr[]){
  array_insertion_sort(arr);
  float element = arr[0];
  float max_seen = element;
  int count = 1;
  int mode_count = 1;
  for (int i = 1; i < ArrayLen(arr); i++){
      if (arr[i] == element){
         count++;
         if (count > mode_count)
            {
            mode_count = count;
            max_seen = element;
        }
      }
      else {
        element = arr[i];
        count = 1;
    }
  }
  return max_seen;
}

// functions for unknown type arrays

inline int array_find_first_num(float arr[], float item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  }
  else{
    do{
      i++;
    } while((arr[i] != item) && (i != ArrayLen(arr)));
    return i;
  }
}
inline int array_find_last_num(float arr[], float item) {
  int i = 0;
  if (arr[ArrayLen(arr) - 1] == item){
    return ArrayLen(arr) - 1 - i;
  }
  else{
    do{
      i++;
    } while((arr[ArrayLen(arr) - 1 - i] != item)&&(i != 0));
      return ArrayLen(arr) - 1 - i;
  }
}

inline int array_find_first_str(string arr[], string item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  }
  else{
    do{
      i++;
    } while((arr[i] != item) && (i != ArrayLen(arr)));
    return i;
  }
}
inline int array_find_last_str(string arr[], string item) {
  int i = 0;
  if (arr[ArrayLen(arr) - 1] == item){
    return ArrayLen(arr) - 1 - i;
  }
  else{
    do{
      i++;
    } while((arr[ArrayLen(arr) - 1 - i] != item)&&(i != 0));
      return ArrayLen(arr) - 1 - i;
  }
}

inline int array_find_first_bool(bool arr[], bool item) {
  int i = 0;
  if (arr[0] == item){
    return i;
  }
  else{
    do{
      i++;
    } while((arr[i] != item) && (i != ArrayLen(arr)));
    return i;
  }
}
inline int array_find_last_bool(bool arr[], bool item) {
  int i = 0;
  if (arr[ArrayLen(arr) - 1] == item){
    return ArrayLen(arr) - 1 - i;
  }
  else{
    do{
      i++;
    } while((arr[ArrayLen(arr) - 1 - i] != item)&&(i != 0));
      return ArrayLen(arr) - 1 - i;
  }
}
