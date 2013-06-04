#include "SM5100B_GPRS.h"

SM5100B_GPRS::SM5100B_GPRS(short rxpin, short txpin) :
		SoftwareSerial(rxpin, txpin)
{

}

boolean SM5100B_GPRS::initializeModule(long int baudrate)
{
#ifdef DEBUG
	Serial.println("Starting SM5100B Module...");
#endif
	this->begin(baudrate);
	boolean success = waitUntil("+SIND: 4");
#ifdef DEBUG
	if (success)
	{
		Serial.println("Module started successful!");
	}
	else
	{
		Serial.println("FAIL on module starting!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::attachGPRS()
{
#ifdef DEBUG
	Serial.println("Attaching GPRS...");
#endif
	this->println("AT+CGATT=1");
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println("GPRS attached successful!");
	}
	else
	{
		Serial.println("FAIL on GPRS attach!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::setUpPDPContext(byte *pdpId, String *apn)
{
#ifdef DEBUG
	Serial.println("Setting up PDP Context...");
#endif
	this->println("AT+CGDCONT=" + String(*pdpId) + ",\"IP\",\"" + *apn + "\"");
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println("PDP Context setted up successful!");
	}
	else
	{
		Serial.println("FAIL on PDP Context setup!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::setUpPDPContext(byte *pdpId, String *apn,
		String *username, String *password)
{
#ifdef DEBUG
	Serial.println("Setting up PDP Context...");
#endif
	this->println("AT+CGDCONT=" + String(*pdpId) + ",\"IP\",\"" + *apn + "\"");
	boolean success = waitFor("OK");
	this->println(
			"AT+CGPCO=0,\"" + *username + "\",\"" + *password + "\","
					+ String(*pdpId));
	success = (success && waitFor("OK"));
#ifdef DEBUG
	if (success)
	{
		Serial.println("PDP Context setted up successful!");
	}
	else
	{
		Serial.println("FAIL on PDP Context setup!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::activatePDPContext(byte *pdpId)
{
#ifdef DEBUG
	Serial.println("Activating PDP Context...");
#endif
	this->println("AT+CGACT=1," + String(*pdpId));
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println("PDP Context activated successful!");
	}
	else
	{
		Serial.println("PDP Context activation FAILURE!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::connectToHostTCP(byte *connectionId, String *hostNameOrIP,
		int *hostPort)
{
#ifdef DEBUG
	Serial.println("Connecting to host...");
#endif
	this->println(
			"AT+SDATACONF=1,\"TCP\",\"" + *hostNameOrIP + "\","
					+ String(*hostPort));
	boolean success = waitFor("OK");
	success = (success && dataStart(connectionId));
	delay(5000);
#ifdef DEBUG
	if (success)
	{
		Serial.println("Connection successful!");
	}
	else
	{
		Serial.println("FAIL on Connection!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::dataStart(byte *connectionId)
{
	this->println("AT+SDATASTART=" + String(*connectionId) + ",1");
	return (waitFor("OK"));
}

boolean SM5100B_GPRS::dataStop(byte *connectionId)
{
	this->println("AT+SDATASTART=" + String(*connectionId) + ",0");
	return (waitFor("OK"));
}

boolean SM5100B_GPRS::checkSocketStatusTCP()
{
	unsigned long checkTimeout = millis();
	String sockstat = "";
	while (true)
	{
		//if ((millis() - checkTimeout) > SOCKET_READY_TIMEOUT)
		//	return (false);

#ifdef DEBUG
		Serial.println("Checking socket status:");
#endif
		this->println("AT+SDATASTATUS=1");
		sockstat = getMessage();
		if (sockstat == "OK")
			sockstat = getMessage();
		sockstat = sockstat.substring(0, 22);
		waitFor("OK");
		if (sockstat.compareTo("+SOCKSTATUS:  1,0,0104") == 0)
		{
#ifdef DEBUG
			Serial.println(
					"Not connected yet. Waiting 1 second and trying again.");
#endif
			delay(1000);
		}
		else
		{
			if (sockstat.compareTo("+SOCKSTATUS:  1,1,0102") == 0)
			{
#ifdef DEBUG	
				Serial.println("Socket connected");
#endif
				return (true);
			}
			else
			{
#ifdef DEBUG
				Serial.println("We didn't expect that.");
#endif
				return (false);
			}
		}
	}
}

boolean SM5100B_GPRS::sendData(String *data, byte *connectionId)
{
#ifdef DEBUG
	Serial.println("Sending HTTP packet...");
#endif
	int packetLength = PRE_PACKET_SIZE + data->length();

	this->print(
			"AT+SDATATSEND=" + String(*connectionId) + ","
					+ String(packetLength) + "\r");
	waitFor('>');
	this->print(*data + "\r\n\r\n");
	this->write(26);
	boolean success = waitFor("OK");
	success = (success && checkDataSentACK(&packetLength));
#ifdef DEBUG
	if (success)
	{
		Serial.println("Data sent successful!");
	}
	else
	{
		Serial.println("FAIL on data send!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::sendString(String *stringToSend, byte *connectionId)
{
#ifdef DEBUG
	Serial.println("Sending HTTP string...");
#endif
	this->print(
			"AT+SSTRSEND=" + String(*connectionId) + ",\"" + *stringToSend
					+ "\"\r");
	boolean success = waitFor("OK");
	int length = (PRE_PACKET_SIZE + stringToSend->length());
	success = (success && checkDataSentACK(&length));
#ifdef DEBUG
	if (success)
	{
		Serial.println("String sent successful!");
	}
	else
	{
		Serial.println("FAIL on string send!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::configureDisplayFormat(byte *connectionId, byte state,
		byte mode)
{
#ifdef DEBUG
	Serial.println("Configuring data display format...");
#endif
	this->println(
			"AT+SDATARXMD=" + String(*connectionId) + "," + String(state) + ","
					+ String(mode));
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println("Display configured successful!");
	}
	else
	{
		Serial.println("Configuration of display FAILURE!!");
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::checkDataSentACK(int *dataLength)
{
	unsigned long checkTimeout = millis();
	String s = "";
	while (true)
	{
		this->println("AT+SDATASTATUS=1");
		s = getMessage();
		if ((s == "+STCPD:1") || (s == "+STCPC:1"))
			s = getMessage();
		waitFor("OK");

		if (!s.startsWith("+SOCKSTATUS"))
		{
#ifdef DEBUG
			Serial.println("Wait, this isn't the SOCKSTATUS message!");
#endif
			return (false);
		}
		if (checkSocketString(&s) >= *dataLength)
		{
			return (true);
		}
		else
		{
			if ((millis() - checkTimeout) > SOCKET_READY_TIMEOUT)
				return (false);
#ifdef DEBUG
			Serial.println(
					"Sent data not yet acknowledged by server, waiting 1 second and checking again.");
#endif
			delay(1000);
		}
	}
}

String SM5100B_GPRS::getServerResponse(byte *connectionId)
{
#ifdef DEBUG
	Serial.println("Reading data from server...");
#endif
	this->println("AT+SDATAREAD=" + String(*connectionId));
	String responseFromServer = getMessage();
	return (responseFromServer);
}

boolean SM5100B_GPRS::waitUntil(String forCompare)
{
	String message = "";
	unsigned long lastReceive = millis();
	while (true)
	{
		message = this->getMessage();
		lastReceive = millis();
		if (message == forCompare)
		{
			delay(100);
			return (true);
		}
		else
		{
			if ((millis() - lastReceive) > RETURN_TIMEOUT)
			{
				return (false);
			}
		}
	}
}

boolean SM5100B_GPRS::cleanCounters()
{
	this->println("AT+SDATASTATUS=0");
	return (waitFor("OK"));
}

boolean SM5100B_GPRS::waitFor(String forCompare)
{
	String message = getMessage();
	if (message != forCompare)
	{
		return (false);
	}
	delay(100);
	return (true);
}

String SM5100B_GPRS::getMessage()
{
	String s = "";
	while (true)
	{
		if (this->available() > 0)
		{
			s = s + (char) this->read();
			if (s.length() > 1 && s[s.length() - 2] == '\r'
					&& s[s.length() - 1] == '\n')
			{
				if (s == " \r\n" || s == "\r\n")
				{
					s = "";
				}
				else
				{
					s = (s.substring(0, s.length() - 2));
#ifdef DEBUG
					Serial.println(s);
#endif
					break;
				}
			}
		}
	}
	return (s);
}

int SM5100B_GPRS::checkSocketString(String *s)
{
	if (socketStringSlice(3, s) == 0)
		return (0);
	else if (socketStringSlice(3, s) == socketStringSlice(4, s))
		return (socketStringSlice(3, s));
	else
		return (0);
}

short SM5100B_GPRS::nthIndexOf(short n, char c, String *s)
{
	short index = 0;
	for (short i = 0; i <= n; i++)
	{
		index = s->indexOf(c, index + 1);
	}
	return (index);
}

short SM5100B_GPRS::socketStringSlice(short n, String *s)
{
	String slice = s->substring(nthIndexOf(n - 1, ',', s) + 1,
			nthIndexOf(n, ',', s));
	char cArray[slice.length() + 1];
	slice.toCharArray(cArray, sizeof(cArray));
	return (atoi(cArray));
}

boolean SM5100B_GPRS::waitFor(char c)
{
	unsigned long lastReceive = millis();
	while (true)
	{
		if (this->available() > 0)
		{
			if ((char) this->read() == c)
			{
				delay(100);
				return true;
			}
			if ((millis() - lastReceive) > RETURN_TIMEOUT)
			{
				return (false);
			}
		}
	}
}
