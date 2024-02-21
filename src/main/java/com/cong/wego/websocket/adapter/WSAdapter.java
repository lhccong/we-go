package com.cong.wego.websocket.adapter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.wego.model.dto.ws.PrivateMessageDTO;
import com.cong.wego.model.entity.RoomFriend;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.enums.ws.WSReqTypeEnum;
import com.cong.wego.model.vo.ws.response.ChatMessageResp;
import com.cong.wego.model.vo.ws.response.WSBaseResp;
import com.cong.wego.service.RoomFriendService;
import com.cong.wego.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ws适配器
 *
 * @author cong
 * @date 2024/02/18
 */
@Component
public class WSAdapter {
    @Resource
    private UserService userService;
    @Resource
    private RoomFriendService roomFriendService;

    public WSBaseResp<ChatMessageResp> buildPrivateMessageResp(PrivateMessageDTO privateMessageDTO) {
        // 获取私信的发送者
        Long loginUserId = privateMessageDTO.getFromUserId();
        //发送信息
        String content = privateMessageDTO.getContent();
        ChatMessageResp chatMessageResp = getMessageVo(loginUserId, content);
        // 创建WSBaseResp对象
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        // 设置房间ID
        Long toUserId = privateMessageDTO.getToUserId();
        long uid1 = loginUserId > toUserId ? toUserId : loginUserId;
        long uid2 = loginUserId > toUserId ? loginUserId : toUserId;
        RoomFriend roomFriend = roomFriendService.getOne(new LambdaQueryWrapper<RoomFriend>()
                .eq(RoomFriend::getUid1, uid1).eq(RoomFriend::getUid2, uid2));
        if (roomFriend != null) {
            chatMessageResp.setRoomId(roomFriend.getRoomId());
        }
        // 设置数据和类型
        wsBaseResp.setData(chatMessageResp);
        wsBaseResp.setType(WSReqTypeEnum.CHAT.getType());
        return wsBaseResp;
    }

    @NotNull
    public ChatMessageResp getMessageVo(Long loginUserId, String content) {
        // 创建ChatMessageResp对象
        ChatMessageResp chatMessageResp = new ChatMessageResp();
        // 获取登录用户的信息
        User user = userService.getById(loginUserId);
        // 创建UserInfo对象
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        // 设置用户名、ID和头像
        userInfo.setUsername(user.getUserName());
        userInfo.setUid(user.getId());
        userInfo.setAvatar(user.getUserAvatar());
        // 和发送者信息
        chatMessageResp.setFromUser(userInfo);
        // 创建Message对象
        ChatMessageResp.Message message = new ChatMessageResp.Message();
        // 设置私信内容
        message.setContent(content);
        // 设置消息对象
        chatMessageResp.setMessage(message);

        return chatMessageResp;
    }


}
