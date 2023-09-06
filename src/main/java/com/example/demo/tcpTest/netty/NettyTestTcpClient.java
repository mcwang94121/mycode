package com.example.demo.tcpTest.netty;


import com.example.demo.tcpTest.netty.codec.NettyTestDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 功能描述:blg server
 *
 * @author mc
 * @auther: seekcy
 * @date: 2019/8/1 16:44
 */
@Slf4j
@Component
public class NettyTestTcpClient {

    private static final String SERVER_HOST = "172.16.5.26";
    private static final int SERVER_PORT = 8890;
    private static final int HEARTBEAT_INTERVAL = 5; // 心跳间隔时间，单位：秒

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyTestTcpClient.HeartbeatHandler());
                            ch.pipeline().addLast(new NettyTestDecoder());
                        }
                    });

            ChannelFuture future = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync();
            Channel channel = future.channel();

            // 定时发送心跳消息
//            channel.eventLoop().scheduleAtFixedRate(() -> {
//                if (channel.isActive()) {
//                    channel.writeAndFlush("ping\n");
//                }
//            }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class HeartbeatHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new NettyTestTcpClient.HeartbeatDecoder());
            ch.pipeline().addLast(new NettyTestTcpClient.HeartbeatHandlerAdapter());
        }
    }

    private static class HeartbeatDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            // 将ByteBuf转换为字符串，并移除换行符
            String message = in.toString(CharsetUtil.UTF_8).trim();
            out.add(message);
        }
    }

    private static class HeartbeatHandlerAdapter extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String message) {
            if ("pong".equals(message)) {
                System.out.println("Received heartbeat response from server.");
            } else {
                // 处理其他消息
                System.out.println("Received message from server: " + message);
            }
        }
    }
}
