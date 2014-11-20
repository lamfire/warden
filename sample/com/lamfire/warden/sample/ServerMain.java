package com.lamfire.warden.sample;


import com.lamfire.warden.ActionRegistry;
import com.lamfire.warden.HttpServer;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		ActionRegistry registry = new ActionRegistry();
		registry.mappingPackage("com.lamfire.warden.sample");
		
		HttpServer server = new HttpServer("0.0.0.0", 8080);
        server.setWorkThreads(4);
		server.startup(registry);
	}
}
