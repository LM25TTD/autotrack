#ifndef SM5100B_GPRS_H_
#define SM5100B_GPRS_H_
#include "Arduino.h"
#include <SoftwareSerial.h>
#include <avr/pgmspace.h>

#define RETURN_TIMEOUT 5000
#define SOCKET_READY_TIMEOUT 10000
#define PRE_PACKET_SIZE 4

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
	boolean setUpPDPContext(byte *pdpId, String *apn);
	boolean setUpPDPContext(byte *pdpId, String *apn, String *username,
			String *password);
	boolean activatePDPContext(byte *pdpId);
	boolean connectToHostTCP(byte *connectionId, String *hostNameOrIP,
			int *hostPort);
	boolean checkSocketStatusTCP();
	boolean sendData(String *data, byte *connectionId);
	boolean sendString(String *stringToSend, byte *connectionId);
	boolean configureDisplayFormat(byte *connectionId, byte state, byte mode);
	boolean checkDataSentACK(int *dataLength);
	String getServerResponse(byte *connectionId);
	boolean dataStart(byte *connectionId);
	boolean dataStop(byte *connectionId);
	boolean cleanCounters();
	boolean waitUntil(String forCompare);
private:
	boolean waitFor(String forCompare);
	boolean waitFor(char c);
	String getMessage();
	int checkSocketString(String *socketString);
	short nthIndexOf(short n, char c, String *s);
	short socketStringSlice(short n, String *s);
};

#endif /* SM5100B_GPRS_H_ */
