package com.cong.wego.model.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 已读状态
 * @author cong
 * @date 2024/02/18
 */
@AllArgsConstructor
@Getter
public enum ReadTargetTypeEnum {
    UN_READ(0, "未读"),
    READ(1, "已读"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, ReadTargetTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(ReadTargetTypeEnum.values()).collect(Collectors.toMap(ReadTargetTypeEnum::getType, Function.identity()));
    }

    public static ReadTargetTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
