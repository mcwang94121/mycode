package com.example.demo.tcpTest.netty1.netty.codec;

import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 数据帧格式
 * 帧头 长度 站点地址 设备地址 命令 数据域 校验码
 * 2Byte 2Byte 2Byte 2Byte 1Byte 0~200Byte 2Byte
 * 帧头:固定值 0xAA，0xBB;
 * 长度:站点地址到数据域字节数;
 * 站点地址:每个站点都有一个唯一的地址，值范围 1~65535; 设备地址:终端设备的地址，值范围 1~65535;
 * 命令:设备根据该命令做相应的功能;
 * 数据域:不同指令的详细信息，数据域长度由命令决定;
 * 校验码:一个累加和校验码，其累加的数据包括长度、站点地址、设备地址、命令、数据域
 *
 * @author wmc
 * @date 2023年09月04日
 */
@Slf4j
public class AlcoholMessageDecoder extends ByteToMessageDecoder {

    /**
     * 帧头+长度字段的长度
     */
    private static final int DATA_MIN_LENGTH = 12;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 数据总字节数
        int bytes = in.readableBytes();

        // 检查字节缓冲区中是否有足够的字节用于解析协议头
        if (bytes < DATA_MIN_LENGTH) {
            return;
        }
        // 标记当前读取位置，以便在解析失败时可以恢复到这个位置
        in.markReaderIndex();

        // 获取读取起点下标
        int idx = 0;
        boolean hasHeaders = false;
        for (int i = 0; i < bytes; i++) {
            byte header1 = in.getByte(i);
            byte header2 = in.getByte(i + 1);
            if (header1 == (byte) 0xAA && header2 == (byte) 0xBB) {
                idx = i;
                hasHeaders = true;
                break;
            }
        }
        if (!hasHeaders) {
            return;
        }

        // 读取帧头
        byte header1 = in.getByte(idx);
        idx += 1;
        byte header2 = in.getByte(idx);
        idx += 1;

        if (header1 != (byte) 0xAA || header2 != (byte) 0xBB) {
            return;
        }
        // 站点地址到数据域字节数; 需要高低位颠倒
        short length = in.getShort(idx);
        idx += 2;

        // 将高低位颠倒
        length = (short) (((length & 0xFF) << 8) | ((length >> 8) & 0xFF));

        // 读取站点地址、
        short siteAddress = in.getShort(idx);
        idx += 2;

        // 设备地址、
        short deviceAddress = in.getShort(idx);
        idx += 2;

        // 命令、
        byte command = in.getByte(idx);
        idx += 1;

        // 数据域 站点地址到数据域字节数;
        // 由于对指定设备进行通讯时，该设备的帧头、站点地址、设备子地址等字段都为固定的值，长度=5+数据域长 度
        byte[] data = new byte[length - 5];
        in.getBytes(idx, data);
        idx += data.length;

        // 计算校验码
        // 校验码:一个累加和校验码，其累加的数据包括长度、站点地址、设备地址、命令、数据域
        short checksum = 0;
        for (int i = 2; i < length + 3; i++) {
            byte aByte = in.getByte(i);
            int unsignedByte = aByte & 0xFF;
            checksum += unsignedByte;
        }

        short checkCode = in.getShort(idx);

        checkCode = (short) (((checkCode & 0xFF) << 8) | ((checkCode >> 8) & 0xFF));
        // 检查校验码是否匹配
        if (checksum != checkCode) {
            return;
        }

        byte[] dataAll = new byte[length + 6];
        in.readBytes(dataAll);
        if (command != AlcoholCommandEnum.HEARTBEAT.getCommand()) {
            StringBuilder originalFrame = new StringBuilder();
            for (byte aByte : dataAll) {
                originalFrame.append(Integer.toHexString(aByte & 0xFF));
                originalFrame.append(" ");
            }
            log.info("AlcoholTcpMessageDecoder decode originalFrame :{}", originalFrame);
        }
        out.add(new AlcoholBaseMO(siteAddress, deviceAddress, command, data));
    }
}
