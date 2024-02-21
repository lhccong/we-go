package com.cong.wego.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cong.wego.model.dto.chat.MessageQueryRequest;
import com.cong.wego.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cong.wego.model.vo.ws.response.ChatMessageResp;

/**
* @author 聪
* @description 针对表【message(消息表)】的数据库操作Service
* @createDate 2024-02-18 10:45:29
*/
public interface MessageService extends IService<Message> {

    /**
     * 按页面列出消息 VO
     *
     * @param messageQueryRequest 消息查询请求
     * @return {@link Page}<{@link ChatMessageResp}>
     */
    Page<ChatMessageResp> listMessageVoByPage(MessageQueryRequest messageQueryRequest);
}
