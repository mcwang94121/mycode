package com.example.demo.tcpTest.netty.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述:
 *
 * @author mc
 * @auther: seekcy
 * @date: 2019/8/1 16:44
 */
@Slf4j
public class NettyTestEncoder extends MessageToByteEncoder<NettyTestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyTestMessage msg, ByteBuf out) throws Exception {
        System.out.printf(out.toString());
    }

}
