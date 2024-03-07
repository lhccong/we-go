package com.cong.wego.model.dto.chat;

import lombok.Data;

/**
 * 消息通知处理
 *
 * @author cong
 * @date 2024/02/19
 */
@Data
public class MessageNoticeUpdateRequest {
    /**
     * 消息 ID
     */
    private Long id;
    /**
     * 消息通知类型 1 系统通知 2 群聊通知 3 好友通知
     */
    private Integer noticeType;
    /**
     * 处理结果 1 已同意 2 已拒绝
     */
    private Integer processResult;
}
