package com.cong.wego.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author liuhuaicong
 * @TableName notice_message
 */
@TableName(value ="notice_message")
@Data
public class NoticeMessage implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 接收用户id
     */
    private Long toUserId;

    /**
     * 消息通知类型 1 系统通知 2 群聊通知 3 好友通知
     */
    private Integer noticeType;

    /**
     * 消息通知内容
     */
    private String noticeContent;

    /**
     * 消息通知标识 0 未读 1 已读
     */
    private Integer readTarget;

    /**
     * 处理结果
     */
    private String processResult;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}