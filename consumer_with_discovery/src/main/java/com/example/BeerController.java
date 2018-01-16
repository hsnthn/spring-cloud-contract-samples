package com.example;

import java.net.MalformedURLException;
import java.net.URI;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerController {

	private final RestTemplate restTemplate;
	private final ProducerClient producerClient;

	BeerController(RestTemplate restTemplate, ProducerClient producerClient) {
		this.restTemplate = restTemplate;
		this.producerClient = producerClient;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) throws MalformedURLException {
		//remove::start[]
		//tag::controller[]
		ResponseEntity<Response> response = this.producerClient.validateEmail(person);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO";
		default:
			return "GET LOST";
		}
		//end::controller[]
		//remove::end[return]
	}
}

class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Person() {
	}
}

class Response {
	public ResponseStatus status;
}

enum ResponseStatus {
	OK, NOT_OK
}

@FeignClient("somenameforproducer")
interface ProducerClient {
	@RequestMapping(value = "/check", method = RequestMethod.POST, consumes = "application/json",
			produces = "application/json")
	ResponseEntity<Response> validateEmail(@RequestBody Person person);
}