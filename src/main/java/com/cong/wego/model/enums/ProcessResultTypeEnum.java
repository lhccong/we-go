package com.cong.wego.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 处理结果类型
 * @author cong
 * @date 2024/02/18
 */
@AllArgsConstructor
@Getter
public enum ProcessResultTypeEnum {
    AGREE (1, "已同意"),
    REFUSE(2, "已拒绝"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, ProcessResultTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(ProcessResultTypeEnum.values()).collect(Collectors.toMap(ProcessResultTypeEnum::getType, Function.identity()));
    }

    public static ProcessResultTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
