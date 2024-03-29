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
public class AlcoholGetDetectionRecordResMO extends AlcoholBaseMO {

    /**
     * 记录状态
     * 0，有效;1，无此记录
     * 0
     */
    private int status;

    /**
     * 记录号
     * lsb
     * 1-4
     */
    private int num;

    /**
     * 工号
     * lsb
     * 5-8
     */
    private int workCode;

    /**
     * 测酒值
     * LSB，单位 mg/100mL
     * 9-10
     */
    private int alcoholContent;

    /**
     * 测酒结果
     * 0 正常，1 饮酒，2 醉酒
     * 11
     */
    private int result;

    /**
     * 日期
     * 年月日时分秒
     * 12-17
     */
    private LocalDateTime time;

    /**
     * 工作方式
     * 0，实时;1，出勤;2，退勤
     * 18
     */
    private int workMode;

    public AlcoholGetDetectionRecordResMO(AlcoholBaseMO message) {
        super(message.getSiteAddress(), message.getDeviceAddress(), message.getCommand(), message.getData());
        byte[] data = message.getData();
        if (data.length > 0) {
            byte status = data[0];
            this.setStatus(status);
            this.setNum(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 1, 4), false));
            if (status == 1) {
                return;
            }
            this.setWorkCode(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 5, 8), false));
            this.setAlcoholContent(ProtocolUtil.byteArrayToInt(ArrayUtil.sub(data, 9, 10), false));
            this.setResult(data[11]);
            this.setTime(LocalDateTime.of(data[12], data[13], data[14], data[15], data[16], data[17]));
            this.setWorkMode(data[18]);
        }
    }
}
