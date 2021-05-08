package com.alessio.service;


import com.alessio.model.Greeting;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(serviceName = "InfoService")
public interface GreetingWsService {
	@WebMethod()
	@WebResult(name = "Greeting")
	Greeting sayHello(@WebParam(name = "GreetingsRequest") String name);
}
