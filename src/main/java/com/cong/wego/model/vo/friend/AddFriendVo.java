package com.cong.wego.model.vo.friend;

import com.cong.wego.model.enums.chat.FriendTargetTypeEnum;
import lombok.Data;

/**
 * 好友内容
 *
 * @author cong
 * @date 2024/03/04
 */
@Data
public class AddFriendVo {

    /**
     * 房间id
     */
    private Long roomId;

    /**
     *用户id
     */
    private Long uid;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 名字
     */
    private String name;

    /**
     * 是否为好友标识
     */
    private int friendTarget = FriendTargetTypeEnum.UN_JOIN.getType();
    /**
     * 类型 1群聊 2好友
     */
    private int type;
}
