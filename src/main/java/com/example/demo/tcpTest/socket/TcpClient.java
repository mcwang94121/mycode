package com.example.demo.tcpTest.socket;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TcpClient
 *
 * @author mc
 * @date 2023年07月11日
 */
public class TcpClient {
    private static final String SERVER_HOST = "172.16.5.26";
    private static final int SERVER_PORT = 8890;
    public static final int HEARTBEAT_INTERVAL = 5000; // 心跳间隔时间，单位：毫秒

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("连接服务器成功：" + socket.getInetAddress());

            // 启动一个线程发送心跳消息
//            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//            executorService.scheduleAtFixedRate(() -> {
//                try {
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    writer.write("ping\n");
//                    writer.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }, 0, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
//            try {
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                writer.write("ping\n");
//                writer.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            // 接收服务端消息
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("接收到服务端消息：" + message);
                    // TODO: 在此处添加你想要的处理逻辑
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
