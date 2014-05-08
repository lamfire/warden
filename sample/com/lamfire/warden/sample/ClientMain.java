package com.lamfire.warden.sample;

import java.io.IOException;

import com.lamfire.json.JSON;
import com.lamfire.utils.HttpClient;

public class ClientMain {
	

	public static void post() throws IOException {
		HttpClient client = new HttpClient();
		client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
		client.setMethod("POST");
		client.open("http://192.168.1.80:8080/echo?a=1");
		//client.open("http://192.168.9.125:8080/");
		
		
		JSON js = new JSON();
		js.put("appkey", "983573123");
		js.put("appsecrt", "{'a':'6788945141'}");
		
		client.post(js.toJSONString().getBytes());

		byte[] ret = client.read();
		System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));
		
	}
	
	public static void get() throws IOException {
		HttpClient client = new HttpClient();
		client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
		client.setMethod("GET");
		
		JSON js = new JSON();
		js.put("appkey", "983573123");
		js.put("appsecrt", "{'a':'6788945141'}");
		
		
		client.open("http://192.168.1.80:8080/echo?" + js.toJSONString());
		byte[] ret = client.read();
		System.out.println("GET_RESULT["+ ret.length +"]:" + new String(ret));
		
	}

	public static void main(String[] args) throws Exception {
		post();
		get();
	}
}
