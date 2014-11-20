package com.lamfire.warden;

import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
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

    public static void write(OutputStream output,String message) {
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(OutputStream output,String message,Charset charset) {
        try {
            output.write(message.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(OutputStream output,byte[] responseMessage) {
        try {
            output.write(responseMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeRedirectResponse(Channel channel, String redirect) {
        if (!channel.isConnected() || !channel.isWritable()) {
            return;
        }
        try {
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(302));
            response.setHeader(Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.setHeader(Names.LOCATION,redirect);
            channel.write(response);
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(),t);
        }
    }
	
	public static void writeResponse(Channel channel, HttpResponse response,byte[] responseMessage) {
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
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			response.setHeader("Content-Length", String.valueOf(length));
            channel.write(response);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(),t);
		}
	}



	public static void writeError(Channel channel, HttpResponseStatus status) {
        if (!channel.isConnected() || !channel.isWritable()) {
            return;
        }
		try {
			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
			response.setHeader(Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
			JSON json = new JSON();
			json.put("status", status.getCode());
			json.put("message", status.getReasonPhrase());
			response.setContent(ChannelBuffers.copiedBuffer(json.toJSONString(), CharsetUtil.UTF_8));
			channel.write(response);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(),t);
		}
	}
}
