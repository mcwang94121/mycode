package com.example.demo.tcpTest.netty1.netty.codec.dto.req;

import com.joysuch.open.platform.common.util.ProtocolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * NettyTestMessage
 *
 * @author mc
 * @date 2023年09月01日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlcoholBaseMO implements Serializable {

    private static final long serialVersionUID = -9155898011273342668L;

    /**
     * 站点地址到数据域字节数;
     * 由于对指定设备进行通讯时，该设备的帧头、站点地址、设备子地址等字段都为固定的值，长度=5+数据域长 度
     */
    private int length;

    /**
     * 站点地址:每个站点都有一个唯一的地址，值范围 1~65535;
     */
    private int siteAddress;

    /**
     * 设备地址:终端设备的地址，值范围 1~65535;
     */
    private int deviceAddress;

    /**
     * 命令:设备根据该命令做相应的功能;
     */
    private int command;

    /**
     * 数据域:不同指令的详细信息，数据域长度由命令决定;
     */
    private byte[] data;

    /**
     * 校验码:一个累加和校验码，其累加的数据包括长度、站点地址、设备地址、命令、数据域
     */
    private int checkCode;

    public AlcoholBaseMO(int siteAddress, int deviceAddress, int command, byte[] data) {
        this.siteAddress = siteAddress;
        this.deviceAddress = deviceAddress;
        this.command = command;
        this.data = data;
        int length = data != null ? data.length + 5 : 5;
        this.length = (short) (((length & 0xFF) << 8) | ((length >> 8) & 0xFF));
    }

    /**
     * 此处总长度不包含帧头
     *
     * @return byte数组
     */
    private byte[] dataToByteArray() {
        byte[] bs;
        if (data != null) {
            bs = new byte[7 + data.length];
        } else {
            bs = new byte[7];
        }
        int index = 0;
        ProtocolUtil.putShortToBuffer(bs, (short) length, index);
        index += 2;

        ProtocolUtil.putShortToBuffer(bs, (short) siteAddress, index);
        index += 2;

        ProtocolUtil.putShortToBuffer(bs, (short) deviceAddress, index);
        index += 2;

        ProtocolUtil.putByteToBuffer(bs, (byte) command, index);
        index += 1;

        if (data != null && data.length > 0) {
            ProtocolUtil.putByteArrToBuffer(bs, data, index);
        }
        return bs;
    }

    /**
     * 计算校验码
     * 校验码:一个累加和校验码，其累加的数据包括长度、站点地址、设备地址、命令、数据域
     *
     * @return byte
     */
    public byte[] toByteArray() {
        // 基础数据
        byte[] byteArray = this.dataToByteArray();
        byte[] bs = new byte[byteArray.length + 2];
        ProtocolUtil.putByteArrToBuffer(bs, byteArray, 0);

        // 计算校验码
        int checksum = 0;
        for (byte b : byteArray) {
            // 将有符号的字节转换为无符号整数
            int unsignedByte = b & 0xFF;
            checksum += unsignedByte;
        }
        checksum = (((checksum & 0xFF) << 8) | ((checksum >> 8) & 0xFF));
        ProtocolUtil.putShortToBuffer(bs, (short) checksum, byteArray.length);
        return bs;
    }

    public static void main(String[] args) {
//        byte[] byteArray = new byte[]{0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xBA, 0x02, 0x00, 0x00, 0x00, 0x00};
        byte[] byteArray = new byte[]{0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xBA, 0x00, 0x00, 0x00, 0x02, 0x00};
        int checksum = 0;
        for (byte b : byteArray) {
            checksum += b;
        }
        byte c = (byte) 0xC6;

        int cc = (((c & 0xFF) << 8) | ((c >> 8) & 0xFF));
        String hexString1 = Integer.toHexString(cc);


        checksum = (((checksum & 0xFF) << 8) | ((checksum >> 8) & 0xFF));
        String hexString = Integer.toHexString(checksum);


        short checksum1 = checksum(byteArray);
        String hexString2 = Integer.toHexString(checksum1);

        System.out.println(checksum);
    }

    public static short checksum(byte[] byteArray) {
        int sum = 0;

        for (byte b : byteArray) {
            int unsignedByte = b & 0xFF; // 将有符号的字节转换为无符号整数
            String hexValue = Integer.toHexString(unsignedByte); // 将字节转换为16进制字符串
            int hexSum = Integer.parseInt(hexValue, 16); // 将16进制字符串转换为整数
            sum += hexSum;
        }

        return (short) sum;
    }
}
