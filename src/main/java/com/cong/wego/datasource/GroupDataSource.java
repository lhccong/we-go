package com.cong.wego.datasource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.wego.model.entity.RoomGroup;
import com.cong.wego.model.enums.chat.FriendSearchTypeEnum;
import com.cong.wego.model.vo.friend.FriendContentVo;
import com.cong.wego.model.vo.friend.FriendVo;
import com.cong.wego.service.RoomGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组数据源
 *
 * @author cong
 * @date 2024/03/04
 */
@Component
public class GroupDataSource implements DataSource {
    @Resource
    private RoomGroupService roomGroupService;
    @Override
    public FriendContentVo doSearch(List<Long> ids) {
        // 创建一个FriendContentVo实例
        FriendContentVo friendContentVo = new FriendContentVo();

        // 根据提供的ids，查询用户列表
        List<RoomGroup> userList = roomGroupService.list(new LambdaQueryWrapper<RoomGroup>().in(RoomGroup::getRoomId, ids));
        // 设置FriendContentVo的类型和类型名称
        friendContentVo.setType(FriendSearchTypeEnum.GROUP.getType());
        friendContentVo.setTypeName(FriendSearchTypeEnum.GROUP.getDesc());

        // 将用户列表转换为群聊信息列表
        List<FriendVo> friendVoList = userList.stream().map(item -> {
            FriendVo friendVo = new FriendVo();
            // 设置头像
            friendVo.setAvatar(item.getAvatar());
            // 设置房间名称
            friendVo.setName(item.getName());
            // 设置房间ID
            friendVo.setRelateId(item.getRoomId());
            return friendVo;
        }).collect(Collectors.toList());

        // 将群聊信息列表设置到FriendContentVo中
        friendContentVo.setContent(friendVoList);

        // 返回FriendContentVo实例
        return friendContentVo;
    }
}
