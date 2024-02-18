package com.cong.wego.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房间表
 * @author 聪
 * @TableName room
 */
@TableName(value ="room")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 房间类型 1群聊 2私聊
     */
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     */
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间
     */
    private Date activeTime;

    /**
     * 会话中的最后一条消息id
     */
    private Long lastMsgId;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    private Object extJson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}