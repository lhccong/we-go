package com.cong.wego.common.event;

import com.cong.wego.model.dto.ws.PrivateMessageDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/**
 * 用户私信事件
 *
 * @author cong
 * @date 2024/02/18
 */
@Getter
public class UserPrivateMessageEvent extends ApplicationEvent {
    private final PrivateMessageDTO privateMessageDTO;

    public UserPrivateMessageEvent(Object source, PrivateMessageDTO privateMessageDTO) {
        super(source);
        this.privateMessageDTO = privateMessageDTO;
    }
}
