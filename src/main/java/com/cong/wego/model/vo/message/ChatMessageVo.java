package com.cong.wego.model.vo.message;

import lombok.Data;

/**
 * 聊天消息VO
 *
 * @author liuhuaicong
 * @date 2023/10/31
 */
@Data
public class ChatMessageVo {
    /**
     * 消息类型 1、群聊 2、私聊
     */
    private Integer type;
    private String content;

}
