package com.lamfire.warden.sample;

import com.lamfire.json.JSON;
import com.lamfire.utils.HttpClient;
import com.lamfire.utils.RandomUtils;

import java.io.IOException;

public class ClientBoundMain {
	

	public static void post() throws IOException {
		HttpClient client = new HttpClient();
		client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
		client.setMethod("POST");
        client.setCharset("UTF-8");
		client.open("http://192.168.1.80:8080");
		//client.open("http://192.168.9.125:8080");
		
		
		client.addPostParameter("name","lamfire(小林子)");
        client.addPostParameter("age", ""+RandomUtils.nextInt());
        client.addPostParameter("items",""+RandomUtils.nextInt());
        client.addPostParameter("items",""+RandomUtils.nextInt());
		client.post();

		byte[] ret = client.read();
		System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));
		
	}


	public static void main(String[] args) throws Exception {
        //for(int i=0;i<10;i++)
		post();
	}
}
