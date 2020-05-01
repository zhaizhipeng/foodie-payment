package com.ysdrzp.utils;

import java.math.BigDecimal;

/**
 * 货币工具类
 */
public class CurrencyUtils {

    /**
     * 分转元，带有小数点
     * @param amount
     * @return
     */
    public static String getFen2YuanWithPoint(Integer amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
    }

    /**
     * 分转元，不带小数，只取整数位
     * @param amount
     * @return
     */
    public static Integer getFen2Yuan(Integer amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).intValue();
    }

    /**
     * 元转分，返回整数
     * @param amount
     * @return
     */
    public static Integer getYuan2Fen(Integer amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 元转分，返回整数
     * @param amount
     * @return
     */
    public static Integer getYuan2Fen(String amount) {
        return BigDecimal.valueOf(Double.valueOf(amount)).multiply(new BigDecimal(100)).intValue();
    }

}
