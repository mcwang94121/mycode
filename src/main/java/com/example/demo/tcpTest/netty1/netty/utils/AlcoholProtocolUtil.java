package com.example.demo.tcpTest.netty1.netty.utils;

import com.joysuch.open.platform.common.util.ProtocolUtil;

/**
 * AlcoholProtocolUtil
 *
 * @author mc
 * @date 2023年09月06日
 */
public class AlcoholProtocolUtil {


    /**
     * 字节数组转换成long类型
     *
     * @param array     字节数组
     * @param bigEndian 是否大端序 {@code true} 大端序, {@code false} 小端序
     * @return long类型数字
     */
    public static long byteArrayToLong(byte[] array, int beginIdx, int endIdx, boolean bigEndian) {

        checkByteArray(new byte[endIdx - beginIdx], Long.BYTES);

        long value = 0;

        if (bigEndian) {
            // 大端序
            for (int i = beginIdx; i <= endIdx; i++) {
                // 必须先强制转换成{@code long}类型, 如果{@code byte}类型左移会导致变成0的情况
                value |= (long) (array[i] & 0x0FF) << ((endIdx - 1 - i) * Byte.SIZE);
            }
        } else {
            // 小端序
            for (int i = beginIdx; i <= endIdx; i++) {
                // 必须先强制转换成{@code long}类型, 如果{@code byte}类型左移会导致变成0的情况
                value |= (long) (array[i] & 0x0FF) << (i * Byte.SIZE);
            }
        }

        return value;
    }

    /**
     * 检查字节数组合法性
     *
     * @param array 字节数组
     * @param bytes 字节长度 (eg. {@code long}=8, {@code int}=4, {@code short}=2, {@code byte}=1)
     */
    private static void checkByteArray(byte[] array, int bytes) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("array must be not empty.");
        }

        if (array.length > bytes) {
            throw new IllegalArgumentException("expected: array.length <= " + bytes + ", but was: " + array.length);
        }
    }

}
