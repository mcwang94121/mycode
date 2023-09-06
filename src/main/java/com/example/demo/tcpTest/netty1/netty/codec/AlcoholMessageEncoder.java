package com.example.demo.tcpTest.netty1.netty.codec;

import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyTest1Encoder
 *
 * @author mc
 * @date 2023年09月01日
 */
@Slf4j
public class AlcoholMessageEncoder extends MessageToByteEncoder<AlcoholBaseMO> {

    private final byte[] header = new byte[]{(byte) 0xAA, (byte) 0xBB};

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AlcoholBaseMO nettyTestMessage, ByteBuf out) {
        byte[] byteArray = nettyTestMessage.toByteArray();
        out.writeBytes(header);
        out.writeBytes(byteArray);
        log.info("AlcoholTcpMessageEncoder encode nettyTestMessage byteArray:{}", byteArray);
    }
}
