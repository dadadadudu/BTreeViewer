package com.zhouyu;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import static com.zhouyu.IndexPageUtilUtil.PAGE_NEW_INFIMUM;
import static com.zhouyu.IndexPageUtilUtil.PAGE_NEW_SUPREMUM;
import static com.zhouyu.PageUtil.PAGE_SIZE;

/**
 * 大都督周瑜（我的微信: dadudu6789，经常在朋友圈分享最新技术、职场心得、面试经验）
 */
public class PageDetailUtil {

    public static void main(String[] args) {
        String ibdFilePath = "/Users/dadudu/idea/cpp/mysql-server/mysql5.7_build/build_out/data/my_db/t2.ibd";
        int pageNo = 4;
        Path path = Paths.get(ibdFilePath);

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
            fileChannel.position((long) pageNo * PAGE_SIZE);
            fileChannel.read(byteBuffer);

            // 页面有多少条记录
            int nRecs = IndexPageUtilUtil.getNRecs(byteBuffer);
            System.out.println("当前页有多少条记录：" + nRecs);

            // 页类型
            int typeNo = PageUtil.getPageType(byteBuffer);
            PageType pageType = Arrays.stream(PageType.values()).filter(type -> type.getType() == typeNo).findFirst().get();

            System.out.println("当前页中的主键为：" + nRecs);
            if (PageType.FIL_PAGE_INDEX.equals(pageType)) {
                int currentOffset = PAGE_NEW_INFIMUM + IndexPageUtilUtil.getNextRecOffs(byteBuffer, PAGE_NEW_INFIMUM);
                while (currentOffset != PAGE_NEW_SUPREMUM) {
                    long id = ByteBufferUtil.machReadFrom8(byteBuffer, currentOffset) & 0x7fff;
                    System.out.println(id);
                    currentOffset = currentOffset + IndexPageUtilUtil.getNextRecOffs(byteBuffer, currentOffset);
                }

            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
