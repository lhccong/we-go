package com.cong.wego.model.dto.friend;

import lombok.Data;

/**
 * 用户或群聊查询请求
 *
 * @author cong
 * @date 2024/02/19
 */
@Data
public class FriendAddRequest {
    /**
     * 添加的好友id
     */
    private Long userId;
    /**
     * 备注信息
     */
    private String remark;
}
