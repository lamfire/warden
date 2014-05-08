package com.lamfire.warden.sample;

import com.lamfire.json.JSON;
import com.lamfire.utils.HttpClient;

import java.io.IOException;

public class ClientBoundMain {
	

	public static void post() throws IOException {
		HttpClient client = new HttpClient();
		client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
		client.setMethod("POST");
		client.open("http://192.168.1.80:8080");
		//client.open("http://192.168.9.125:8080/");
		
		
		client.addPostParameter("name","lamfire");
        client.addPostParameter("age","18");
		client.post();

		byte[] ret = client.read();
		System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));
		
	}
	
	public static void get() throws IOException {
		HttpClient client = new HttpClient();
		client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
		client.setMethod("GET");

		client.open("http://192.168.1.80:8080/?name=lamfire&age=18");
		byte[] ret = client.read();
		System.out.println("GET_RESULT["+ ret.length +"]:" + new String(ret));
		
	}

	public static void main(String[] args) throws Exception {
		post();
		get();
	}
}
