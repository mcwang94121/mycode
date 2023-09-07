package com.example.demo.tcpTest.netty1.netty.eunms;

import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholDetectionReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholGetDetectionRecordReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholSetDeviceAddressReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholDetectionResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholGetDetectionRecordResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholSetDeviceAddressResMO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * AlcoholCmdEnum
 *
 * @author mc
 * @date 2023年09月04日
 */
public enum AlcoholCommandEnum {

    /**
     * 终端心跳参数
     */
    HEARTBEAT(0, (byte) 0x0F, "终端心跳参数", null, AlcoholHeartbeatBaseResMO.class),

    /**
     * 主动控制进入测酒
     */
    ALCOHOL_DETECTION(1, (byte) 0xBA, "主动控制进入测酒", AlcoholDetectionReqMO.class, AlcoholDetectionResMO.class),

    /**
     * 获取记录数据
     */
    GET_DETECTION_RECORDS(2, (byte) 0xB4, "获取记录数据", AlcoholGetDetectionRecordReqMO.class, AlcoholGetDetectionRecordResMO.class),

    /**
     * 设置终端站点与设备地址
     */
    SET_DEVICE_ADDRESS(3, (byte) 0xB2, " 站点与设备地址", AlcoholSetDeviceAddressReqMO.class, AlcoholSetDeviceAddressResMO.class),
    ;

    @Getter
    private final int code;
    @Getter
    private final int command;
    @Getter
    private final String desc;
    @Getter
    private final Class<?> reqClazz;
    @Getter
    private final Class<?> resClazz;

    AlcoholCommandEnum(int code, byte command, String desc, Class<?> reqClazz, Class<?> resClazz) {
        this.code = code;
        this.command = command;
        this.desc = desc;
        this.reqClazz = reqClazz;
        this.resClazz = resClazz;
    }

    private static final Map<Integer, AlcoholCommandEnum> COMMAND_MAP = new HashMap<>();
    private static final Map<Integer, AlcoholCommandEnum> CODE_MAP = new HashMap<>();

    static {
        for (AlcoholCommandEnum v : values()) {
            COMMAND_MAP.put(v.getCommand(), v);
            CODE_MAP.put(v.getCode(), v);
        }
    }

    public static AlcoholCommandEnum findByCommand(int command) {
        return COMMAND_MAP.get(command);
    }

    public static AlcoholCommandEnum findByCode(int code) {
        return CODE_MAP.get(code);
    }
}
