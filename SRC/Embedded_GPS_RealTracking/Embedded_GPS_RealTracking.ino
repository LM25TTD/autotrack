#include <SoftwareSerial.h>
#include <SM5100B_GPRS.h>
#include <TinyGPS.h>
#include <EEPROM.h>
#include <avr/pgmspace.h>

#define DEBUG_MESSAGES

#define MAX_NUM_ERRORS 10
#define GSM_TX_PIN 2
#define GSM_RX_PIN 3
#define GPS_TX_PIN 4
#define GPS_RX_PIN 5
#define GPS_T_OUT 5000

#define TIME_TO_SEND 5000

#define REBOOT_PIN 9
#define STATE_PERM_DATA_ADDR 0
#define STATE_UNBLOCKED LOW 
#define STATE_BLOCKED HIGH
#define IO_PIN  12
#define ALARM_STATUS_PIN 10

#define BLOCK_MESSAGE "300"
#define UNBLOCK_MESSAGE "200"


TinyGPS gps;
SM5100B_GPRS cell(GSM_TX_PIN, GSM_RX_PIN);  
SoftwareSerial gpsCommunicator(GPS_TX_PIN, GPS_RX_PIN);


String USER_AGENT = "Mozilla/5.0";
String HOST = "lm25ttd.no-ip.org";
int PORT = 8229;

String apn = "tim.br";
String user = "tim";
String password = "tim";
String path = "/AutoTrack_WebManager/api/embedded";
String responseFromServer = "";
byte pdpId = 1;
byte connectionId = 1;

byte numOfErrors=0;

byte moduleState = STATE_UNBLOCKED;

float actualLatitude = 0.0f;
float actualLongitude = 0.0f;

boolean attachNetwork()
{
  if (cell.attachGPRS())
  {
#ifdef DEBUG_MESSAGES
    Serial.println(F("GPRS"));
#endif
    if(cell.setUpPDPContext(&pdpId, &apn, &user, &password))
    {
#ifdef DEBUG_MESSAGES
      Serial.println(F("SetPDP"));
#endif
      if(cell.activatePDPContext(&pdpId))
      {
#ifdef DEBUG_MESSAGES
        Serial.println(F("ActivePDP"));
#endif
      return (true);
      }
    }
  }
  return (false);
}

boolean createSocket()
{
  if(cell.connectToHostTCP(&connectionId, &HOST, &PORT))
  {
#ifdef DEBUG_MESSAGES
    Serial.println(F("Connect"));
#endif
    if(cell.configureDisplayFormat(&connectionId, GSM_SHOW_ASCII, GSM_NOT_ECHO_RESPONSE))
    {
#ifdef DEBUG_MESSAGES
      Serial.println(F("Display"));      
#endif
      return (true);
    }
  }
  return false; 
}


void rebootGSMProcedure()
{
  digitalWrite(REBOOT_PIN, LOW);
}

void signalizeError()
{
  numOfErrors++;
  if(numOfErrors>MAX_NUM_ERRORS)
  {
    rebootGSMProcedure();
  } 
}

boolean sendMessageToServer(String *request, byte *connectionId)
{     
  if(cell.checkSocketStatusTCP())
  {
    numOfErrors=0;
#ifdef DEBUG_MESSAGES
    Serial.println(F("Socket is Open"));
#endif
    if(cell.sendData(request, connectionId))
    {
#ifdef DEBUG_MESSAGES
      Serial.println(F("SendData"));
#endif
      delay(15000);
      responseFromServer = cell.getServerResponse(connectionId);
      cell.cleanCounters();
      return (true);
    }
    else
    {
      signalizeError();
#ifdef DEBUG_MESSAGES
      Serial.println(F("FAIL!!! SendData"));
#endif
      return (false);
    }
  }
  else
  {
#ifdef DEBUG_MESSAGES
    Serial.println(F("Fail on Socket Status!"));
#endif
    while(!cell.dataStart(connectionId))
    {  
      signalizeError();
    }  
    signalizeError();
    cell.cleanCounters();
    return (false);    
  }
  delay(100); 
}


