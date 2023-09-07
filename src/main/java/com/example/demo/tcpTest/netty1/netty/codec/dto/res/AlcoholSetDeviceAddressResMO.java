package com.example.demo.tcpTest.netty1.netty.codec.dto.res;

import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 因为终端的站点地址或设备地址已经更改，且在无线通讯情况下，执行复位无线模块或者自复位，上位机可能 收不到该条应答。
 *
 * @author mc
 * @date 2023年09月05日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlcoholSetDeviceAddressResMO extends AlcoholBaseMO {

    /**
     * 固定0
     */
    private int zero;

    /**
     * 0 成功，其它为失败(可能存储错误)
     */
    private int status;

    public AlcoholSetDeviceAddressResMO(AlcoholBaseMO message) {
        super(message.getSiteAddress(), message.getDeviceAddress(), message.getCommand(), message.getData());
        byte[] data = message.getData();
        if (data.length > 0) {
            this.setZero(data[0]);
            this.setStatus(data[1]);
        }
    }
}
