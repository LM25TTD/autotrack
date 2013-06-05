#include <SoftwareSerial.h>
#include <SM5100B_GPRS.h>
#include <TinyGPS.h>
#include <avr/pgmspace.h>

#define DEBUG_MESSAGES

#define MAX_NUM_ERRORS 20
#define GSM_TX_PIN 2
#define GSM_RX_PIN 3
#define GPS_TX_PIN 4
#define GPS_RX_PIN 5
#define GPS_T_OUT 5000


TinyGPS gps;
SM5100B_GPRS cell(GSM_TX_PIN, GSM_RX_PIN);  
SoftwareSerial gpsCommunicator(GPS_TX_PIN, GPS_RX_PIN);


String USER_AGENT = "Mozilla/5.0";
String HOST = "201.8.178.230";
int PORT = 8229;

String apn = "tim.br";
String user = "tim";
String password = "tim";
String path = "/";
String responseFromServer = "";
byte pdpId = 1;
byte connectionId = 1;

byte numOfErrors=0;

float actualLatitude = 0.0f;
float actualLongitude = 0.0f;

void attachNetwork()
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
      }
    }
  }
}

void createSocket()
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
    }
  } 
}


void rebootGSMProcedure()
{
#ifdef DEBUG_MESSAGES  
  Serial.println(F("Rebooting GSM..."));
#endif
  cell.listen();
  delay(5000);
  attachNetwork();
  createSocket();
#ifdef DEBUG_MESSAGES  
  Serial.println(F("Reboot GSM complete..."));  
#endif
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

  jsonContent += "\"lat\":\"";
  dtostrf(*latitude, 1, 4, latConverted);
  jsonContent+= latConverted;

  char longConverted[10] = "";

  jsonContent += "\",\"long\":\"";
  dtostrf(*longitude, 1, 4, longConverted);
  jsonContent += longConverted;
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


void loop()
{ 
  gpsCommunicator.listen();
  delay(5000);

  if(feedGps())
    gps.f_get_position(&actualLatitude, &actualLongitude);  

  cell.listen();
  delay(5000);

  if(doPost(&connectionId, &path, &actualLatitude, &actualLongitude))
  {
#ifdef DEBUG_MESSAGES
    Serial.println(F("Delay"));
#endif
    delay(25000);
  }
  Serial.println(responseFromServer);
  responseFromServer = ""; 
}









