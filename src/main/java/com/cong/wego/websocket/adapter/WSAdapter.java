package com.cong.wego.websocket.adapter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.wego.model.dto.ws.PrivateMessageDTO;
import com.cong.wego.model.entity.RoomFriend;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.enums.ws.WSReqTypeEnum;
import com.cong.wego.model.vo.ws.response.ChatMessageResp;
import com.cong.wego.model.vo.ws.response.WSBaseResp;
import com.cong.wego.model.vo.ws.response.WSLoginSuccess;
import com.cong.wego.service.RoomFriendService;
import com.cong.wego.service.UserService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

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

    public  WSBaseResp<ChatMessageResp> buildPrivateMessageResp(PrivateMessageDTO privateMessageDTO) {
        // 获取私信的发送者和接收者ID
        Long loginUserId = privateMessageDTO.getFromUserId();
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
        // 设置房间ID和发送者信息
        chatMessageResp.setFromUser(userInfo);
        // 创建Message对象
        ChatMessageResp.Message message = new ChatMessageResp.Message();
        // 设置私信内容
        message.setContent(privateMessageDTO.getContent());
        // 设置消息对象
        chatMessageResp.setMessage(message);
        // 创建WSBaseResp对象
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        // 设置数据和类型
        wsBaseResp.setData(chatMessageResp);
        wsBaseResp.setType(WSReqTypeEnum.CHAT.getType());
        return wsBaseResp;
    }
}
