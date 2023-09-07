package com.example.demo.tcpTest.netty1.netty.utils;

import cn.hutool.core.util.StrUtil;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AlcoholUtil
 *
 * @author mc
 * @date 2023年09月07日
 */
public class AlcoholUtil {

    private static final String KEY_TEMPLATE = "{}_{}";

    /**
     * 根据设备ip和指令类型获取缓存key
     *
     * @param ip                 ip
     * @param alcoholCommandEnum 指令类型
     * @return key
     */
    public static String getAlcoholTypeKey(String ip, AlcoholCommandEnum alcoholCommandEnum) {
        return StrUtil.format(KEY_TEMPLATE, ip, alcoholCommandEnum.getCode());
    }

    /**
     * 根据规则获取站点和设备地址
     *
     * @param ip ip
     * @return 站点和设备地址
     */
    public static SitAndDeviceAddr getSitAddressAndDeviceAddress(String ip) {
        if (StrUtil.isBlank(ip)) {
            return null;
        }
        String[] split = ip.split("\\.");
        if (split.length < 4) {
            return null;
        }
        return new SitAndDeviceAddr(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }

    @Data
    @AllArgsConstructor
    public static class SitAndDeviceAddr{
        private int sitAddress;
        private int deviceAddress;
    }

}
