package com.cong.wego.model.vo.room;

import lombok.Data;

import java.util.Date;

/**
 * VO室
 *
 * @author cong
 * @date 2024/02/19
 */
@Data
public class RoomVo {
    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间类型 1群聊 2私聊
     */
    private Integer type;

    /**
     * 群最后消息的更新时间
     */
    private Date activeTime;
    /**
     * 会话中的最后一条消息
     */
    private String content;

    /**
     * 昵称
     */
    private String roomName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 未读
     */
    private int unreadNum = 0;
}
