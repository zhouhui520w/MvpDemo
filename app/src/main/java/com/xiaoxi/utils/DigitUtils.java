package com.xiaoxi.utils;

public class DigitUtils {

    /**
     * 金额字符串转double，保留两位小数点
     *
     * @param amount
     * @return
     */
    public static double convert(String amount) {
        double d = 0;
        if (StringUtils.isNotEmpty(amount)) {
            try {
                d = Double.parseDouble(amount);
            } catch (NumberFormatException e) {
                d = 0;
            }
        }
        d = Double.valueOf(String.format("%.2f", d));
        return d;
    }

    /**
     * 金额字符串转double，保留两位小数点
     *
     * @param amount
     * @return
     */
    public static double convert(double amount) {
        return Double.valueOf(String.format("%.2f", amount));
    }
}
