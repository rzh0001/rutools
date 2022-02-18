package org.zlwl.util;

import java.math.BigDecimal;

/**
 * 数字工具
 *
 * @author ruanzh.eth
 */
public class DecimalTool {

    /**
     * 10的N次方
     *
     * @param exponential 指数N
     * @return
     */
    public static BigDecimal tenPow(Integer exponential) {
        return BigDecimal.TEN.pow(exponential);
    }

}
