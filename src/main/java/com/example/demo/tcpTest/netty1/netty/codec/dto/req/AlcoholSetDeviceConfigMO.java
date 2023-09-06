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
public class AlcoholSetDeviceConfigMO implements Serializable {

    private static final long serialVersionUID = 3831809508025660741L;

    /**
     * 0 查询/设置
     * 0，查询;1 设置，设置时下方数据有效
     */
    private int type;
    /**
     * 1 韦根位数
     * 0,26 位;1,34 位
     */
    private int wiegandNumber;
    /**
     * 2 韦根PID码
     * 0，无;1，有
     */
    private int wiegandPid;
    /**
     * 3 显示单位
     * 0，mg/100mL;1,mg/L;2,g/210L
     */
    private int unit;
    /**
     * 4音量
     * 0~15,15 最大，0 最小
     */
    private int volume;
    /**
     * 5 是否主动上传记录
     * 1，否;2，是;注:主动上传记录只上传一次
     */
    private int autoUpload;

    public byte[] toByteArray() {
        byte[] bs = new byte[6];
        int index = 0;
        ProtocolUtil.putByteToBuffer(bs, (byte) type, index);

        index += 1;
        ProtocolUtil.putByteToBuffer(bs, (byte) wiegandNumber, index);

        index += 1;
        ProtocolUtil.putByteToBuffer(bs, (byte) wiegandPid, index);

        index += 1;
        ProtocolUtil.putByteToBuffer(bs, (byte) unit, index);

        index += 1;
        ProtocolUtil.putByteToBuffer(bs, (byte) volume, index);

        index += 1;
        ProtocolUtil.putByteToBuffer(bs, (byte) autoUpload, index);
        return bs;
    }
}
