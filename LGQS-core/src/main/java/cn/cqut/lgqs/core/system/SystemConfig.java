package cn.cqut.lgqs.core.system;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置
 */
public class SystemConfig {
    // 小程序相关配置
    public final static String LGQS_WX_INDEX_NEW = "lgqs_wx_index_new";
    public final static String LGQS_WX_INDEX_HOT = "lgqs_wx_index_hot";
    public final static String LGQS_WX_INDEX_BRAND = "lgqs_wx_index_brand";
    public final static String LGQS_WX_INDEX_TOPIC = "lgqs_wx_index_topic";
    public final static String LGQS_WX_INDEX_CATLOG_LIST = "lgqs_wx_catlog_list";
    public final static String LGQS_WX_INDEX_CATLOG_GOODS = "lgqs_wx_catlog_goods";
    public final static String LGQS_WX_SHARE = "lgqs_wx_share";
    // 运费相关配置
    public final static String LGQS_EXPRESS_FREIGHT_VALUE = "lgqs_express_freight_value";
    public final static String LGQS_EXPRESS_FREIGHT_MIN = "lgqs_express_freight_min";
    // 订单相关配置
    public final static String LGQS_ORDER_UNPAID = "lgqs_order_unpaid";
    public final static String LGQS_ORDER_UNCONFIRM = "lgqs_order_unconfirm";
    public final static String LGQS_ORDER_COMMENT = "lgqs_order_comment";
    // 餐厅相关配置
    public final static String LGQS_MALL_NAME = "lgqs_mall_name";
    public final static String LGQS_MALL_ADDRESS = "lgqs_mall_address";
    public final static String LGQS_MALL_PHONE = "lgqs_mall_phone";
    public final static String LGQS_MALL_QQ = "lgqs_mall_qq";
    public final static String LGQS_MALL_LONGITUDE = "lgqs_mall_longitude";
    public final static String LGQS_MALL_Latitude = "lgqs_mall_latitude";

    //所有的配置均保存在该 HashMap 中
    private static Map<String, String> SYSTEM_CONFIGS = new HashMap<>();

    private static String getConfig(String keyName) {
        return SYSTEM_CONFIGS.get(keyName);
    }

    private static Integer getConfigInt(String keyName) {
        return Integer.parseInt(SYSTEM_CONFIGS.get(keyName));
    }

    private static Boolean getConfigBoolean(String keyName) {
        return Boolean.valueOf(SYSTEM_CONFIGS.get(keyName));
    }

    private static BigDecimal getConfigBigDec(String keyName) {
        return new BigDecimal(SYSTEM_CONFIGS.get(keyName));
    }

    public static Integer getNewLimit() {
        return getConfigInt(LGQS_WX_INDEX_NEW);
    }

    public static Integer getHotLimit() {
        return getConfigInt(LGQS_WX_INDEX_HOT);
    }

    public static Integer getBrandLimit() {
        return getConfigInt(LGQS_WX_INDEX_BRAND);
    }

    public static Integer getTopicLimit() {
        return getConfigInt(LGQS_WX_INDEX_TOPIC);
    }

    public static Integer getCatlogListLimit() {
        return getConfigInt(LGQS_WX_INDEX_CATLOG_LIST);
    }

    public static Integer getCatlogMoreLimit() {
        return getConfigInt(LGQS_WX_INDEX_CATLOG_GOODS);
    }

    public static boolean isAutoCreateShareImage() {
        return getConfigBoolean(LGQS_WX_SHARE);
    }

    public static BigDecimal getFreight() {
        return getConfigBigDec(LGQS_EXPRESS_FREIGHT_VALUE);
    }

    public static BigDecimal getFreightLimit() {
        return getConfigBigDec(LGQS_EXPRESS_FREIGHT_MIN);
    }

    public static Integer getOrderUnpaid() {
        return getConfigInt(LGQS_ORDER_UNPAID);
    }

    public static Integer getOrderUnconfirm() {
        return getConfigInt(LGQS_ORDER_UNCONFIRM);
    }

    public static Integer getOrderComment() {
        return getConfigInt(LGQS_ORDER_COMMENT);
    }

    public static String getMallName() {
        return getConfig(LGQS_MALL_NAME);
    }

    public static String getMallAddress() {
        return getConfig(LGQS_MALL_ADDRESS);
    }

    public static String getMallPhone() {
        return getConfig(LGQS_MALL_PHONE);
    }

    public static String getMallQQ() {
        return getConfig(LGQS_MALL_QQ);
    }

    public static String getMallLongitude() {
        return getConfig(LGQS_MALL_LONGITUDE);
    }

    public static String getMallLatitude() {
        return getConfig(LGQS_MALL_Latitude);
    }

    public static void setConfigs(Map<String, String> configs) {
        SYSTEM_CONFIGS = configs;
    }

    public static void updateConfigs(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            SYSTEM_CONFIGS.put(entry.getKey(), entry.getValue());
        }
    }
}