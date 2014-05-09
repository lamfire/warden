package com.lamfire.warden;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.lamfire.logger.Logger;

class HttpServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger LOGGER = Logger.getLogger(HttpServerHandler.class);

	private ActionRegistry registry;
	private ExecutorService worker;
	private AtomicInteger counter = new AtomicInteger();

	public HttpServerHandler(ActionRegistry registry, ExecutorService worker) {
		this.registry = registry;
		this.worker = worker;
	}

	protected Action getAction(ActionContext context) throws ActionNotFoundException {
		Action action = registry.lookup(context);
		if (action == null) {
			throw new ActionNotFoundException("Not found action mapping URI:" + context.getHttpRequestUri());
		}

        return action;
	}

	protected void invokeAction(ActionContext context, Action action) {
		ActionTask task = new ActionTask(context, action);
		this.worker.submit(task);
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		HttpRequest request = (HttpRequest) e.getMessage();

		try {
            ActionContext context = new ActionContext(ctx, request);
            Action action = getAction(context);
            invokeAction(context,action);
		} catch (Throwable t) {
			HttpResponseWriters.writeError(ctx.getChannel(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error(t.getMessage(), t);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Throwable cause = e.getCause();
		try {
			if (cause instanceof TooLongFrameException) {
				HttpResponseWriters.writeError(ctx.getChannel(), HttpResponseStatus.BAD_REQUEST);
				return;
			}
			LOGGER.error(e.getCause().getMessage(), e.getCause());
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		this.counter.decrementAndGet();
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelOpen(ctx, e);
		this.counter.incrementAndGet();
	}

}
