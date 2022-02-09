package org.zlwl.core.util;


import cn.hutool.core.util.StrUtil;

/**
 * {}
 *
 * @author ruanzh
 */
public class StrTool {
    /**
     * 字符串拼接，参考Slf4j源码
     *
     * @param format 原字符串
     * @param args   插入参数
     * @return String 拼接后的字符串
     */
    public static String concat(String format, Object... args) {
        return StrUtil.format(format, args);
    }
}
