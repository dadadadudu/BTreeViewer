package com.zhouyu;

import cn.hutool.core.collection.CollUtil;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkSource;
import guru.nidi.graphviz.model.Node;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zhouyu.IndexPageUtilUtil.PAGE_NEW_INFIMUM;
import static com.zhouyu.IndexPageUtilUtil.PAGE_NEW_SUPREMUM;
import static com.zhouyu.PageUtil.PAGE_SIZE;
import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
public class Main {

    private static final int ID_LENGTH = 8;  // 主键ID的长度，bigInt占8个字节

    public static void main(String[] args) {

        String ibdFilePath = "/Users/dadudu/idea/cpp/mysql-server/mysql5.7_build/build_out/data/my_db/t2.ibd";

        Path path = Paths.get(ibdFilePath);

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            int result = 1;
            long primaryKeyIndexId = 0;
            List<PageNode> primaryKeyList = new ArrayList<>();
            List<PageNode> totalList = new ArrayList<>();
            while (result > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
                result = fileChannel.read(byteBuffer);
                if (result == -1) break;
                byteBuffer.clear();

                PageNode pageNode = new PageNode();

                // 页号
                int pageNo = PageUtil.getPageNo(byteBuffer);
                pageNode.setPageNo(pageNo);

                // 页面有多少条记录
                int nRecs = IndexPageUtilUtil.getNRecs(byteBuffer);
                pageNode.setNRecs(nRecs);

                // 页类型
                int typeNo = PageUtil.getPageType(byteBuffer);
                PageType pageType = Arrays.stream(PageType.values()).filter(type -> type.getType() == typeNo).findFirst().get();
                pageNode.setPageType(pageType);

                if (PageType.FIL_PAGE_INDEX.equals(pageType)) {

                    // 叶子节点的level等于0
                    int level = IndexPageUtilUtil.getLevel(byteBuffer);
                    pageNode.setLevel(level);

                    // 索引id
                    long indexId = IndexPageUtilUtil.getIndexId(byteBuffer);
                    pageNode.setIndexId(indexId);

                    // 第一个Index页为主键索引的根页
                    if (primaryKeyIndexId == 0) {
                        primaryKeyIndexId = indexId;
                    }

                    // 如果某一页是非叶子节点
                    if (level > 0) {
                        List<NodePtr> nodePtrList = new ArrayList<>();

                        int currentOffset = PAGE_NEW_INFIMUM + IndexPageUtilUtil.getNextRecOffs(byteBuffer, PAGE_NEW_INFIMUM);
                        while (currentOffset != PAGE_NEW_SUPREMUM) {

                            // 目前只支持int类型（4个字节）的主键id
                            long id = ByteBufferUtil.machReadFrom8(byteBuffer, currentOffset) & 0x7fff;
                            int childPageNo = ByteBufferUtil.machReadFrom4(byteBuffer, currentOffset + ID_LENGTH);
                            NodePtr nodePtr = new NodePtr();
                            nodePtr.setId(id);
                            nodePtr.setChildPageNo(childPageNo);
                            nodePtrList.add(nodePtr);
                            currentOffset = currentOffset + IndexPageUtilUtil.getNextRecOffs(byteBuffer, currentOffset);
                        }

                        pageNode.setNodePtrList(nodePtrList);
                    }

                }

                // primaryKeyList记录主键索引中的所有页
                if (pageNode.getIndexId() != 0 && pageNode.getIndexId() == primaryKeyIndexId) {
                    primaryKeyList.add(pageNode);
                }

                // totalList记录表中的所有页
                totalList.add(pageNode);
            }

            // 按level进行排序
            primaryKeyList.sort((o1, o2) -> o2.getLevel() - o1.getLevel());

            List<LinkSource> linkSources = new ArrayList<>();
            for (PageNode pageNode : primaryKeyList) {
                List<NodePtr> nodePtrList = pageNode.getNodePtrList();
                if (CollUtil.isNotEmpty(nodePtrList)) {
                    for (NodePtr nodePtr : nodePtrList) {
                        String parentNodeName = String.format("pageNo=%s", pageNode.getPageNo());
                        String childNodeName = String.format("pageNo=%s, min_id=%s", nodePtr.getChildPageNo(), nodePtr.getId());
                        Node parentNode = node(parentNodeName).with(Records.of(rec("pageNo", pageNode.getPageNo().toString())))
//                                .with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
                        ;
                        Node childNode = node(childNodeName).with(Records.of(rec("minId", nodePtr.getId().toString()),
                                        rec("pageNo", nodePtr.getChildPageNo().toString())))
//                                .with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
                        ;
                        linkSources.add(parentNode.link(childNode));
                    }
                }
            }


            Graph g = graph("btree").directed()
                    .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                    .nodeAttr().with(Font.name("arial"))
                    .linkAttr().with("class", "link-class")
                    .with(linkSources);
            Graphviz.fromGraph(g).height(300).render(Format.PNG).toFile(new File("btree/result.png"));

            System.out.println(String.format("总共有多少页：%s页", totalList.size()));
            System.out.println(String.format("聚集索引B+树有多少页：%s页", primaryKeyList.size()));
            System.out.println(String.format("聚集索引B+树有多高：%s层", primaryKeyList.get(0).getLevel() + 1));

            System.out.println("聚集索引每页的详细情况：");
            for (PageNode pageNode : primaryKeyList) {
                System.out.println(pageNode);
            }

            System.out.println("表空间每页的详细情况：");
            for (PageNode pageNode : totalList) {
                System.out.println(pageNode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Data
    @ToString
    static class PageNode {
        private Integer pageNo;
        private PageType pageType;
        private int level;
        private long indexId;
        private int nRecs;
        private List<NodePtr> nodePtrList;
    }

    @Data
    static class NodePtr {
        private Long id;
        private Integer childPageNo;
    }

}
