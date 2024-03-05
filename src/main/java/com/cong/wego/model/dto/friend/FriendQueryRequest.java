package com.cong.wego.model.dto.friend;

import lombok.Data;

/**
 * 用户或群聊查询请求
 *
 * @author cong
 * @date 2024/02/19
 */
@Data
public class FriendQueryRequest {
    /**
     * 房间 ID（房间id后缀有s）或用户id
     */
    private String id;
}
