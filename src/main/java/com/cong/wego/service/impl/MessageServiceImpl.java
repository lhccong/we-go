package com.cong.wego.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.model.entity.Message;
import com.cong.wego.service.MessageService;
import com.cong.wego.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 聪
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-02-18 10:45:29
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




