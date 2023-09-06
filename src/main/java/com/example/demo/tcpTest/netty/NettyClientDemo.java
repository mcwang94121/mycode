package com.example.demo.tcpTest.netty;

/**
 * aaa
 *
 * @author mc
 * @date 2023年09月01日
 */

import com.example.demo.tcpTest.netty.codec.NettyTestDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyClientDemo {

    private static final String HOST = "172.16.5.26";
//    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8890;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS)); // 添加心跳检测
                            ch.pipeline().addLast(new NettyTestDecoder());
                            ch.pipeline().addLast(new HardwareClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static class HardwareClientHandler extends SimpleChannelInboundHandler<byte[]> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
            // 在这里处理从硬件终端接收的数据
            // 解析数据包，处理响应等
            System.out.println(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    // 读取超时，即认为连接处于空闲状态，可以发送心跳包
                    // 编写发送心跳包的逻辑
                    System.out.println("编写发送心跳包的逻辑");
                }
            }
        }
    }
}
