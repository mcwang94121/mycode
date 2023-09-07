package com.example.demo.tcpTest.netty1.netty.codec.dto.req;

import com.joysuch.open.platform.common.util.ProtocolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AlcoholTcpPushMessage
 *
 * @author mc
 * @date 2023年09月05日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlcoholSetDeviceAddressReqMO implements Serializable {

    private static final long serialVersionUID = 6682193002684955730L;

    /**
     * 0，查询;1 设置，设置时下方数据有效
     */
    private int type;

    /**
     * 站点地址:每个站点都有一个唯一的地址，值范围 1~65535;
     * LSB，值范围 0~65535，设置状态时有效
     */
    private int siteAddress;

    /**
     * 设备地址:终端设备的地址，值范围 1~65535;
     * LSB，值范围 0~65535，设置状态时有效
     */
    private int deviceAddress;

    /**
     * 0 不复位，1 复位，设置状态时有效
     */
    private int reposition;

    public byte[] toByteArray() {
        byte[] bs = new byte[6];
        int index = 0;
        ProtocolUtil.putByteToBuffer(bs, (byte) type, index);

        index += 1;
        ProtocolUtil.putShortToBuffer(bs, (short) siteAddress, index);

        index += 2;
        ProtocolUtil.putShortToBuffer(bs, (short) deviceAddress, index);

        index += 2;
        ProtocolUtil.putByteToBuffer(bs, (byte) reposition, index);
        return bs;
    }
}
