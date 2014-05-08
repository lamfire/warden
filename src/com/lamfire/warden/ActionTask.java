package com.lamfire.warden;

import com.lamfire.logger.Logger;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.io.ByteArrayOutputStream;

class ActionTask implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(ActionTask.class);
	private Action action;
	private ActionContext context;

	
	public ActionTask(ActionContext context,Action action){
		this.context = context;
		this.action = action;
	}

	@Override
	public void run() {
		try {
			action.execute(this.context);
			HttpResponseWriters.writeResponse(context.getChannel(), context.getHttpResponse(),context.getHttpResponseData());
		} catch (Throwable t) {
			HttpResponseWriters.writeError(context.getChannel(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
            LOGGER.error(t.getMessage(), t);
		}
	}

}
