package com.example.demo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.demo.service.AlcoholDetectionService;
import com.example.demo.tcpTest.netty1.netty.AlcoholDetectionChannelManager;
import com.example.demo.tcpTest.netty1.netty.codec.dto.AlcoholPushDTO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholDetectionMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholGetDetectionMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholSetDeviceAddressMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import com.example.demo.tcpTest.netty1.netty.handler.AlcoholMessageHandler;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * alcoholDetectionService
 *
 * @author mc
 * @date 2023年09月05日
 */
@Slf4j
@Service
public class AlcoholDetectionServiceImpl implements AlcoholDetectionService {

    @Override
    public void push(AlcoholPushDTO dto) throws InterruptedException {
        Channel channel = AlcoholDetectionChannelManager.getInstance().getChannel(dto.getIp());
        Map<String, AlcoholHeartbeatBaseMO> ipRecordNumMapping = AlcoholMessageHandler.ipRecordNumMapping;
        AlcoholCommandEnum byCommand = AlcoholCommandEnum.findByCode(dto.getCmd());
        if (null == channel) {
            return;
        }
        if (byCommand == null) {
            return;
        }
        AlcoholBaseMO messageMo = new AlcoholBaseMO(
                0, 0, byCommand.getCommand(), null
        );
        switch (byCommand) {
            case ALCOHOL_DETECTION:
                messageMo.setData(new AlcoholDetectionMO(2, 0).toByteArray());
                break;
            case SET_DEVICE_ADDRESS:
                messageMo.setData(new AlcoholSetDeviceAddressMO(1, 1, 1, 1).toByteArray());
                break;
            case GET_DETECTION_RECORDS:
                AlcoholHeartbeatBaseMO heartbeatBaseMO = ipRecordNumMapping.get(dto.getIp());
                if (ObjectUtil.isNotNull(heartbeatBaseMO)) {
                    messageMo.setData(new AlcoholGetDetectionMO(heartbeatBaseMO.getRecordNumber()).toByteArray());
                }
                break;
            default:
                break;
        }
        channel.writeAndFlush(messageMo);
        Thread.sleep(2000);
        Map<String, Object> cmdResult = AlcoholMessageHandler.cmdResult;
        Object o = cmdResult.get(AlcoholMessageHandler.getKey(dto.getIp(), byCommand));
        log.info("AlcoholDetectionServiceImpl push cmdResult:{}", o);
    }

    public void alcoholDetection(String ip, Integer workCode, Long timeout) {
        AlcoholCommandEnum byCommand = AlcoholCommandEnum.ALCOHOL_DETECTION;

        byte[] data = new AlcoholDetectionMO(workCode, 0).toByteArray();
        Channel channel = AlcoholDetectionChannelManager.getInstance().getChannel(ip);
        if (null == channel) {
            return;
        }

        AlcoholBaseMO messageMo = new AlcoholBaseMO(
                0, 0, byCommand.getCommand(), data
        );
        channel.writeAndFlush(messageMo);
    }
}
