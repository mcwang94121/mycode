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
public class AlcoholGetDetectionRecordReqMO implements Serializable {

    private static final long serialVersionUID = -3071843061807048944L;

    private int detectionNum;

    public byte[] toByteArray() {
        byte[] bs = new byte[4];
        int index = 0;
        ProtocolUtil.putByteArrToBuffer(bs, ProtocolUtil.intToByteArray(detectionNum,false), index);
        return bs;
    }
}
