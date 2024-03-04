package com.cong.wego.model.vo.friend;

import lombok.Data;

/**
 * 好友内容
 *
 * @author cong
 * @date 2024/03/04
 */
@Data
public class FriendVo {

    /**
     * 关系id 根据外层来判断是好友还是群
     */
    private Long relateId;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 名字
     */
    private String name;
}
