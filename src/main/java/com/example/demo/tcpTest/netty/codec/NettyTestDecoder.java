package com.example.demo.tcpTest.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Blg解码器
 *
 * @author qingzhou
 * 1/9/19
 */
@Slf4j
public class NettyTestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        System.out.printf(out.toString());
        int length = buf.readableBytes();
        if (length < 10) {
            return;
        }
        int inx = buf.readerIndex();

        short head = buf.getShort(inx);
        if (head == 0xAA || head == 0xBB) {
            inx += 2;
            short addrLength = buf.getShort(inx);
            inx += 2;
            byte[] macBytes = new byte[6];
            buf.getBytes(inx, macBytes);
            inx += 6;
            int dataLength = buf.getInt(inx);
            inx += 4;
            byte type = buf.getByte(inx);
            inx += 1;
            byte[] data = new byte[dataLength];
            buf.getBytes(inx, data);
            String dataJson = new String(data, 0, data.length, StandardCharsets.UTF_8);

            log.info("ThirdPartyDecoder：json:[{}]", dataJson);
//            SpringUtil.getBean(ThirdPartyEcologyDataHelper.class).handle(type, Integer.toHexString(vendorId & 0xffff), ProtocolUtil.byteArrToHexStr(macBytes), dataJson);
        }
    }

}
