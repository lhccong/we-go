package com.cong.wego.model.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 消息状态
 * @author cong
 * @date 2024/02/18
 */
@AllArgsConstructor
@Getter
public enum NoticeTypeEnum {
    SYSTEM(1, "系统通知"),
    GROUP(2, "群里通知"),
    USER(3, "好友通知"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, NoticeTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(NoticeTypeEnum.values()).collect(Collectors.toMap(NoticeTypeEnum::getType, Function.identity()));
    }

    public static NoticeTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
