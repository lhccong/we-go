package com.cong.wego.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.manager.FriendSearchFacade;
import com.cong.wego.mapper.RoomMapper;
import com.cong.wego.model.dto.chat.RoomQueryRequest;
import com.cong.wego.model.dto.friend.FriendQueryRequest;
import com.cong.wego.model.entity.*;
import com.cong.wego.model.enums.chat.FriendSearchTypeEnum;
import com.cong.wego.model.enums.chat.FriendTargetTypeEnum;
import com.cong.wego.model.enums.chat.RoomTypeEnum;
import com.cong.wego.model.vo.friend.AddFriendVo;
import com.cong.wego.model.vo.friend.FriendContentVo;
import com.cong.wego.model.vo.room.RoomVo;
import com.cong.wego.service.*;
import com.cong.wego.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 聪
 * @description 针对表【room(房间表)】的数据库操作Service实现
 * @createDate 2024-02-18 10:45:29
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
        implements RoomService {

    private final UserRoomRelateService userRoomRelateService;
    private final MessageService messageService;
    private final RoomFriendService roomFriendService;
    private final UserService userService;
    private final RoomGroupService roomGroupService;
    private final UserFriendRelateService userFriendRelateService;
    private final FriendSearchFacade friendSearchFacade;

    @Override
    public Page<RoomVo> listRoomVoByPage(RoomQueryRequest roomQueryRequest) {
        int size = roomQueryRequest.getPageSize();
        int current = roomQueryRequest.getCurrent();
        //当前登陆用户id
        User loginUser = userService.getLoginUser();
        Long loginUserId = loginUser.getId();
        //1、查询用户下的房间，后面可改为游标查询
        Page<UserRoomRelate> page = userRoomRelateService.page(new Page<>(current, size), new LambdaQueryWrapper<UserRoomRelate>().eq(UserRoomRelate::getUserId, loginUserId)
                .orderByDesc(UserRoomRelate::getUpdateTime));
        List<UserRoomRelate> userRoomRelateList = page.getRecords();
        //房间id集合
        Map<Long, RoomVo> roomVoMap = new HashMap<>();
        userRoomRelateList.forEach(item -> {
            Long roomId = item.getRoomId();
            RoomVo roomVo = new RoomVo();
            roomVo.setId(roomId);
            roomVoMap.put(roomId, roomVo);
        });

        //2、查询房间信息
        List<Room> roomList = this.listByIds(roomVoMap.keySet());
        for (Room item : roomList) {
            RoomVo roomVo = roomVoMap.get(item.getId());
            Long lastMsgId = item.getLastMsgId();
            //3、查询数据信息
            Message message = messageService.getById(lastMsgId);
            if (message != null) {
                roomVo.setContent(message.getContent());
            }
            roomVo.setActiveTime(item.getActiveTime());
            roomVo.setType(item.getType());

            //判断房间类型
            if (Objects.equals(item.getType(), RoomTypeEnum.GROUP.getType())) {
                //群聊
                RoomGroup roomGroup = roomGroupService.getOne(new LambdaQueryWrapper<RoomGroup>().eq(RoomGroup::getRoomId, item.getId()));
                roomVo.setAvatar(roomGroup.getAvatar());
                roomVo.setRoomName(roomGroup.getName());
                roomVo.setUserId(roomGroup.getOwnerId());
            } else {
                //4、查询私聊房间信息
                RoomFriend roomFriend = roomFriendService.getOne(new LambdaQueryWrapper<RoomFriend>().eq(RoomFriend::getRoomId, item.getId()));
                Long userId = Objects.equals(roomFriend.getUid1(), loginUserId) ? roomFriend.getUid2() : roomFriend.getUid1();
                //5、查询好友信息
                User user = userService.getById(userId);
                roomVo.setAvatar(user.getUserAvatar());
                roomVo.setRoomName(user.getUserName());
                roomVo.setUserId(userId);
            }

        }
        Page<RoomVo> roomVoPage = new Page<>(current, size, page.getTotal());
        List<RoomVo> roomVoList = new ArrayList<>(roomVoMap.values());
        roomVoPage.setRecords(roomVoList);
        return roomVoPage;
    }

    @Override
    public List<FriendContentVo> listFriendContentVo() {
        // 获取当前登录用户的ID
        Long loginUserId = Long.valueOf(StpUtil.getLoginId().toString());
        // 查询当前登录用户的所有好友关系
        List<UserFriendRelate> userFriendRelates = userFriendRelateService.list(new LambdaQueryWrapper<UserFriendRelate>().eq(UserFriendRelate::getUserId, loginUserId));

        List<FriendContentVo> friendContentVos = new ArrayList<>();
        Map<Integer, List<Long>> roomTypeMap = new HashMap<>();
        // 根据关系类型分组好友关系，以便后续处理
        for (UserFriendRelate userFriendRelate : userFriendRelates) {
            roomTypeMap.computeIfAbsent(userFriendRelate.getRelateType(), k -> new ArrayList<>()).add(userFriendRelate.getRelateId());
        }
        // 遍历分组后的关系类型，为每种关系类型调用搜索服务，将结果添加到返回列表中
        roomTypeMap.keySet().forEach(item -> {
            FriendContentVo friendContentVo = friendSearchFacade.searchAll(item, roomTypeMap.get(item));
            friendContentVos.add(friendContentVo);
        });
        return friendContentVos;

    }

    @Override
    public AddFriendVo searchFriendVo(FriendQueryRequest friendQueryRequest) {

        String id = friendQueryRequest.getId();
        // 判断ID是否为纯数字或以数字开头的字符串
        if (!CommonUtils.isNumeric(id) && !CommonUtils.isNumericExceptLastS(id)) {
            return null;
        }
        if (CommonUtils.isNumeric(id)) {
            // 如果ID是纯数字，则查询用户信息
            Long uid = Long.valueOf(id);
            User user = userService.getById(uid);
            if (user == null) {
                return null;
            }
            // 查询用户和房间的关系，以确定是否为好友
            RoomFriend roomFriend = roomFriendService.getRoomFriend(uid);
            return getAddFriendVo(user, roomFriend);
        } else {
            // 如果ID不是纯数字，则尝试查询群组信息
            String roomId = id.substring(0, id.length() - 1);
            RoomGroup roomGroup = roomGroupService.getOne(new LambdaQueryWrapper<RoomGroup>().eq(RoomGroup::getRoomId, roomId));
            if (roomGroup == null) {
                return null;
            }
            // 封装群组信息
            AddFriendVo addFriendVo = new AddFriendVo();
            addFriendVo.setAvatar(roomGroup.getAvatar()); // 设置群组头像
            addFriendVo.setType(FriendSearchTypeEnum.GROUP.getType()); // 设置查询类型为群组
            addFriendVo.setName(roomGroup.getName()); // 设置群组名称
            addFriendVo.setRoomId(roomGroup.getRoomId()); // 设置群组ID
            // 查询当前用户是否已加入该群组
            UserRoomRelate userRoomRelate = userRoomRelateService.getOne(new LambdaQueryWrapper<UserRoomRelate>()
                    .eq(UserRoomRelate::getUserId, Long.valueOf(StpUtil.getLoginId().toString()))
                    .eq(UserRoomRelate::getRoomId, roomId));
            if (userRoomRelate != null) {
                // 如果已加入，则设置好友目标类型为已加入
                addFriendVo.setFriendTarget(FriendTargetTypeEnum.JOIN.getType());
            }
            return addFriendVo;
        }

    }

    @NotNull
    private static AddFriendVo getAddFriendVo(User user, RoomFriend roomFriend) {
        AddFriendVo addFriendVo = new AddFriendVo();
        addFriendVo.setAvatar(user.getUserAvatar()); // 设置用户头像
        addFriendVo.setUid(user.getId()); // 设置用户ID
        addFriendVo.setType(FriendSearchTypeEnum.FRIEND.getType()); // 设置查询类型为好友
        addFriendVo.setName(user.getUserName()); // 设置用户昵称
        if (roomFriend != null) {
            // 如果用户存在房间关系，则设置房间ID和好友目标类型
            addFriendVo.setRoomId(roomFriend.getRoomId());
            addFriendVo.setFriendTarget(FriendTargetTypeEnum.JOIN.getType());
        }
        return addFriendVo;
    }


}




