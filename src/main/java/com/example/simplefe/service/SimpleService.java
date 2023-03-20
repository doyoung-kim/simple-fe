package com.example.simplefe.service;

import java.util.List;

import com.example.simplefe.model.Simple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value="boardService", url= "${api.simple.url}", fallbackFactory = SimpleServiceFallbackFactory.class)
public interface SimpleService {

	@GetMapping("/hello")
    public String hello();

	@GetMapping("/simple")
	public List<Simple> simple();

	@GetMapping("/version")
    public String version();
}
