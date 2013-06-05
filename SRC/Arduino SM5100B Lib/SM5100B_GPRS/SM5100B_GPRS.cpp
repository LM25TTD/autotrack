#include "SM5100B_GPRS.h"

SM5100B_GPRS::SM5100B_GPRS(short rxpin, short txpin) :
		SoftwareSerial(rxpin, txpin)
{

}

boolean SM5100B_GPRS::initializeModule(long int baudrate)
{
#ifdef DEBUG
	Serial.println(F("Starting SM5100B Module..."));
#endif
	this->begin(baudrate);
	boolean success = waitUntil("+SIND: 4");
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("Module started successful!"));
	}
	else
	{
		Serial.println(F("FAIL on module starting!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::attachGPRS()
{
#ifdef DEBUG
	Serial.println(F("Attaching GPRS..."));
#endif
	this->println(F("AT+CGATT=1"));
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("GPRS attached successful!"));
	}
	else
	{
		Serial.println(F("FAIL on GPRS attach!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::setUpPDPContext(byte *pdpId, String *apn)
{
#ifdef DEBUG
	Serial.println(F("Setting up PDP Context..."));
#endif

	this->print(F("AT+CGDCONT="));
	this->print(String(*pdpId));
	this->print(F(",\"IP\",\""));
	this->print(*apn);
	this->println(F("\""));
	//delete (&command);
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("PDP Context setted up successful!"));
	}
	else
	{
		Serial.println(F("FAIL on PDP Context setup!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::setUpPDPContext(byte *pdpId, String *apn,
		String *username, String *password)
{
#ifdef DEBUG
	Serial.println(F("Setting up PDP Context..."));
#endif

	this->print(F("AT+CGDCONT="));
	this->print(String(*pdpId));
	this->print(F(",\"IP\",\""));
	this->print(*apn);
	this->println(F("\""));
	boolean success = waitFor("OK");

	this->print(F("AT+CGPCO=0,\""));
	this->print(*username);
	this->print(F("\",\""));
	this->print(*password);
	this->print(F("\","));
	this->println(String(*pdpId));
	success = (success && waitFor("OK"));
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("PDP Context setted up successful!"));
	}
	else
	{
		Serial.println(F("FAIL on PDP Context setup!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::activatePDPContext(byte *pdpId)
{
#ifdef DEBUG
	Serial.println(F("Activating PDP Context..."));
#endif
	this->print(F("AT+CGACT=1,"));
	this->println(String(*pdpId));
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("PDP Context activated successful!"));
	}
	else
	{
		Serial.println(F("PDP Context activation FAILURE!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::connectToHostTCP(byte *connectionId, String *hostNameOrIP,
		int *hostPort)
{
#ifdef DEBUG
	Serial.println(F("Connecting to host..."));
#endif
	this->print(F("AT+SDATACONF=1,\"TCP\",\""));
	this->print(*hostNameOrIP);
	this->print(F("\","));
	this->println(String(*hostPort));
	boolean success = waitFor("OK");
	success = (success && dataStart(connectionId));
	delay(5000);
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("Connection successful!"));
	}
	else
	{
		Serial.println(F("FAIL on Connection!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::dataStart(byte *connectionId)
{
	this->print(F("AT+SDATASTART="));
	this->print(String(*connectionId));
	this->println(F(",1"));
	return (waitFor("OK"));
}

boolean SM5100B_GPRS::dataStop(byte *connectionId)
{
	this->print(F("AT+SDATASTART="));
	this->print(String(*connectionId));
	this->println(F(",0"));
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
		Serial.println(F("Checking socket status:"));
#endif
		this->println(F("AT+SDATASTATUS=1"));
		sockstat = getMessage();
		if (sockstat == "OK")
			sockstat = getMessage();
		sockstat = sockstat.substring(0, 22);
		waitFor("OK");
		if ((sockstat == "+SOCKSTATUS:  1,0,0104") || (sockstat == "0,0104"))
		{
#ifdef DEBUG
			Serial.println(F(
							"Not connected yet. Waiting 1 second and trying again."));
#endif
			delay(1000);
		}
		else
		{
			if ((sockstat == "+SOCKSTATUS:  1,1,0102")
					|| (sockstat == "1,0102"))
			{
#ifdef DEBUG
				Serial.println(F("Socket connected"));
#endif
				return (true);
			}
			else
			{
#ifdef DEBUG
				Serial.println(F("We didn't expect that."));
#endif
				return (false);
			}
		}
	}
}

boolean SM5100B_GPRS::sendData(String *data, byte *connectionId)
{
#ifdef DEBUG
	Serial.println(F("Sending HTTP packet..."));
#endif
	int packetLength = PRE_PACKET_SIZE + data->length();

	this->print(F("AT+SDATATSEND="));
	this->print(String(*connectionId));
	this->print(F(","));
	this->print(String(packetLength));
	this->print(F("\r"));
	waitFor('>');
	this->print(*data);
	this->print(F("\r\n\r\n"));
	this->write(26);
	boolean success = waitFor("OK");
	success = (success && checkDataSentACK(&packetLength));
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("Data sent successful!"));
	}
	else
	{
		Serial.println(F("FAIL on data send!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::sendString(String *stringToSend, byte *connectionId)
{
#ifdef DEBUG
	Serial.println(F("Sending HTTP string..."));
#endif
	this->print(F("AT+SSTRSEND="));
	this->print(String(*connectionId));
	this->print(F(",\""));
	this->print(*stringToSend);
	this->print(F("\"\r"));
	boolean success = waitFor("OK");
	int length = (PRE_PACKET_SIZE + stringToSend->length());
	success = (success && checkDataSentACK(&length));
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("String sent successful!"));
	}
	else
	{
		Serial.println(F("FAIL on string send!!"));
	}
#endif
	return (success);
}

boolean SM5100B_GPRS::configureDisplayFormat(byte *connectionId, byte state,
		byte mode)
{
#ifdef DEBUG
	Serial.println(F("Configuring data display format..."));
#endif
	this->print(F("AT+SDATARXMD="));
	this->print(String(*connectionId));
	this->print(F(","));
	this->print(String(state));
	this->print(F(","));
	this->println(String(mode));
	boolean success = waitFor("OK");
#ifdef DEBUG
	if (success)
	{
		Serial.println(F("Display configured successful!"));
	}
	else
	{
		Serial.println(F("Configuration of display FAILURE!!"));
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
		this->println(F("AT+SDATASTATUS=1"));
		s = getMessage();
		if ((s == "+STCPD:1") || (s == "+STCPC:1"))
			s = getMessage();
		waitFor("OK");

		if (!s.startsWith("+SOCKSTATUS"))
		{
#ifdef DEBUG
			Serial.println(F("Wait, this isn't the SOCKSTATUS message!"));
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
			Serial.println(F(
							"Sent data not yet acknowledged by server, waiting 1 second and checking again."));
#endif
			delay(1000);
		}
	}
}

String SM5100B_GPRS::getServerResponse(byte *connectionId)
{
#ifdef DEBUG
	Serial.println(F("Reading data from server..."));
#endif
	this->print(F("AT+SDATAREAD="));
	this->println(String(*connectionId));
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
	this->println(F("AT+SDATASTATUS=0"));
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
