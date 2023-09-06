package com.example.demo.tcpTest.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther: seekcy
 * @date: 2019/8/1 16:56
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyTestHandler extends SimpleChannelInboundHandler<String> {

    public static final NettyTestHandler INSTANCE = new NettyTestHandler();
    private static final Map<String, ChannelHandlerContext> BLGCTX = new ConcurrentHashMap<>();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        System.out.printf(s);
    }
}
