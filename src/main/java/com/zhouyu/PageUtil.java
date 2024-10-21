package com.zhouyu;

import lombok.Data;

import java.nio.ByteBuffer;

import static com.zhouyu.ByteBufferUtil.machReadFrom2;
import static com.zhouyu.ByteBufferUtil.machReadFrom4;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
@Data
public class PageUtil {

    public static final int PAGE_SIZE = 16 * 1024;

    private final static int FIL_PAGE_SPACE_OR_CHKSUM = 0;
    private final static int FIL_PAGE_OFFSET = 4;
    private final static int FIL_PAGE_PREV = 8;
    private final static int FIL_PAGE_NEXT = 12;
    private final static int FIL_PAGE_LSN = 16;
    private final static int FIL_PAGE_TYPE = 24;
    private final static int FIL_PAGE_FILE_FLUSH_LSN = 26;
    private final static int FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID = 34;


    public static int getPageNo(ByteBuffer pageByteBuffer) {
        return machReadFrom4(pageByteBuffer, FIL_PAGE_OFFSET);
    }

    public static int getSpaceId(ByteBuffer pageByteBuffer) {
        return machReadFrom4(pageByteBuffer, FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID);
    }

    public static int getPageType(ByteBuffer pageByteBuffer) {
        return machReadFrom2(pageByteBuffer, FIL_PAGE_TYPE);
    }

    public static int getNextPage(ByteBuffer pageByteBuffer) {
        return machReadFrom4(pageByteBuffer, FIL_PAGE_NEXT);
    }

    public static int getPrevPage(ByteBuffer pageByteBuffer) {
        return machReadFrom4(pageByteBuffer, FIL_PAGE_PREV);
    }

}
