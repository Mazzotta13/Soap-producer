package com.alessio.security.handler.processor;

import javax.xml.ws.handler.soap.SOAPMessageContext;

public interface SoapMessageProcessor {

    void process(SOAPMessageContext messageContext) throws Exception;
}
