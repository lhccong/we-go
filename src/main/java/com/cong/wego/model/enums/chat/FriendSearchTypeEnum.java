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
public enum FriendSearchTypeEnum {
    GROUP(1, "群聊"),
    FRIEND(2, "好友"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, FriendSearchTypeEnum> CACHE;

    static {
        CACHE = Arrays.stream(FriendSearchTypeEnum.values()).collect(Collectors.toMap(FriendSearchTypeEnum::getType, Function.identity()));
    }

    public static FriendSearchTypeEnum of(Integer type) {
        return CACHE.get(type);
    }
}
