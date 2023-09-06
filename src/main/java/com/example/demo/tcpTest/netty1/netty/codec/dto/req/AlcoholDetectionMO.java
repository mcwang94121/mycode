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
public class AlcoholDetectionMO implements Serializable {

    private static final long serialVersionUID = -6923987431170378840L;

    /**
     * 工号
     * 4字节纯数字
     */
    private int workCode;

    /**
     * 工作方式
     * 0，实时;1，出勤;2，退勤
     */
    private int workMode;

    public byte[] toByteArray() {
        byte[] bs = new byte[5];
        int index = 0;
        ProtocolUtil.putByteArrToBuffer(bs, ProtocolUtil.intToByteArray(workCode,true), index);
        index += 4;
        ProtocolUtil.putByteToBuffer(bs, (byte) workMode, index);
        return bs;
    }
}
