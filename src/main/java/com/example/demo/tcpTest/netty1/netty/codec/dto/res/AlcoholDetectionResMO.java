package com.example.demo.tcpTest.netty1.netty.codec.dto.res;

import cn.hutool.core.util.ArrayUtil;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.joysuch.open.platform.common.util.ProtocolUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AlcoholTcpHeartbeatMessage
 * 终端心跳返回
 *
 * @author mc
 * @date 2023年09月04日
 */
@Getter
@Setter
@ToString
public class AlcoholDetectionResMO extends AlcoholBaseMO {

    /**
     * 接收状态
     * 0，成功;非 0 失败
     * 0
     */
    private int status;

    /**
     * 工号
     * lsb
     * 1-4
     */
    private int workCode;

    public AlcoholDetectionResMO(AlcoholBaseMO message) {
        super(message.getSiteAddress(), message.getDeviceAddress(), message.getCommand(), message.getData());
        byte[] data = message.getData();
        if (data.length > 0) {
            byte status = data[0];
            this.setStatus(status);
            this.setWorkCode(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 1, 4), false));
        }
    }
}
