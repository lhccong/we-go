package com.cong.wego.datasource;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.wego.model.entity.RoomFriend;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.enums.chat.FriendSearchTypeEnum;
import com.cong.wego.model.vo.friend.FriendContentVo;
import com.cong.wego.model.vo.friend.FriendVo;
import com.cong.wego.service.RoomFriendService;
import com.cong.wego.service.UserService;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 好友数据源
 *
 * @author cong
 * @date 2024/03/04
 */
@Component
public class FriendDataSource implements DataSource {
    @Resource
    private UserService userService;
    @Resource
    private RoomFriendService roomFriendService;

    @Override
    public FriendContentVo doSearch(List<Long> ids) {
        //获取登录用户id
        Long loginUserId = Long.valueOf(StpUtil.getLoginId().toString());
        // 创建一个FriendContentVo实例
        FriendContentVo friendContentVo = new FriendContentVo();

        // 根据提供的ids，查询用户列表
        List<User> userList = userService.list(new LambdaQueryWrapper<User>().in(User::getId, ids));
        // 设置FriendContentVo的类型和类型名称
        friendContentVo.setType(FriendSearchTypeEnum.FRIEND.getType());
        friendContentVo.setTypeName(FriendSearchTypeEnum.FRIEND.getDesc());

        // 将用户列表转换为朋友信息列表
        List<FriendVo> friendVoList = userList.stream().map(item -> {
            FriendVo friendVo = new FriendVo();
            // 设置头像
            friendVo.setAvatar(item.getUserAvatar());
            // 设置用户名
            friendVo.setName(item.getUserName());
            // 设置用户ID
            friendVo.setUid(item.getId());
            //获取房间ID
            long uid1 = item.getId() > loginUserId ? loginUserId : item.getId();
            long uid2 = item.getId() > loginUserId ? item.getId() : loginUserId;
            RoomFriend roomFriend = roomFriendService.getOne(new LambdaQueryWrapper<RoomFriend>().eq(RoomFriend::getUid1, uid1).eq(RoomFriend::getUid2, uid2));
            // 设置房间ID
            if (roomFriend != null) {
                friendVo.setRoomId(roomFriend.getRoomId());
            }
            return friendVo;
        }).collect(Collectors.toList());

        // 将朋友信息列表设置到FriendContentVo中
        friendContentVo.setContent(friendVoList);

        // 返回FriendContentVo实例
        return friendContentVo;

    }
}
