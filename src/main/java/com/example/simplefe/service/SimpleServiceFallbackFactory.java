package com.example.simplefe.service;

import java.util.ArrayList;
import java.util.List;

import com.example.simplefe.model.Simple;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleServiceFallbackFactory implements FallbackFactory<SimpleService> {

	private final SimpleServiceFallback SimpleServiceFallback;

	public SimpleServiceFallbackFactory() {
		this.SimpleServiceFallback = new SimpleServiceFallback();
	}

	public SimpleService create(Throwable cause) {
		log.info("[SimpleServiceFallbackFactory] error occured: {}", cause.getMessage());
		return SimpleServiceFallback;
	}

	public class SimpleServiceFallback implements SimpleService {
		@Override
		public String hello() {
			log.debug("★ feign return fallback!!!!!!!!");
			return "feign return failed";
		}

		@Override
		public List<Simple> simple() {
			log.debug("★ feign return fallback!!!!!!!!");
			List<Simple> list = new ArrayList<Simple>();
			return list;
		}

		@Override
		public String version() {
			log.debug("★ feign return fallback!!!!!!!!");
			return "feign return failed";
		}
	}
	
}
