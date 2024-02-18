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
public enum MessageTypeEnum {
    GROUP(1, "群聊"),
    PRIVATE(2, "私聊"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, MessageTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(MessageTypeEnum.values()).collect(Collectors.toMap(MessageTypeEnum::getType, Function.identity()));
    }

    public static MessageTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
