package com.lamfire.warden;


import java.io.OutputStream;

public interface Action {

	public void execute(ActionContext context);
	
}
