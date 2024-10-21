package com.zhouyu;

import java.nio.ByteBuffer;

import static com.zhouyu.ByteBufferUtil.*;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
public class IndexPageUtilUtil extends PageUtil {


    private final static int PAGE_HEADER = 38;
    private final static int PAGE_N_DIR_SLOTS = 0;
    private final static int PAGE_HEAP_TOP = 2;
    private final static int PAGE_N_HEAP = 4;

    private final static int PAGE_LEVEL = 26;
    private final static int PAGE_INDEX_ID = 28;

    private final static int FSEG_HEADER_SIZE = 10;


    private final static int PAGE_DATA = (PAGE_HEADER + 36 + 2 * FSEG_HEADER_SIZE);

    public final static int PAGE_NEW_INFIMUM = (PAGE_DATA + 5);
    public final static int PAGE_NEW_SUPREMUM = (PAGE_DATA + 18);

    public final static int REC_NEXT = 2;

    public static int getHeapTop(ByteBuffer pageByteBuffer) {
        return machReadFrom2(pageByteBuffer, PAGE_HEADER + PAGE_HEAP_TOP);
    }

    public static int getNHeap(ByteBuffer pageByteBuffer) {
        return machReadFrom2(pageByteBuffer, PAGE_HEADER + PAGE_N_HEAP) & 0x7fff;
    }

    public static int getLevel(ByteBuffer pageByteBuffer) {
        return machReadFrom2(pageByteBuffer, PAGE_HEADER + PAGE_LEVEL);
    }

    public static long getIndexId(ByteBuffer pageByteBuffer) {
        return machReadFrom8(pageByteBuffer, PAGE_HEADER + PAGE_INDEX_ID);
    }

    public static int getNextRecOffs(ByteBuffer pageByteBuffer, int rec) {
        return machReadFrom2(pageByteBuffer, rec - REC_NEXT);
    }


}
