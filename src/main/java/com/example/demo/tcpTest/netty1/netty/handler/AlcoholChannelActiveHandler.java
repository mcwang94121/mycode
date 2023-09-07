package com.example.demo.tcpTest.netty1.netty.handler;

import cn.hutool.core.util.ObjectUtil;
import com.example.demo.tcpTest.netty1.netty.AlcoholDetectionChannelManager;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholBaseMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.req.AlcoholSetDeviceAddressReqMO;
import com.example.demo.tcpTest.netty1.netty.codec.dto.res.AlcoholHeartbeatBaseResMO;
import com.example.demo.tcpTest.netty1.netty.eunms.AlcoholCommandEnum;
import com.example.demo.tcpTest.netty1.netty.utils.AlcoholUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * AlcoholTcpChannelHandler
 *
 * @author mc
 * @date 2023年09月04日
 */
@Slf4j
public class AlcoholChannelActiveHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        String clientIp = remoteAddress.getAddress().getHostAddress();
        AlcoholDetectionChannelManager.getInstance().bindChannel(clientIp, channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        String clientIp = remoteAddress.getAddress().getHostAddress();
        AlcoholDetectionChannelManager.getInstance().unBindChannel(clientIp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常，这里可以记录日志或采取其他措施
        cause.printStackTrace();
        // 关闭连接或采取其他操作
        // ctx.close();
    }
}
