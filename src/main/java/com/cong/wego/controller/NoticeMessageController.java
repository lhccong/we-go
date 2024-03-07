package com.cong.wego.controller;

import com.cong.wego.common.BaseResponse;
import com.cong.wego.common.ResultUtils;
import com.cong.wego.model.dto.chat.MessageNoticeUpdateRequest;
import com.cong.wego.model.dto.friend.FriendAddRequest;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.vo.message.MessageNumVo;
import com.cong.wego.model.vo.message.NoticeMessageVo;
import com.cong.wego.service.NoticeMessageService;
import com.cong.wego.service.UserService;
import com.cong.wego.sse.SseServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.cong.wego.constant.NoticeConstant.USER_KEY;

/**
 * @author cong
 * @date 2024/03/05
 */
@RestController
@RequestMapping("/notice")
@Slf4j
@RequiredArgsConstructor
@Api(value = "消息通知")
public class NoticeMessageController {
    private final NoticeMessageService noticeMessageService;
    private final UserService userService;
    @PostMapping("/add/friend")
    @ApiOperation(value = "添加好友通知请求")
    public BaseResponse<Boolean> addFriend(@RequestBody FriendAddRequest friendAddRequest) {
       noticeMessageService.addFriend(friendAddRequest);
        return ResultUtils.success(true);
    }
    /**
     * 连接
     * 用户SSE连接
     * 它返回一个SseEmitter实例，这时候连接就已经创建了.
     *
     * @return {@link SseEmitter}
     */
    @GetMapping("/userConnect")
    @ApiOperation(value = "SSE连接请求")
    public SseEmitter connect(String token) {
        //一般取登录用户账号作为 messageId。分组的话需要约定 messageId的格式。
        User loginUser = userService.getLoginUser(token);
        String userId = USER_KEY + loginUser.getId();
        return SseServer.createConnect(userId);
    }

    @GetMapping("/messageNum")
    @ApiOperation(value = "获取消息数量")
    public BaseResponse<MessageNumVo> getMessageNum() {
        return ResultUtils.success(noticeMessageService.getMessageNum());
    }
    @GetMapping("/messageNotice/list")
    @ApiOperation(value = "获取消息列表")
    public BaseResponse<List<NoticeMessageVo>> getMessageNoticeList() {
        return ResultUtils.success(noticeMessageService.getMessageNoticeList());
    }
    @GetMapping("/messageNotice/read")
    @ApiOperation(value = "消息已读")
    public BaseResponse<Boolean> readMessageNotice(Long id) {
        return ResultUtils.success(noticeMessageService.readMessageNotice(id));
    }
    @PostMapping("/messageNotice/handle")
    @ApiOperation(value = "消息处理")
    public BaseResponse<String> handleMessageNotice(@RequestBody MessageNoticeUpdateRequest noticeUpdateRequest) {
        return ResultUtils.success(noticeMessageService.handleMessageNotice(noticeUpdateRequest));
    }

}
