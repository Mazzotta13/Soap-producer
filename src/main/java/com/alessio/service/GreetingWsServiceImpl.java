package com.alessio.service;

import com.alessio.model.Greeting;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GreetingWsServiceImpl implements GreetingWsService {

	@Override
	public Greeting sayHello(String name) {
		Greeting greeting = new Greeting();
		greeting.setMessage("How are you " + name + "!!!");
		greeting.setDate(new Date());
		return greeting;
	}
}
