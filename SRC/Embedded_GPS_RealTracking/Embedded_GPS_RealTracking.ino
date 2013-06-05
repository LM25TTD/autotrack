#include <SoftwareSerial.h>
#include <SM5100B_GPRS.h>
#include <TinyGPS.h>
#include <avr/pgmspace.h>

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
    Serial.println(F("GPRS"));
    if(cell.setUpPDPContext(&pdpId, &apn, &user, &password))
    {
      Serial.println(F("SetPDP"));
      if(cell.activatePDPContext(&pdpId))
      {
        Serial.println(F("ActivePDP"));
      }
    }
  }
}

void createSocket()
{
  if(cell.connectToHostTCP(&connectionId, &HOST, &PORT))
  {
    Serial.println(F("Connect"));
    if(cell.configureDisplayFormat(&connectionId, GSM_SHOW_ASCII, GSM_NOT_ECHO_RESPONSE))
    {
      Serial.println(F("Display"));      
    }
  } 
}


void rebootGSMProcedure()
{
  Serial.println(F("Rebooting GSM..."));
  cell.listen();
  delay(5000);
  attachNetwork();
  createSocket();   
  Serial.println(F("Reboot GSM complete..."));  
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
    Serial.println(F("Socket is Open"));
    if(cell.sendData(request, connectionId))
    {
      Serial.println(F("SendData"));
      responseFromServer = cell.getServerResponse(connectionId);        
      cell.cleanCounters();
      return (true);
    }
    else
    {
      signalizeError();
      Serial.println(F("FAIL!!! SendData"));
      return (false);
    }
  }
  else
  {
    Serial.println(F("Fail on Socket Status!"));
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
  Serial.begin(9600);  
  cell.initializeModule(9600);
  attachNetwork();
  createSocket();
  gpsCommunicator.begin(4800);
  Serial.println(F("Setup Ok!"));
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
    Serial.println(F("Delay"));
    delay(25000);
  }
  Serial.println(responseFromServer);
  responseFromServer = ""; 
}









