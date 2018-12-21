package com.example.demogateway;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemogatewayApplicationTests {

	@LocalServerPort
	int port;
	private WebTestClient client;

	private static ClientAndServer mockRest;

	@BeforeClass
	public static void startServer() {
		mockRest = ClientAndServer.startClientAndServer(38492);
		mockRest.when(request().withMethod("GET").withPath("/v2/version"))
				.respond(response().withStatusCode(200)
						.withHeader("Access-Control-Allow-Credentials", "true")
						.withHeader("Access-Control-Allow-Origin", "http://wat.com"));
	}

	@AfterClass
	public static void stopServer() {
		mockRest.stop();
	}

	@Before
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	public void testCorsRest() {
		client.get().uri("/v2/version")
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", "http://wat.com")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().valueEquals("Access-Control-Allow-Origin", "http://wat.com")
				.expectHeader().valueEquals("Access-Control-Allow-Credentials", "true")
		;
	}

}
