package com.example.demo.tcpTest.netty1.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholDetectionRecordsMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
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

    public static Map<String, Object> cmdResult = new ConcurrentHashMap<>();
    public static Map<String, AlcoholHeartbeatBaseMO> ipRecordNumMapping = new ConcurrentHashMap<>();

    public static final String KEY_TEMPLATE = "{}_{}";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AlcoholBaseMO message) {
        AlcoholCommandEnum command = AlcoholCommandEnum.findByCommand(message.getCommand());
        if (null != command) {
            InetSocketAddress remoteAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            String clientIp = remoteAddress.getAddress().getHostAddress();
            String key = getKey(clientIp, command);
            cmdResult.put(key,message.getData());
            switch (command) {
                case HEARTBEAT:
                    AlcoholHeartbeatBaseMO heartbeatBaseMO = new AlcoholHeartbeatBaseMO(message);
                    ipRecordNumMapping.put(clientIp,heartbeatBaseMO);
//                    log.info("AlcoholMessageHandler channelRead0 HEARTBEAT message:{}", heartbeatBaseMO);
                    break;
                case ALCOHOL_DETECTION:
                    break;
                case SET_DEVICE_ADDRESS:
                    break;
                case GET_DETECTION_RECORDS:
                    AlcoholDetectionRecordsMO recordsMO = new AlcoholDetectionRecordsMO(message);
                    log.info("AlcoholMessageHandler channelRead0 GET_DETECTION_RECORDS message:{}", recordsMO);
                    break;
                default:
                    break;
            }
        }
    }

    public static String getKey(String ip, AlcoholCommandEnum alcoholCommandEnum) {
        return StrUtil.format(KEY_TEMPLATE, ip, alcoholCommandEnum.getCode());
    }
}
