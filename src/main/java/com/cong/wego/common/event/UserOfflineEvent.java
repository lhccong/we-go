package com.cong.wego.common.event;

import com.cong.wego.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户离线事件
 *
 * @author cong
 * @date 2024/02/18
 */
@Getter
public class UserOfflineEvent extends ApplicationEvent {
    private final User user;

    public UserOfflineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
