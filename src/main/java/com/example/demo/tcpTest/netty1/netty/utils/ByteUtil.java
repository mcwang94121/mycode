package com.example.demo.tcpTest.netty1.netty.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * 功能描述:
 *
 * @auther: seekcy
 * @date: 2019/8/1 16:34
 */
public class ByteUtil {


    public static void main(String[] args) throws UnknownHostException {
//        testIP();
        InetAddress targetAddress = InetAddress.getByName("172.16.5.27");
        System.out.println(targetAddress);
    }

    private static void testIP() {
        try {
            // 获取本机的所有网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // 排除回环接口和虚拟接口
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }

                // 获取网络接口的所有 IP 地址
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    String hostAddress = address.getHostAddress();
                    // 获取子网掩码长度
                    short subnetMaskLength = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();

                    // 计算同一子网的所有 IP 地址
                    for (int i = 1; i <= 254; i++) {
                        String targetIP = hostAddress.substring(0, hostAddress.lastIndexOf('.') + 1) + i;
                        InetAddress targetAddress = InetAddress.getByName(targetIP);

                        // 尝试连接目标 IP 地址
                        if (targetAddress.isReachable(100)) {
                            System.out.println("设备在线：" + targetIP);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final byte[] HEX = "0123456789ABCDEF".getBytes();

    public static byte[] get6Bytes(long data) {
        byte[] bytes = new byte[6];
        bytes[5] = (byte) (data & 0xff);
        bytes[4] = (byte) ((data >> 8) & 0xff);
        bytes[3] = (byte) ((data >> 16) & 0xff);
        bytes[2] = (byte) ((data >> 24) & 0xff);
        bytes[1] = (byte) ((data >> 32) & 0xff);
        bytes[0] = (byte) ((data >> 40) & 0xff);
        return bytes;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public static byte[] intToByteArray(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++) {
            byteArray[3 - n] = (byte) (integer >>> (n * 8));
        }
        return (byteArray);
    }

    private static int parse(char c) {
        if (c >= 'a') {
            return (c - 'a' + 10) & 0x0f;
        }
        if (c >= 'A') {
            return (c - 'A' + 10) & 0x0f;
        }
        return (c - '0') & 0x0f;
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] hexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    public static String bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = HEX[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = HEX[b[i] & 0x0f];
        }
        return new String(buff);
    }

    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }

    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLongBy8Bytes(byte[] bytes) {
        return (0xffL & (long) bytes[7]) | (0xff00L & ((long) bytes[6] << 8)) | (0xff0000L & ((long) bytes[5] << 16))
                | (0xff000000L & ((long) bytes[4] << 24)) | (0xff00000000L & ((long) bytes[3] << 32))
                | (0xff0000000000L & ((long) bytes[2] << 40)) | (0xff000000000000L & ((long) bytes[1] << 48))
                | (0xff00000000000000L & ((long) bytes[0] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLongBy8Bytes(bytes);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "GBK");
    }

    public static Byte[] copyBytes(byte[] bs) {
        Byte[] arr = new Byte[bs.length];
        for (int i = 0; i < bs.length; i++) {
            arr[i] = bs[i];
        }
        return arr;
    }

    public static byte[] copyBytes(Byte[] bs) {
        byte[] arr = new byte[bs.length];
        for (int i = 0; i < bs.length; i++) {
            arr[i] = bs[i];
        }
        return arr;
    }

}
