package com.cong.wego.common.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.wego.common.event.UserOnlineEvent;
import com.cong.wego.common.event.UserPrivateMessageEvent;
import com.cong.wego.model.dto.ws.PrivateMessageDTO;
import com.cong.wego.model.entity.Message;
import com.cong.wego.model.entity.Room;
import com.cong.wego.model.entity.RoomFriend;

import com.cong.wego.model.entity.UserRoomRelate;
import com.cong.wego.model.enums.chat.MessageTypeEnum;
import com.cong.wego.service.MessageService;
import com.cong.wego.service.RoomFriendService;
import com.cong.wego.service.RoomService;
import com.cong.wego.service.UserRoomRelateService;
import com.cong.wego.websocket.utils.NettyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;

/**
 * 用户私人消息监听器
 *
 * @author cong
 * @date 2024/02/18
 */
@RequiredArgsConstructor
@Component
public class UserPrivateMessageListener {

    private final RoomFriendService roomFriendService;

    private final RoomService roomService;

    private final UserRoomRelateService userRoomRelateService;

    private final MessageService messageService;

    @Async
    @EventListener(classes = UserPrivateMessageEvent.class)
    public void saveDb(UserPrivateMessageEvent event) {
        // 获取私信DTO
        PrivateMessageDTO privateMessageDTO = event.getPrivateMessageDTO();
        // 获取用户ID
        Long loginUserId = privateMessageDTO.getFromUserId();
        Long uid = privateMessageDTO.getToUserId();

        long uid1 = Math.min(loginUserId, uid);
        long uid2 = Math.max(loginUserId, uid);

        // 查询是否有聊天室了
        RoomFriend roomFriend = roomFriendService.getOne(new LambdaQueryWrapper<RoomFriend>()
                .eq(RoomFriend::getUid1, uid1).eq(RoomFriend::getUid2, uid2));
        // 如果没有聊天室，则保存房间和私聊房间
        if (roomFriend == null) {
            // 1、保存房间
            Room room = Room.builder().type(MessageTypeEnum.PRIVATE.getType()).build();
            roomService.save(room);
            // 2、保存私聊房间
            roomFriend = RoomFriend.builder().roomKey(uid1 + "_" + uid2).uid1(uid1).uid2(uid2).roomId(room.getId()).build();
            roomFriendService.save(roomFriend);
            //3.保存两个房间与用户关系
            ArrayList<UserRoomRelate> userRoomRelates = new ArrayList<>();
            UserRoomRelate userRoomRelate1 = UserRoomRelate.builder().userId(uid1).roomId(room.getId()).build();
            UserRoomRelate userRoomRelate2 = UserRoomRelate.builder().userId(uid2).roomId(room.getId()).build();
            userRoomRelates.add(userRoomRelate1);
            userRoomRelates.add(userRoomRelate2);
            userRoomRelateService.saveBatch(userRoomRelates);
        }
        Message message = Message.builder().fromUid(loginUserId).content(privateMessageDTO.getContent()).roomId(roomFriend.getRoomId()).build();
        messageService.save(message);

        //更新房间信息
        Room room = Room.builder().id(roomFriend.getRoomId()).lastMsgId(message.getId()).updateTime(message.getUpdateTime()).build();

        roomService.updateById(room);

    }
}
