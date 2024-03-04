package com.cong.wego.model.vo.ws.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 消息
 * Date: 2023-03-23
 * @author liuhuaicong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp {

    @ApiModelProperty("发送者信息")
    private UserInfo fromUser;
    @ApiModelProperty("消息详情")
    private Message message;
    @ApiModelProperty("房间id")
    private Long roomId;
    @Data
    public static class UserInfo {
        @ApiModelProperty("用户名称")
        private String username;
        @ApiModelProperty("用户id")
        private Long uid;
        @ApiModelProperty("头像")
        private String avatar;
    }

    @Data
    public static class Message {
        @ApiModelProperty("消息id")
        private Long id;
        @ApiModelProperty("消息发送时间")
        private Date sendTime;
        @ApiModelProperty("消息内容")
        private String content;
        @ApiModelProperty("消息类型 1正常文本 2.爆赞 （点赞超过10）3.危险发言（举报超5）")
        private Integer type;
    }

}
