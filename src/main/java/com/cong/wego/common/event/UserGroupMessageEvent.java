package com.cong.wego.common.event;

import com.cong.wego.model.dto.ws.GroupMessageDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/**
 * 用户群聊事件
 *
 * @author cong
 * @date 2024/02/18
 */
@Getter
public class UserGroupMessageEvent extends ApplicationEvent {
    private final GroupMessageDTO groupMessageDTO;

    public UserGroupMessageEvent(Object source, GroupMessageDTO groupMessageDTO) {
        super(source);
        this.groupMessageDTO = groupMessageDTO;
    }
}
