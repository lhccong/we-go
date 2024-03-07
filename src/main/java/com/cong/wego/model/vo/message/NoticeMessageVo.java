package com.cong.wego.model.vo.message;

import lombok.Data;

import java.util.Date;

/**
 * @author cong
 * @date 2024/03/05
 */
@Data
public class NoticeMessageVo {
    /**
     * 用户 id
     */
    private Long userId;


    /**
     * 消息通知类型 1 系统通知 2 群聊通知 3 好友通知
     */
    private Integer noticeType;

    /**
     * 通知标题
     */
    private String title;
    /**
     * 消息通知标识 0 未读 1 已读
     */
    private Integer readTarget;
    /**
     * 消息通知内容
     */
    private String noticeContent;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 名字
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
}
