package com.example.demo.tcpTest.netty1.netty.handler;

import cn.hutool.core.util.ObjectUtil;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholSetDeviceAddressReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholDetectionResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholGetDetectionRecordResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseResMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholSetDeviceAddressResMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import com.example.demo.tcpTest.netty1.netty.utils.AlcoholUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NettyTest1Handler
 * 酒精值解析: 高低位颠倒,转十进制*0.1舍掉小数位
 *
 * @author mc
 * @date 2023年09月01日
 */
@Slf4j
public class AlcoholMessageHandler extends SimpleChannelInboundHandler<AlcoholBaseMO> {

    private static final Map<String, Object> CMD_RESULT = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AlcoholBaseMO message) {
        AlcoholCommandEnum command = AlcoholCommandEnum.findByCommand(message.getCommand());
        if (null != command) {
            InetSocketAddress remoteAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            String clientIp = remoteAddress.getAddress().getHostAddress();
            String key = AlcoholUtil.getAlcoholTypeKey(clientIp, command);
            Object resObj = null;
            switch (command) {
                case HEARTBEAT:
                    resObj = new AlcoholHeartbeatBaseResMO(message);
                    if (ObjectUtil.isNull(CMD_RESULT.get(key))) {
                        // 设备首次上报心跳,检测并设置站点,设备地址
                        if (ObjectUtil.isNull(message) || (message.getSiteAddress() == 0 || message.getDeviceAddress() == 0)) {
                            setDeviceAddress(clientIp, channelHandlerContext.channel());
                        }
                        log.info("AlcoholMessageHandler channelRead0 first heartbeat :{}", message);
                    }
                    break;
                case ALCOHOL_DETECTION:
                    resObj = new AlcoholDetectionResMO(message);
                    break;
                case SET_DEVICE_ADDRESS:
                    resObj = new AlcoholSetDeviceAddressResMO(message);
                    break;
                case GET_DETECTION_RECORDS:
                    resObj = new AlcoholGetDetectionRecordResMO(message);
                    break;
                default:
                    break;
            }
            CMD_RESULT.put(key, resObj);
        }
    }

    public static <T> T getCmdResult(String ip, AlcoholCommandEnum alcoholCommandEnum, Class<T> resultType) {
        Object o = CMD_RESULT.get(AlcoholUtil.getAlcoholTypeKey(ip, alcoholCommandEnum));
        if (ObjectUtil.isNull(o)) {
            return null;
        }

        if (resultType.isInstance(o)) {
            return resultType.cast(o);
        } else {
            throw new ClassCastException("Type mismatch");
        }
    }

    /**
     * 首次连接需要设置站点与设备地址
     *
     * @param ip      客户端ip
     * @param channel 客户端连接通道
     */
    private void setDeviceAddress(String ip, Channel channel) {
        if (ObjectUtil.isNull(channel)) {
            return;
        }
        AlcoholUtil.SitAndDeviceAddr sitAddressAndDeviceAddress = AlcoholUtil.getSitAddressAndDeviceAddress(ip);
        if (ObjectUtil.isNull(sitAddressAndDeviceAddress)) {
            return;
        }
        int sitAddr = sitAddressAndDeviceAddress.getSitAddress();
        int deviceAddr = sitAddressAndDeviceAddress.getDeviceAddress();
        AlcoholBaseMO messageMo = new AlcoholBaseMO(
                0, 0,
                AlcoholCommandEnum.SET_DEVICE_ADDRESS.getCommand(),
                new AlcoholSetDeviceAddressReqMO(1, sitAddr, deviceAddr, 1).toByteArray()
        );
        channel.writeAndFlush(messageMo);
    }

}
