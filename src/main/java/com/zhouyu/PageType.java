package com.zhouyu;

/**
 * 大都督周瑜（我的微信: dadudu6789）
 */
public enum PageType {

    FIL_PAGE_INDEX(17855),  /*!< B-tree node */
    FIL_PAGE_RTREE(17854), /*!< B-tree node */
    FIL_PAGE_UNDO_LOG(2),  /*!< Undo log page */
    FIL_PAGE_INODE(3),    /*!< Index node */
    FIL_PAGE_IBUF_FREE_LIST(4),   /*!< Insert buffer free list */
    /* File page types introduced in MySQL/InnoDB 5.1.7 */
    FIL_PAGE_TYPE_ALLOCATED(0),    /*!< Freshly allocated page */
    FIL_PAGE_IBUF_BITMAP(5),    /*!< Insert buffer bitmap */
    FIL_PAGE_TYPE_SYS(6),   /*!< System page */
    FIL_PAGE_TYPE_TRX_SYS(7),  /*!< Transaction system data */
    FIL_PAGE_TYPE_FSP_HDR(8),  /*!< File space header */
    FIL_PAGE_TYPE_XDES(9),  /*!< Extent descriptor page */
    FIL_PAGE_TYPE_BLOB(10),   /*!< Uncompressed BLOB page */
    FIL_PAGE_TYPE_ZBLOB(11),   /*!< First compressed BLOB page */
    FIL_PAGE_TYPE_ZBLOB2(12),   /*!< Subsequent compressed BLOB page */
    FIL_PAGE_TYPE_UNKNOWN(13), 	/*!< In old tablespaces, garbage in FIL_PAGE_TYPE is replaced with this value when flushing pages. */
    FIL_PAGE_COMPRESSED(14),   /*!< Compressed page */
    FIL_PAGE_ENCRYPTED(15),  /*!< Encrypted page */
    FIL_PAGE_COMPRESSED_AND_ENCRYPTED(16),
    /*!< Compressed and Encrypted page */
    FIL_PAGE_ENCRYPTED_RTREE(17);    /*!< Encrypted R-tree page */

    private int type;

    PageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
