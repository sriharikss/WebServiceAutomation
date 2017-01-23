package com.api.restassured.practice;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AppTest{
    
	/**Open POSTMAN and start UFT services for before running below methods.*/
		
	@Test
	public void testStatus(){
		String baseUrl = "https://maps.googleapis.com";
		String path = "/maps/api/geocode/json";
		String url = baseUrl + path;
		int statusCode = given()
							.header("address", "Bangalore, India")
							.header("key", "AIzaSyDvsjXzZWurU5PISOdo1NwZmdCoAXZqkzc")
						.when()
							.get(url)
							.statusCode();				
		System.out.println(statusCode);

		assertTrue("Status code is not 200", statusCode == 200);
	}
	
	@Test
	public void usingPathParams(){
		String baseUrl = "http://localhost:24240/HPFlights_REST";
		String path = "/Flights?DepartureCity={FlyFrom}&ArrivalCity={FlyTo}";
		String url = baseUrl + path;
		/*int statusCode =*/ given()
							.contentType(ContentType.XML)
							.pathParam("FlyFrom", "Denver")
							.pathParam("FlyTo", "London")
						.when()
							.get(url)
						.then()
							.assertThat()
							.statusCode(200);
		
		//System.out.println("The status code for getting list of flights "+statusCode);		
	}
	
	@Test
	public void testResponsetype(){
		String baseUrl = "http://localhost:24240/HPFlights_REST";
		String path = "/Flights?DepartureCity={FlyFrom}&ArrivalCity={FlyTo}";
		String url = baseUrl + path;
		Headers headers =  given()
							.contentType(ContentType.XML)
							.pathParam("FlyFrom", "Denver")
							.pathParam("FlyTo", "London")
						.when()
							.get(url)
							.headers();
		Header responseType = headers.get("Content-Type");
		assertTrue("The response type is not XML", responseType.getValue().contains(ContentType.XML.toString()));
	}
	
	@Test
	public void testResponseValues(){
		String baseUrl = "https://echo.getpostman.com";
		String path = "/get?test=123";
		String url = baseUrl + path;
		Response response = given()
								.contentType(ContentType.TEXT)
							.when()
								.get(url)
							.then()
								.extract()
								.response();
		String testValue = response.path("args.test");
		assertNotNull("Parameter with name test does not exist",testValue);
		assertTrue("Test name is not 123","123".equals(response.path("args.test")));
	}
	
	@Test
	public void testResponseCount(){
		String baseUrl = "https://echo.getpostman.com";
		String path = "/get?test=123";
		String url = baseUrl + path;
		Response response = given()
				.contentType(ContentType.TEXT)
			.when()
				.get(url)
			.then()
				.extract()
				.response();
		JsonPath jsonPath = new JsonPath(response.asString());
		Map<Object, Object> headerValues = jsonPath.getMap("headers");
		assertTrue("The headers count is mismatching :",headerValues.size() == 7);		
	}
	
	@Test
	public void testPostAPI(){
		String baseUrl = "https://echo.getpostman.com";
		String path = "/post";
		String url = baseUrl + path;
		String body = "Duis posuere augue vel cursus pharetra. In luctus a ex nec pretium. Praesent neque quam, tincidunt nec leo eget, rutrum vehicula magna."
				+ "Maecenas consequat elementum elit, id semper sem tristique et. Integer pulvinar enim quis consectetur interdum volutpat";
		Response response = given()
								.contentType(ContentType.TEXT)
								.body(body)
							.when()
								.post(url)
							.then()
								.extract()
								.response();
		JsonPath jsonPath = new JsonPath(response.asString());
		System.out.println(jsonPath.getString("data"));
		assertTrue("An issue with Body",response.path("data").equals(body));
	}
	
	@Test
	public void sendJsonBodyAndTestPostAPI() {
		String baseUrl = "https://www.googleapis.com";
		String path = "geolocation/v1/geolocate?key={key}";
		String url = baseUrl + "/" + path;
		Map<String, Object> jsonRequest = new HashMap<String, Object>();
		jsonRequest.put("homeMobileCountryCode", 310);
		jsonRequest.put("homeMobileNetworkCode", 410);
		jsonRequest.put("radioType", "gsm");
		jsonRequest.put("carrier", "Vodafone");
		jsonRequest.put("considerIp", "true");
		Response response = given()
								.contentType(ContentType.JSON)
								.pathParam("key", "AIzaSyDvsjXzZWurU5PISOdo1NwZmdCoAXZqkzc")
								.body(jsonRequest)
							.when()
								.post(url)
							.then()
								.extract()
								.response();
		JsonPath jsonPath = new JsonPath(response.asString());
		System.out.println("Latitude is "+jsonPath.get("location.lat"));
	}
}
