package com.cong.wego.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.model.dto.chat.MessageQueryRequest;
import com.cong.wego.model.entity.Message;
import com.cong.wego.model.vo.ws.response.ChatMessageResp;
import com.cong.wego.service.MessageService;
import com.cong.wego.mapper.MessageMapper;
import com.cong.wego.websocket.adapter.WSAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 聪
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-02-18 10:45:29
*/
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    private final WSAdapter wsAdapter;

    @Override
    public Page<ChatMessageResp> listMessageVoByPage(MessageQueryRequest messageQueryRequest) {
        // 获取当前页码
        int current = messageQueryRequest.getCurrent();
        // 获取每页大小
        int size = messageQueryRequest.getPageSize();
        // 创建分页对象
        Page<Message> messagePage = this.page(new Page<>(current, size),
                // 创建查询条件对象
                new LambdaQueryWrapper<Message>().eq(Message::getRoomId, messageQueryRequest.getRoomId()).orderByDesc(Message::getCreateTime));
        // 获取分页结果中的消息列表 翻转
        List<Message> messageList = ListUtil.reverse(messagePage.getRecords());
        // 将消息列表转换为ChatMessageResp对象列表
        List<ChatMessageResp> chatMessageRespList = messageList.stream().map(item -> wsAdapter.getMessageVo(item.getFromUid(), item.getContent()))
                .collect(Collectors.toList());
        // 创建新的分页对象，用于存储转换后的消息对象
        Page<ChatMessageResp> messageVoPage = new Page<>(current, size, messagePage.getTotal());
        // 将转换后的消息对象列表设置为新的分页对象的记录
        messageVoPage.setRecords(chatMessageRespList);
        // 返回新的分页对象
        return messageVoPage;
    }
}




