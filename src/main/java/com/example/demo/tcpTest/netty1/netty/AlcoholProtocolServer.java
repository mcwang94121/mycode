package com.example.demo.tcpTest.netty1.netty;

import com.example.demo.tcpTest.netty1.netty.codec.AlcoholMessageDecoder;
import com.example.demo.tcpTest.netty1.netty.codec.AlcoholMessageEncoder;
import com.example.demo.tcpTest.netty1.netty.handler.AlcoholChannelActiveHandler;
import com.example.demo.tcpTest.netty1.netty.handler.AlcoholMessageHandler;
import com.example.demo.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * AlcoholTcpProtocolServer
 *
 * @author mc
 * @date 2023年09月01日
 */
@Slf4j
@Component
public class AlcoholProtocolServer {

    private static final int SERVER_PORT = 4196;
    private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;

    public static void main(String[] args) throws InterruptedException {
        start();
    }

    /**
     * 开始服务
     */
    @PostConstruct
    public static void start() {
        bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("alcohol-detection-boss"));
        workerGroup = new NioEventLoopGroup(2, new NamedThreadFactory("alcohol-detection-worker"));
        init(bossGroup, workerGroup);
    }

    /**
     * 结束任务
     */
    public void stop() {
        if (Objects.nonNull(bossGroup)) {
            bossGroup.shutdownGracefully();
        }
        if (Objects.nonNull(workerGroup)) {
            workerGroup.shutdownGracefully();
        }
    }

    public static void init(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        int port = SERVER_PORT;
        log.info("测酒仪 AlcoholTcpProtocolServer 绑定端口: " + port);
        //
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                // 保活机制 正常应为true 这里因为有心跳上报逻辑,所以设置为false
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new AlcoholMessageDecoder());
                        ch.pipeline().addLast(new AlcoholMessageHandler());
                        ch.pipeline().addLast(new AlcoholChannelActiveHandler());
                        ch.pipeline().addLast(new AlcoholMessageEncoder());
                    }
                })
                .bind(port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("AlcoholTcpProtocolServer protocol server startup success");
                    } else {
                        log.error("AlcoholTcpProtocolServer protocol server startup failed", future.cause());
                    }
                });
        serverBootstrap.childOption(ChannelOption.SO_RCVBUF, 256);
    }
}
