package com.lamfire.warden;

import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

class HttpResponseUtils {
	private static final Logger LOGGER = Logger.getLogger(HttpResponseUtils.class);

    public static void writeRedirectResponse(Channel channel, String redirect) {
        if (!channel.isConnected() || !channel.isWritable()) {
            return;
        }
        try {
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(302));
            response.headers().set(Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set(Names.LOCATION, redirect);
            channel.write(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(),t);
        }
    }
	
	public static void writeResponse(ActionContext context, HttpResponse response,byte[] responseMessage) {
        Channel channel = context.getChannel();
        if (!channel.isConnected() || !channel.isWritable()) {
            return;
        }
		try {
			int length = 0;
			if (responseMessage != null) {
				ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(responseMessage);
				response.setContent(buffer);
				length = response.getContent().writerIndex();
			}
			response.headers().set(Names.CONTENT_TYPE, "text/html; charset=UTF-8");
			response.headers().set(Names.CONTENT_LENGTH, String.valueOf(length));
            ChannelFuture future = channel.write(response);
            if(!context.isKeepAlive()){
                //future.addListener(ChannelFutureListener.CLOSE);
            }
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(),t);
		}
	}

	public static void writeError(ActionContext context, HttpResponseStatus status) {
        writeError(context.getChannel(),status,context.isKeepAlive());
	}

    public static void writeError(Channel channel, HttpResponseStatus status) {
        writeError(channel,status,false);
    }

    public static void writeError(Channel channel, HttpResponseStatus status,boolean keepAlive) {
        if (!channel.isConnected() || !channel.isWritable()) {
            return;
        }
        try {
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
            response.headers().set(Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            JSON json = new JSON();
            json.put("status", status.getCode());
            json.put("message", status.getReasonPhrase());
            response.setContent(ChannelBuffers.copiedBuffer(json.toJSONString(), CharsetUtil.UTF_8));
            ChannelFuture future = channel.write(response);
            if(!keepAlive){
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(),t);
        }
    }
}
