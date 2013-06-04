#include <SoftwareSerial.h>
#include <SM5100B_GPRS.h>



String USER_AGENT = "Mozilla/5.0";
String HOST = "179.236.72.52";
int PORT = 8229;


SM5100B_GPRS cell(2,3);  

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
    Serial.println("GPRS");
    if(cell.setUpPDPContext(&pdpId, &apn, &user, &password))
    {
      Serial.println("SetPDP");
      if(cell.activatePDPContext(&pdpId))
      {
        Serial.println("ActivePDP");
      }
    }
  }
}

void createSocket()
{
  if(cell.connectToHostTCP(&connectionId, &HOST, &PORT))
  {
    Serial.println("Connect");
    if(cell.configureDisplayFormat(&connectionId, GSM_SHOW_ASCII, GSM_NOT_ECHO_RESPONSE))
    {
      Serial.println("Display");      
    }
  } 
}


void rebootGSMProcedure()
{
  Serial.println("Rebooting GSM...");
  attachNetwork();
  createSocket();   
  Serial.println("Reboot GSM complete...");  
}

void signalizeError()
{
  numOfErrors++;
  if(numOfErrors>20)
  {
    rebootGSMProcedure();
  } 
}

void sendMessageToServer(String *request, byte *connectionId)
{     
  if(cell.checkSocketStatusTCP())
  {
    numOfErrors=0;
    Serial.println("Socket is Open");
    if(cell.sendData(request, connectionId))
    {
      Serial.println("SendData");
      responseFromServer = cell.getServerResponse(connectionId);        
      cell.cleanCounters();
      Serial.println("Delay");
      delay(25000);
    }
    else
    {
      signalizeError();
      Serial.println("FAIL!!! SendData");
    }
  }
  else
  {
    Serial.println("Fail on Socket Status!");
    while(!cell.dataStart(connectionId))
    {  
      signalizeError();
    }  
    signalizeError();
    cell.cleanCounters();     
  }
  delay(100); 
}


void doPost(byte *connectionId, String *path, float *latitude, float *longitude)
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

  sendMessageToServer(&request, connectionId);  
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

void setup()
{
  Serial.begin(9600);  
  cell.initializeModule(9600);
  attachNetwork();
  createSocket();   
  Serial.println("Setup Ok!");
}


void loop()
{
  actualLatitude = 1.0005f;
  actualLongitude =  -1.045785f;

  doPost(&connectionId, &path, &actualLatitude, &actualLongitude);

  responseFromServer = "";

}








