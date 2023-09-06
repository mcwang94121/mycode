package com.example.demo.tcpTest.socket;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * TcpServer
 *
 * @author mc
 * @date 2023年07月11日
 */
@Slf4j
public class TcpServer {
    private static final int PORT = 4196;

    public static void main(String[] args) {
        try (ServerSocket serverSocket1 = new ServerSocket(PORT)) {
            System.out.println("服务器已启动，等待客户端连接...");
            while (true) {
                Socket clientSocket1 = serverSocket1.accept();
                System.out.println("客户端连接成功：" + clientSocket1.getInetAddress());
                // 启动一个线程处理客户端连接
                new ClientHandler(clientSocket1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // 获取Socket的输入流
                InputStream inputStream = clientSocket.getInputStream();

                // 定义一个字节数组来存储读取的数据
                byte[] buffer = new byte[256];

                // 从输入流中读取数据
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    // 在这里处理读取到的字节数据
                    // 例如，可以将字节数组转换为字符串
                    String hex = Integer.toHexString(bytesRead);
                    System.out.println("Received data: " + Arrays.toString(buffer));

                    int read = inputStream.read(new byte[2]);
                    // 读取帧头
                    byte header1 = buffer[0];
                    byte header2 = buffer[1];
                    if (header1 != (byte) 0xAA || header2 != (byte) 0xBB) {
                        // 如果帧头不匹配，则抛出异常并关闭连接
                        return;
                    }

                    // 站点地址到数据域字节数; 需要高低位颠倒
                    short length = buffer[2];

                    // 将高低位颠倒
                    length = (short) (((length & 0xFF) << 8) | ((length >> 8) & 0xFF));

                    /*// 读取站点地址、
                    int siteAddress = in.readShort();

                    // 设备地址、
                    int deviceAddress = in.readShort();

                    // 命令、
                    int command = in.readByte();

                    // 数据域 站点地址到数据域字节数;
                    // 由于对指定设备进行通讯时，该设备的帧头、站点地址、设备子地址等字段都为固定的值，长度=5+数据域长 度
                    byte[] data = new byte[length - 5];
                    in.readBytes(data);

                    // 计算校验码
                    // 校验码:一个累加和校验码，其累加的数据包括长度、站点地址、设备地址、命令、数据域
                    byte checksum = 0;
                    for (int i = 2; i < bytes - 2; i++) {
                        checksum += in.getByte(i);
                    }

                    byte checkCode = in.readByte();
                    // 检查校验码是否匹配
                    if (checksum != checkCode) {
                        // 校验失败，关闭连接或执行其他错误处理
//            log.error("AlcoholTcpMessageDecoder decode checksum!=checkCode :{},{}", checksum, checkCode);
                        return;
                    }*/

                    // 清空缓冲区
                    buffer = new byte[1024];
                }
                // 关闭Socket连接
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String stringToHex(String input) {
        StringBuilder hexString = new StringBuilder();

        for (char c : input.toCharArray()) {
            // 将字符的ASCII码值转换为十六进制字符串
            String hex = Integer.toHexString(c);

            // 如果是单个字符，前面补0以确保是两位十六进制
            if (hex.length() == 1) {
                hex = "0" + hex;
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}
