package practice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("deprecation")
public class GetRequest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://echo.getpostman.com/basic-auth");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Authorization", "Basic cG9zdG1hbjpwYXNzd29yZA==");

		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(request, responseHandler);

			System.out.println(response);

			JSONObject getResponse = new JSONObject();
			JSONParser parser = new JSONParser();
			getResponse = (JSONObject) parser.parse(response);
			System.out.println(getResponse.get("authenticated"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}