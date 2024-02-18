package com.cong.wego.common.event;

import com.cong.wego.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户在线活动
 *
 * @author liuhuaicong
 * @date 2023/10/31
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private final User user;

    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
