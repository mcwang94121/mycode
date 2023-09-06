package com.example.demo.tcpTest.netty1.netty.codec.dto.res;

import cn.hutool.core.util.ArrayUtil;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.joysuch.open.platform.common.util.ProtocolUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
public class AlcoholHeartbeatBaseMO extends AlcoholBaseMO {

    private static final long serialVersionUID = -2511684421620148962L;

    /**
     * 时间
     * 年月日时分秒
     * 0-5
     */
    private LocalDateTime time;

    /**
     * 终端状态
     * 0 空闲，1 测试中
     * 6
     */
    private int deviceStatus;

    /**
     * 探头通讯状态
     * 0 正常，1 无通讯
     * 7
     */
    private int probeCommunicationStatus;

    /**
     * 已存记录数量
     * 8-11
     */
    private int recordNumber;

    /**
     * 已存人员数量
     * 12-15
     */
    private int personNumber;

    public AlcoholHeartbeatBaseMO(AlcoholBaseMO message) {
        super(message.getSiteAddress(), message.getDeviceAddress(), message.getCommand(), message.getData());
        byte[] data = message.getData();
        if (data.length > 0) {
            this.setTime(LocalDateTime.of(data[0], data[1], data[2], data[3], data[4], data[5]));
            this.setDeviceStatus(data[6]);
            this.setProbeCommunicationStatus(data[7]);
            this.setRecordNumber(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 8, 11),false));
            this.setPersonNumber(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 12, 15),false));
        }
    }
}
