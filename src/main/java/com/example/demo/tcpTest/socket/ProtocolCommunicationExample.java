package com.example.demo.tcpTest.socket;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * aa
 *
 * @author mc
 * @date 2023年09月01日
 */

public class ProtocolCommunicationExample {
    public static void main(String[] args) {
        try {
            // 创建Socket连接
            Socket socket = new Socket("172.16.5.26", 8890);

            // 获取输入输出流
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            // 构造数据包
            byte[] packet = constructPacket((short) 1, (short) 2, (byte) 0x01, new byte[]{0x01, 0x02, 0x03});

            // 发送数据包
//            outputStream.write(packet);

            // 接收响应
            byte[] response = new byte[1024];
            int bytesRead = inputStream.read(response);

            // 解析响应数据包
            if (bytesRead > 0) {
                byte[] responseData = new byte[bytesRead];
                System.arraycopy(response, 0, responseData, 0, bytesRead);
                parseResponse(responseData);
            }

            // 关闭连接
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] constructPacket(short siteAddress, short deviceAddress, byte command, byte[] data) throws IOException {
        // 构造数据包
        ByteArrayOutputStream packetStream = new ByteArrayOutputStream();
        packetStream.write(new byte[]{(byte) 0xAA, (byte) 0xBB});
        packetStream.write(0); // 预留长度，后续计算
        packetStream.write((byte) (siteAddress >> 8));
        packetStream.write((byte) siteAddress);
        packetStream.write((byte) (deviceAddress >> 8));
        packetStream.write((byte) deviceAddress);
        packetStream.write(command);
        packetStream.write(data.length);
        packetStream.write(data, 0, data.length);

        byte[] packetData = packetStream.toByteArray();
        packetData[1] = (byte) (packetData.length - 3); // 设置长度
        // 计算校验码
        byte checksum = 0;
        for (byte b : packetData) {
            checksum += b;
        }
        packetStream.write(checksum);

        return packetStream.toByteArray();
    }

    private static void parseResponse(byte[] responseData) {
        // 解析响应数据包
        // 根据协议字段解析数据包内容
    }
}
