package com.lamfire.warden;

import com.lamfire.logger.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

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
			HttpResponseUtils.writeResponse(context.getChannel(), context.getHttpResponse(), context.getHttpResponseData());
		} catch (Throwable t) {
            Channel channel = context.getChannel();
            if(channel.isWritable()){
			    HttpResponseUtils.writeError(context.getChannel(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
            LOGGER.error(t.getMessage(), t);
		}
	}

}
