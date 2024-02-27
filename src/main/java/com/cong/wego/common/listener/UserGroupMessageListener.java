package com.cong.wego.common.listener;

import com.cong.wego.common.event.UserGroupMessageEvent;
import com.cong.wego.model.dto.ws.GroupMessageDTO;
import com.cong.wego.model.entity.Message;
import com.cong.wego.model.entity.Room;
import com.cong.wego.service.MessageService;
import com.cong.wego.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户群聊消息监听器
 *
 * @author cong
 * @date 2024/02/27
 */
@RequiredArgsConstructor
@Component
public class UserGroupMessageListener {

    private final RoomService roomService;

    private final MessageService messageService;

    @Async
    @EventListener(classes = UserGroupMessageEvent.class)
    public void saveDb(UserGroupMessageEvent event) {
        // 获取群聊消息DTO
        GroupMessageDTO privateMessageDTO = event.getGroupMessageDTO();
        // 获取用户ID
        Long loginUserId = privateMessageDTO.getFromUserId();
        Long roomId = privateMessageDTO.getToRoomId();
        Message message = Message.builder().fromUid(loginUserId).content(privateMessageDTO.getContent()).roomId(roomId).build();
        messageService.save(message);
        //更新房间信息
        Room room = Room.builder().id(roomId).lastMsgId(message.getId()).updateTime(message.getUpdateTime()).build();
        roomService.updateById(room);

    }
}
