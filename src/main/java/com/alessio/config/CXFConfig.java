package com.alessio.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import com.alessio.security.handler.WSSecurityHandlerWithProcessor;
import com.alessio.security.handler.processor.SoapMessageProcessor;
import com.alessio.security.handler.processor.ingoing.DecryptionAndSignatureProcessor;
import com.alessio.security.ssl.KeystoreInfo;
import com.alessio.service.GreetingWsServiceImpl;
import com.alessio.service.interceptors.AppInboundInterceptor;
import com.alessio.service.interceptors.AppOutboundInterceptor;

import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CXFConfig {

    private static final String WS_SECURITY_HANDLER_WITH_PROCESSOR_BEAN_NAME = "WS_SECURITY_HANDLER_WITH_PROCESSOR_BEAN_NAME";

    @Bean
	public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
    }
	
    @Bean(name=Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {    
    	SpringBus springBus = new SpringBus();
       	springBus.getInInterceptors().add(new AppInboundInterceptor());
    	springBus.getOutInterceptors().add(new AppOutboundInterceptor());
    	return springBus;
    }

    @Value("${wssecurity.signature.keystore.path:}")
    private String signatureTruststorePath;
    @Value("${wssecurity.signature.keystore.password:}")
    private String signatureTruststorePassword;
    @Value("${wssecurity.signature.keystore.type:jks}")
    private String signatureTruststoreType;

    @Bean(WS_SECURITY_HANDLER_WITH_PROCESSOR_BEAN_NAME)
    public WSSecurityHandlerWithProcessor wsSecurityHandlerWithProcessor() {
        WSSecurityHandlerWithProcessor wsSecurityHandlerWithProcessor = new WSSecurityHandlerWithProcessor();

        if (StringUtils.hasText(signatureTruststorePath)) {
            KeystoreInfo signatureTruststore =
                    new KeystoreInfo(
                            signatureTruststorePath,
                            signatureTruststorePassword,
                            signatureTruststoreType);

            DecryptionAndSignatureProcessor decryptionAndSignatureProcessor = new DecryptionAndSignatureProcessor();
            decryptionAndSignatureProcessor.setSignatureVerificationKeystoreInfo(signatureTruststore);

            ArrayList<SoapMessageProcessor> ingoingMessageProcessors = new ArrayList<>();
            ingoingMessageProcessors.add(decryptionAndSignatureProcessor);
            wsSecurityHandlerWithProcessor.setIngoingMessageProcessors(ingoingMessageProcessors);
        }

        return wsSecurityHandlerWithProcessor;
    }
    
    @Bean
    public Endpoint endpoint(WSSecurityHandlerWithProcessor wsSecurityHandlerWithProcessor) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new GreetingWsServiceImpl());
        endpoint.getFeatures().add(new LoggingFeature());

        List<Handler> handlers = new ArrayList<>();
        handlers.add(wsSecurityHandlerWithProcessor);
        endpoint.setHandlers(handlers);

        endpoint.publish("/GreetingWs");
        return endpoint;
    }
}
