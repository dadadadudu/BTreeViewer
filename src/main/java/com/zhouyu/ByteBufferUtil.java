package com.zhouyu;

import cn.hutool.core.util.ByteUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
public class ByteBufferUtil {

    public static int machReadFrom2(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[2];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return (((int) (b[0]) << 8) | ByteUtil.byteToUnsignedInt(b[1]));
    }

    public static int machReadFrom4(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[4];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return ByteUtil.bytesToInt(b, ByteOrder.BIG_ENDIAN);
    }

    public static long machReadFrom8(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[8];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return ByteUtil.bytesToLong(b, ByteOrder.BIG_ENDIAN);
    }

}
