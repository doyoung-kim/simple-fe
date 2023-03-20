package com.example.simplefe.controller;
import java.util.List;

import com.example.simplefe.model.Simple;
import com.example.simplefe.service.SimpleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SimpleFeController {

	private final SimpleService service;
	
	@GetMapping("/hello")
	public String hello() {		
		log.info("==========simple-fe hello()");		
		return service.hello();		
	}

	@GetMapping("/simple")
	public List<Simple> simple(){		 
		log.info("==========simple-fe simple()");
		return service.simple();		
	}
	
	@GetMapping("/version")
	public String version(){
		log.info("====== simple-fe  version()");
		return service.version();
		
	}

	@GetMapping("/local_version")
	public String localVersion(){
		log.debug("====== simple-fe  local version()");
		return "=====  version 1.0";
	}	

	@GetMapping("/local_hello")
	public String localHello() {		
		log.debug("==========simple-fe debug local hello()");
		log.info("==========simple-fe local hello()");		
		return "hello local world";
	}


}
