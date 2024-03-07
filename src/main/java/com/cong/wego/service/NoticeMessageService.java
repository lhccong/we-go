package com.cong.wego.service;

import com.cong.wego.model.dto.friend.FriendAddRequest;
import com.cong.wego.model.entity.NoticeMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cong.wego.model.vo.message.MessageNumVo;
import com.cong.wego.model.vo.message.NoticeMessageVo;

import java.util.List;

/**
* @author liuhuaicong
* @description 针对表【notice_message】的数据库操作Service
* @createDate 2024-03-05 14:05:23
*/
public interface NoticeMessageService extends IService<NoticeMessage> {

    /**
     * 添加好友
     *
     * @param friendAddRequest 好友添加请求
     */
    void addFriend(FriendAddRequest friendAddRequest);

    /**
     * 获取消息数量
     * @return {@link MessageNumVo}
     */
    MessageNumVo getMessageNum();

    /**
     * 获取消息通知列表
     * @return {@link List}<{@link NoticeMessageVo}>
     */
    List<NoticeMessageVo> getMessageNoticeList();

    /**
     * @param id 消息通知id
     * @return {@link Boolean}
     */
    Boolean readMessageNotice(Long id);
}
