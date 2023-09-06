package com.example.demo.service.impl;

import com.example.demo.service.AlcoholDetectionService;
import com.example.demo.tcpTest.netty1.netty.AlcoholDetectionChannelManager;
import com.example.demo.tcpTest.netty1.netty.codec.dto.AlcoholPushDTO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholDetectionMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


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
        Channel channel1 = AlcoholDetectionChannelManager.getInstance().getChannel(dto.getIp());
        AlcoholCommandEnum byCommand = AlcoholCommandEnum.findByCode(dto.getCmd());

        byte[] data = null;


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
