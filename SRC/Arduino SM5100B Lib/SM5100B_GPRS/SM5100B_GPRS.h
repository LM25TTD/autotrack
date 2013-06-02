#ifndef SM5100B_GPRS_H_
#define SM5100B_GPRS_H_
#include "Arduino.h"
#include <SoftwareSerial.h>

#define RETURN_TIMEOUT 5000
#define SOCKET_READY_TIMEOUT 10000
#define PRE_PACKET_SIZE 26

#define GSM_SHOW_HEX 0
#define GSM_SHOW_ASCII 1

#define GSM_ECHO_RESPONSE 0
#define GSM_NOT_ECHO_RESPONSE 1

//#define DEBUG

class SM5100B_GPRS: public SoftwareSerial
{
public:
	SM5100B_GPRS(short rxpin, short txpin);
	boolean initializeModule(long int baudrate);
	boolean attachGPRS();
	boolean setUpPDPContext(int pdpId, String apn);
	boolean setUpPDPContext(int pdpId, String apn, String username,
			String password);
	boolean activatePDPContext(int pdpId);
	boolean connectToHostTCP(int connectionId, String hostNameOrIP,
			String hostPort);
	boolean checkSocketStatusTCP();
	boolean sendData(String data, int connectionId, String server,
			String userAgent);
	boolean sendString(String stringToSend, int connectionId, String server,
			String userAgent);
	boolean configureDisplayFormat(int connectionId, int state, int mode);
	boolean checkDataSentACK(int dataLength);
	String getServerResponse(int connectionId);
	boolean dataStart(int connectionId);
	boolean dataStop(int connectionId);
	boolean cleanCounters();
	boolean waitUntil(String forCompare);
private:
	boolean waitFor(String forCompare);
	boolean waitFor(char c);
	String getMessage();
	int checkSocketString(String socketString);
	short nthIndexOf(short n, char c, String s);
	short socketStringSlice(short n, String s);
};

#endif /* SM5100B_GPRS_H_ */
