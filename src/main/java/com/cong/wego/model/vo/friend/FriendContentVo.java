package com.cong.wego.model.vo.friend;

import lombok.Data;

import java.util.List;

/**
 * 好友列表
 *
 * @author cong
 * @date 2024/03/04
 */
@Data
public class FriendContentVo {
    /**
     *类型
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String typeName;

    private List<FriendVo> content;

}
