#include <MemoryFree.h>
#include <SoftwareSerial.h>
#include <SM5100B_GPRS.h>


#define RESET_PIN 13

SM5100B_GPRS cell(2,3);  

const String apn = "tim.br";
const String user = "tim";
const String password = "tim";
const String server = "201.9.96.201";
int port = 8229;
const int pdpId = 1;
const int connectionId = 1;

const String request = "GET / HTTP/1.1";
const String useragent = "Mozilla/5.0";

int numOfErrors=0;
int callbackPeriod=0;

void attachNetwork()
{
  if (cell.attachGPRS())
  {
    Serial.println("GPRS");
    if(cell.setUpPDPContext(pdpId, apn, user, password))
    {
      Serial.println("SetPDP");
      if(cell.activatePDPContext(pdpId))
      {
        Serial.println("ActivePDP");
      }
    }
  }
}

void createSocket()
{
  if(cell.connectToHostTCP(connectionId, server, String(port)))
  {
    Serial.println("Connect");
    if(cell.configureDisplayFormat(connectionId, GSM_SHOW_ASCII, GSM_NOT_ECHO_RESPONSE))
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


void setup()
{
  Serial.begin(9600);  
  cell.initializeModule(9600);
  attachNetwork();
  createSocket();   
  Serial.println("Setup Ok!");
}

void callback()
{       
  if(cell.checkSocketStatusTCP())
  {
    numOfErrors=0;
    Serial.println("Socket is Open");
    if(cell.sendData(request, connectionId, server, useragent))
    {
      Serial.println("SendData");
      Serial.println(cell.getServerResponse(connectionId));        
      cell.cleanCounters();

      Serial.println("Delaying"); 
      delay(15000);
    }
    else
    {
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

void loop()
{
  Serial.print("freeMemory()=");
  Serial.println(freeMemory());
  delay(1000);

  callback();

}



