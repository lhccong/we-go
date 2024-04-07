package com.cong.wego.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 * # @author <a href="https://github.com/lhccong">程序员聪</a>
 */
public class SqlUtils {

    private SqlUtils() {
    }
    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return boolean
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
