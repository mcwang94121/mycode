package com.example.demo.tcpTest.socket;

import cn.hutool.core.map.MapUtil;
import com.example.demo.tcpTest.netty.NettyTestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * masdasd
 *
 * @author mc
 * @date 2023年07月21日
 */
public class MultiServerTcpClient {

    public static Map<String, Integer> devices= MapUtil.newConcurrentHashMap();

    @PostConstruct
    public void init(){
        devices.put("",1231);
    }

    public void connectToServers() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // Server 1
            String host1 = "127.0.0.1";
            int port1 = 2019;
            connectToServer(group, host1, port1);

            // Server 2
            int port2 = 2018;
            connectToServer(group, host1, port2);

            // Add more servers as needed

            // Do not call group.shutdownGracefully() here
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the group after all connections are closed
            group.shutdownGracefully();
        }
    }

    private void connectToServer(EventLoopGroup group, String host, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new NettyTestHandler()); // 替换为你自己的客户端处理器
                    }
                });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        Channel channel = future.channel();
        if (channel.isActive()) {
            channel.writeAndFlush(port + ": ping\n");
        }
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        MultiServerTcpClient client = new MultiServerTcpClient();
        client.connectToServers();
    }
}
