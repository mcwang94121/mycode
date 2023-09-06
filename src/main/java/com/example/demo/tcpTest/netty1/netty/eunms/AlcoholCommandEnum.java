package com.example.demo.tcpTest.netty1.netty.eunms;

import lombok.Getter;

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
    HEARTBEAT(0, (byte) 0x0F, "终端心跳参数"),

    /**
     * 主动控制进入测酒
     */
    ALCOHOL_DETECTION(1, (byte) 0xBA, "主动控制进入测酒"),

    /**
     * 获取记录数据
     */
    GET_DETECTION_RECORDS(2, (byte) 0xB4, "获取记录数据"),

    /**
     * 设置终端站点与设备地址
     */
    SET_DEVICE_ADDRESS(3, (byte) 0xB2, " 站点与设备地址"),

    ;

    @Getter
    private final int code;
    @Getter
    private final int command;
    @Getter
    private final String desc;

    AlcoholCommandEnum(int code, byte command, String desc) {
        this.code = code;
        this.command = command;
        this.desc = desc;
    }

    public static AlcoholCommandEnum findByCommand(int command) {
        AlcoholCommandEnum[] values = AlcoholCommandEnum.values();
        for (AlcoholCommandEnum v : values) {
            if (v.getCommand() == command) {
                return v;
            }
        }
        return null;
    }

    public static AlcoholCommandEnum findByCode(int code) {
        AlcoholCommandEnum[] values = AlcoholCommandEnum.values();
        for (AlcoholCommandEnum v : values) {
            if (v.getCode() == code) {
                return v;
            }
        }
        return null;
    }

}
