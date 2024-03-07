package com.cong.wego.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.common.ErrorCode;
import com.cong.wego.exception.BusinessException;
import com.cong.wego.mapper.NoticeMessageMapper;
import com.cong.wego.model.dto.chat.MessageNoticeUpdateRequest;
import com.cong.wego.model.dto.friend.FriendAddRequest;
import com.cong.wego.model.entity.NoticeMessage;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.entity.UserFriendRelate;
import com.cong.wego.model.enums.ProcessResultTypeEnum;
import com.cong.wego.model.enums.chat.MessageTypeEnum;
import com.cong.wego.model.enums.chat.NoticeTypeEnum;
import com.cong.wego.model.enums.chat.ReadTargetTypeEnum;
import com.cong.wego.model.enums.chat.RoomTypeEnum;
import com.cong.wego.model.enums.ws.WSReqTypeEnum;
import com.cong.wego.model.vo.message.ChatMessageVo;
import com.cong.wego.model.vo.message.MessageNumVo;
import com.cong.wego.model.vo.message.NoticeMessageVo;
import com.cong.wego.model.vo.ws.request.WSBaseReq;
import com.cong.wego.service.NoticeMessageService;
import com.cong.wego.service.UserFriendRelateService;
import com.cong.wego.service.UserService;
import com.cong.wego.sse.SseServer;
import com.cong.wego.websocket.service.WebSocketService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cong.wego.constant.MessageConstant.ADD_USER_MESSAGE;
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
    @Resource
    private WebSocketService webSocketService;

    @Resource
    private UserFriendRelateService userFriendRelateService;

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

    @Override
    public MessageNumVo getMessageNum() {
        // 获取当前登录用户的ID
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        // 获取未读消息数量 后面考虑使用Redis缓存
        long count = this.count(new LambdaQueryWrapper<NoticeMessage>()
                .eq(NoticeMessage::getToUserId, userId)
                .eq(NoticeMessage::getReadTarget, ReadTargetTypeEnum.UN_READ.getType()));

        // 创建消息数量Vo对象
        MessageNumVo messageNumVo = new MessageNumVo();
        messageNumVo.setNoticeNum(count);

        return messageNumVo;
    }

    @Override
    public List<NoticeMessageVo> getMessageNoticeList() {

        // 根据当前登录用户的ID查询通知消息列表，并按创建时间降序排序
        List<NoticeMessage> noticeMessageList = this.list(new LambdaQueryWrapper<NoticeMessage>()
                .eq(NoticeMessage::getToUserId, Long.valueOf(StpUtil.getLoginId().toString()))
                .orderByDesc(NoticeMessage::getCreateTime));

        // 遍历通知消息列表，转换为NoticeMessageVo对象，并设置发送者的头像和名称
        return noticeMessageList.stream().map(item -> {
            NoticeMessageVo noticeMessageVo = new NoticeMessageVo();
            BeanUtils.copyProperties(item, noticeMessageVo); // 复制基础属性
            User user = userService.getById(item.getUserId()); // 查询发送用户的信息
            noticeMessageVo.setAvatar(user.getUserAvatar()); // 设置发送者头像
            noticeMessageVo.setName(user.getUserName()); // 设置发送者名称

            // 如果通知类型为用户添加好友请求，则设置特定的标题
            if (item.getNoticeType().equals(NoticeTypeEnum.USER.getType())) {
                noticeMessageVo.setTitle(user.getUserName() + "请求添加您为好友");
            }

            return noticeMessageVo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean readMessageNotice(Long id) {
        // 根据通知消息ID查询通知消息
        NoticeMessage noticeMessage = this.getById(id);
        // 如果通知消息不存在，抛出业务异常
        if (noticeMessage == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息不存在");
        }
        //修改消息状态
        noticeMessage.setReadTarget(ReadTargetTypeEnum.READ.getType());
        // 更新通知消息的状态
        return this.updateById(noticeMessage);
    }

    @Override
    public String handleMessageNotice(MessageNoticeUpdateRequest noticeUpdateRequest) {
        // 根据通知消息ID查询通知消息
        NoticeMessage noticeMessage = this.getOne(new LambdaQueryWrapper<NoticeMessage>()
                .eq(NoticeMessage::getId, noticeUpdateRequest.getId())
                .eq(NoticeMessage::getToUserId, Long.valueOf(StpUtil.getLoginId().toString())));
        // 如果通知消息不存在，抛出业务异常
        if (noticeMessage == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息不存在");
        }
        // 初始化描述信息为空格
        String desc = " ";
        // 判断通知类型是否为用户类型
        if (NoticeTypeEnum.of(noticeMessage.getNoticeType()) == NoticeTypeEnum.USER) {
            // 根据处理结果获取描述信息，并更新通知消息的处理结果
            desc = ProcessResultTypeEnum.of(noticeUpdateRequest.getProcessResult()).getDesc();
            noticeMessage.setProcessResult(desc);
            noticeMessage.setReadTarget(ReadTargetTypeEnum.READ.getType());
            if (noticeUpdateRequest.getProcessResult().equals(ProcessResultTypeEnum.AGREE.getType())) {
                // 如果处理结果为同意
                saveUserRelate(noticeMessage);
                // 获取用户token信息
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                // 构建WebSocket消息基础请求
                WSBaseReq wsBaseReq = new WSBaseReq();
                wsBaseReq.setType(WSReqTypeEnum.CHAT.getType());
                wsBaseReq.setUserId(noticeMessage.getUserId());
                // 构建聊天消息体
                ChatMessageVo chatMessageVo = new ChatMessageVo();
                chatMessageVo.setType(MessageTypeEnum.PRIVATE.getType());
                chatMessageVo.setContent(ADD_USER_MESSAGE);
                // 将聊天消息体序列化为JSON字符串，设置为消息数据
                wsBaseReq.setData(JSONUtil.toJsonStr(chatMessageVo));
                // 发送WebSocket消息
                webSocketService.sendMessage(tokenInfo.tokenValue, wsBaseReq);
            }

        }

        this.updateById(noticeMessage);

        return desc;
    }

    private void saveUserRelate(NoticeMessage noticeMessage) {
        // 创建用户和朋友的关系列表
        ArrayList<UserFriendRelate> userFriendRelates = new ArrayList<>();

        userFriendRelates.add(createUserFriendRelate(noticeMessage.getUserId(), noticeMessage.getToUserId(), RoomTypeEnum.PRIVATE.getType()));
        userFriendRelates.add(createUserFriendRelate(noticeMessage.getToUserId(), noticeMessage.getUserId(), RoomTypeEnum.PRIVATE.getType()));

        // 批量保存用户和朋友的关系
        userFriendRelateService.saveBatch(userFriendRelates);

    }
    private UserFriendRelate createUserFriendRelate(Long userId, Long friendId, Integer relateType) {
        UserFriendRelate relate = new UserFriendRelate();
        relate.setUserId(userId);
        relate.setRelateId(friendId);
        relate.setRelateType(relateType);
        return relate;
    }

}




