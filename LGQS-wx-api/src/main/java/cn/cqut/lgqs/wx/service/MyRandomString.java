package cn.cqut.lgqs.wx.service;

import java.util.Random;

/**
 * @author CuriT
 * @Date 2022-6-22 15:32
 */
public class MyRandomString {
    /**
     * 退款单号
     * @return String 20位
     */
    public static String refundId(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 订单号
     * @return  String 15位
     */
    public static String orderId(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
