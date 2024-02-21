package com.cong.wego.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cong.wego.common.BaseResponse;
import com.cong.wego.common.ResultUtils;
import com.cong.wego.model.dto.chat.MessageQueryRequest;
import com.cong.wego.model.dto.chat.RoomQueryRequest;
import com.cong.wego.model.vo.room.RoomVo;
import com.cong.wego.model.vo.ws.response.ChatMessageResp;
import com.cong.wego.service.MessageService;
import com.cong.wego.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天控制器
 *
 * @author cong
 * @date 2024/02/19
 */
@RestController
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
@Api(value = "聊天")
public class ChatController {

    private final RoomService roomService;
    private final MessageService messageService;
    @PostMapping("/list/page/vo")
    @ApiOperation(value = "分页获取用户房间会话列表")
    public BaseResponse<Page<RoomVo>> listRoomVoByPage(@RequestBody RoomQueryRequest roomQueryRequest) {
        Page<RoomVo> roomVoPage = roomService.listRoomVoByPage(roomQueryRequest);
        return ResultUtils.success(roomVoPage);
    }

    @PostMapping("/message/page/vo")
    @ApiOperation(value = "分页获取用户房间消息列表")
    public BaseResponse<Page<ChatMessageResp>> listMessageVoByPage(@RequestBody MessageQueryRequest messageQueryRequest) {
        Page<ChatMessageResp> messageVoPage = messageService.listMessageVoByPage(messageQueryRequest);
        return ResultUtils.success(messageVoPage);
    }
}
