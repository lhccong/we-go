package com.cong.wego.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.common.ErrorCode;
import com.cong.wego.exception.BusinessException;
import com.cong.wego.model.dto.friend.FriendAddRequest;
import com.cong.wego.model.entity.NoticeMessage;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.enums.chat.NoticeTypeEnum;
import com.cong.wego.model.vo.message.NoticeMessageVo;
import com.cong.wego.service.NoticeMessageService;
import com.cong.wego.mapper.NoticeMessageMapper;
import com.cong.wego.service.UserService;
import com.cong.wego.sse.SseServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.cong.wego.constant.NoticeConstant.USER_KEY;

/**
 * @author liuhuaicong
 * @description 针对表【notice_message】的数据库操作Service实现
 * @createDate 2024-03-05 14:05:23
 */
@Service
public class NoticeMessageServiceImpl extends ServiceImpl<NoticeMessageMapper, NoticeMessage>
        implements NoticeMessageService {
    @Resource
    private UserService userService;

    @Override
    public void addFriend(FriendAddRequest friendAddRequest) {

        // 根据好友添加请求获取备注信息和请求用户ID
        String remark = friendAddRequest.getRemark();
        Long toUserId = friendAddRequest.getUserId();

        // 创建通知消息对象，并设置相关信息
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setUserId(Long.valueOf(StpUtil.getLoginId().toString()));
        noticeMessage.setNoticeType(NoticeTypeEnum.USER.getType());
        noticeMessage.setToUserId(toUserId);
        noticeMessage.setNoticeContent(remark);

        // 保存通知消息到数据库
        boolean save = this.save(noticeMessage);
        // 如果保存失败，抛出业务异常
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加好友失败");
        }

        // 检查目标用户是否在线，如果在线，则发送实时通知
        if (SseServer.containUser(USER_KEY + toUserId)) {
            // 构造用于发送给客户端的通知消息Vo对象
            NoticeMessageVo noticeMessageVo = new NoticeMessageVo();
            noticeMessageVo.setNoticeType(NoticeTypeEnum.USER.getType());
            noticeMessageVo.setNoticeContent(remark);
            noticeMessageVo.setUserId(Long.valueOf(StpUtil.getLoginId().toString()));

            // 获取当前登录用户的头像和用户名，用于通知显示
            User loginUser = userService.getLoginUser();
            String userName = loginUser.getUserName();
            noticeMessageVo.setAvatar(loginUser.getUserAvatar());
            noticeMessageVo.setName(userName);
            noticeMessageVo.setTitle(userName + "请求添加您为好友");

            // 向目标用户发送通知消息
            SseServer.sendMessage(USER_KEY + toUserId, JSONUtil.toJsonStr(noticeMessageVo));
        }


    }
}