boolean doPost(byte *connectionId, String *path, float *latitude, float *longitude)
{
  String request = "";
  String parameters = buildJsonContent(latitude, longitude);
  request += "POST ";
  request += *path;
  request += " HTTP/1.1\nHost: ";
  request += HOST;
  request += "\nConnection: keep-alive";
  request += "\nUser-Agent: ";
  request += (USER_AGENT+"\n");
  request += "Accept: application/json\n";
  request += "Content-Length: ";
  request += parameters.length();
  request += "\n";
  request += "Content-Type: application/json\n\n";
  request += parameters;

  return sendMessageToServer(&request, connectionId);  
}

String buildJsonContent(float *latitude, float *longitude)
{
  String jsonContent= "";
  jsonContent = "{";
  jsonContent += "\"idModule\":\"12345678\",\"codAccess\":\"25897\",";

  char latConverted[10] = "";

  jsonContent += "\"latitude\":\"";
  dtostrf(*latitude, 1, 4, latConverted);
  jsonContent+= latConverted;

  char longConverted[10] = "";

  jsonContent += "\",\"longitude\":\"";
  dtostrf(*longitude, 1, 4, longConverted);
  jsonContent += longConverted;
  jsonContent += "\",\"alarm\":\"";
  jsonContent += digitalRead(ALARM_STATUS_PIN); 
  jsonContent += "\"}";

  return jsonContent;
}

static bool feedGps()
{
  unsigned long checker = millis();
  while (true)
  {
    if (gpsCommunicator.available() && gps.encode(gpsCommunicator.read()))
      return true;
    if((millis()-checker)>GPS_T_OUT)
      return false;  
  }
}  


void setup()
{
  digitalWrite(REBOOT_PIN, HIGH);
  pinMode(REBOOT_PIN, OUTPUT);
  
  pinMode(IO_PIN, OUTPUT);
  moduleState = EEPROM.read(STATE_PERM_DATA_ADDR);
  digitalWrite(IO_PIN, moduleState);

  pinMode(ALARM_STATUS_PIN, INPUT);
#ifdef DEBUG_MESSAGES  
  Serial.begin(9600);
#endif  
  cell.initializeModule(9600);
  attachNetwork();
  createSocket();
  gpsCommunicator.begin(4800);
#ifdef DEBUG_MESSAGES  
  Serial.println(F("Setup Ok!"));
#endif
}

void treatServerResponse(String *response)
{
  if (response->substring(17, 20)==BLOCK_MESSAGE)
  {
    if(moduleState!=STATE_BLOCKED)
    {
      moduleState=STATE_BLOCKED;
      EEPROM.write(STATE_PERM_DATA_ADDR, moduleState);
      digitalWrite(IO_PIN, moduleState);
    }
  }else{
    if(moduleState!=STATE_UNBLOCKED)
    {
      moduleState=STATE_UNBLOCKED;
      EEPROM.write(STATE_PERM_DATA_ADDR, STATE_UNBLOCKED);
      digitalWrite(IO_PIN, moduleState);
    }
  }  
}

void loop()
{ 
  gpsCommunicator.listen();
  delay(5000);

  if(feedGps())
    gps.f_get_position(&actualLatitude, &actualLongitude);  

  if((actualLatitude==0.0f) || (actualLongitude==0.0f))
    return;  
  
  cell.listen();
  delay(5000);

  if(doPost(&connectionId, &path, &actualLatitude, &actualLongitude))
  {
    treatServerResponse(&responseFromServer);
#ifdef DEBUG_MESSAGES
    Serial.println(F("Delay"));
#endif
    delay(TIME_TO_SEND);
  }
#ifdef DEBUG_MESSAGES
  Serial.println(responseFromServer);
#endif  
  responseFromServer = ""; 
}

