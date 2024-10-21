package com.zhouyu;

import java.nio.ByteBuffer;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
public class FspHdrPageUtilUtil extends PageUtil {

    private final static int FSP_HEADER = 38;
    private final static int FSP_SPACE_ID = 0;
    private final static int FSP_NOT_USED = 4;
    private final static int FSP_SIZE = 8;

    public static int get_fsp_size(ByteBuffer pageByteBuffer) {
        return ByteBufferUtil.machReadFrom4(pageByteBuffer, FSP_HEADER + FSP_SIZE);
    }

}
