package com.example.demo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.demo.service.AlcoholDetectionService;
import com.example.demo.tcpTest.netty1.netty.AlcoholDetectionChannelManager;
import com.example.demo.tcpTest.netty1.netty.codec.dto.AlcoholPushDTO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholDetectionReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholGetDetectionRecordReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholSetDeviceAddressReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholGetDetectionRecordResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseResMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import com.example.demo.tcpTest.netty1.netty.handler.AlcoholMessageHandler;
import com.example.demo.tcpTest.netty1.netty.utils.AlcoholUtil;
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
    public Object push(AlcoholPushDTO dto) throws InterruptedException {
        Channel channel = AlcoholDetectionChannelManager.getInstance().getChannel(dto.getIp());
        AlcoholCommandEnum byCommand = AlcoholCommandEnum.findByCode(dto.getCmd());
        if (null == channel) {
            return null;
        }
        if (byCommand == null) {
            return null;
        }
        AlcoholBaseMO messageMo = new AlcoholBaseMO(
                0, 0, byCommand.getCommand(), null
        );
        switch (byCommand) {
            case ALCOHOL_DETECTION:
                AlcoholGetDetectionRecordResMO recordResMO = alcoholDetection(dto.getIp(), 2, 5000L);
                log.info("recordResMO : {}", recordResMO);
                return recordResMO;
            case SET_DEVICE_ADDRESS:
                messageMo.setData(new AlcoholSetDeviceAddressReqMO(1, 1, 1, 1).toByteArray());
                break;
            case GET_DETECTION_RECORDS:
                AlcoholHeartbeatBaseResMO heartbeatBaseMO = AlcoholMessageHandler.getCmdResult(
                        dto.getIp(),
                        AlcoholCommandEnum.HEARTBEAT,
                        AlcoholHeartbeatBaseResMO.class
                );
                if (ObjectUtil.isNotNull(heartbeatBaseMO)) {
                    messageMo.setData(new AlcoholGetDetectionRecordReqMO(heartbeatBaseMO.getRecordNumber()).toByteArray());
                }
                break;
            default:
                break;
        }
        channel.writeAndFlush(messageMo);
        Thread.sleep(2000);
        return AlcoholMessageHandler.getCmdResult(
                dto.getIp(),
                byCommand,
                Object.class
        );
    }

    /**
     * 下发酒测
     *
     * @param ip       设备ip
     * @param workCode 工号
     * @param timeout  超时时间
     * @return 酒测结果
     * @throws InterruptedException 中断的异常
     */
    public AlcoholGetDetectionRecordResMO alcoholDetection(String ip, int workCode, Long timeout) throws InterruptedException {
        byte[] data;
        AlcoholBaseMO messageMo;

        // 获取设备连接
        Channel channel = AlcoholDetectionChannelManager.getInstance().getChannel(ip);
        if (null == channel) {
            throw new RuntimeException("获取设备连接失败!");
        }

        // 获取站点,设备地址
        AlcoholUtil.SitAndDeviceAddr sitAddressAndDeviceAddress = AlcoholUtil.getSitAddressAndDeviceAddress(ip);
        if (null == sitAddressAndDeviceAddress) {
            throw new RuntimeException("站点设备地址解析失败!");
        }

        // 构建下发酒测指令
        data = new AlcoholDetectionReqMO(workCode, 0).toByteArray();
        messageMo = new AlcoholBaseMO(
                sitAddressAndDeviceAddress.getSitAddress(),
                sitAddressAndDeviceAddress.getDeviceAddress(),
                AlcoholCommandEnum.ALCOHOL_DETECTION.getCommand(), data
        );

        // 获取最新设备测酒记录条数
        AlcoholHeartbeatBaseResMO heartbeatBaseMO = AlcoholMessageHandler.getCmdResult(
                ip,
                AlcoholCommandEnum.HEARTBEAT,
                AlcoholHeartbeatBaseResMO.class);
        if (null == heartbeatBaseMO) {
            throw new RuntimeException("设备未上报过最新记录数!");
        }

        int recordNumber = heartbeatBaseMO.getRecordNumber();

        // 下发指令
        channel.writeAndFlush(messageMo);

        // 等待用户吹气测酒
        Thread.sleep(timeout);

        // 用户测酒完毕测试记录+1
        recordNumber += 1;

        // 构建获取测试记录指令
        data = new AlcoholGetDetectionRecordReqMO(recordNumber).toByteArray();
        messageMo = new AlcoholBaseMO(
                sitAddressAndDeviceAddress.getSitAddress(),
                sitAddressAndDeviceAddress.getDeviceAddress(),
                AlcoholCommandEnum.GET_DETECTION_RECORDS.getCommand(), data
        );
        // 下发获取记录指令
        channel.writeAndFlush(messageMo);

        // 等待指令返回
        Thread.sleep(200);

        // 获取测试记录返回
        AlcoholGetDetectionRecordResMO recordResMO = AlcoholMessageHandler.getCmdResult(
                ip,
                AlcoholCommandEnum.GET_DETECTION_RECORDS,
                AlcoholGetDetectionRecordResMO.class);
        if (ObjectUtil.isNull(recordResMO) || recordResMO.getStatus() == 1) {
            throw new RuntimeException("超时未测酒或测酒无效!");
        }
        if (recordResMO.getNum() != recordNumber) {
            throw new RuntimeException("超时未测酒!");
        }
        return recordResMO;
    }


}
