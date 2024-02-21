package com.cong.wego.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.wego.mapper.RoomFriendMapper;
import com.cong.wego.model.dto.chat.RoomQueryRequest;
import com.cong.wego.model.entity.*;
import com.cong.wego.model.enums.chat.MessageTypeEnum;
import com.cong.wego.model.vo.room.RoomVo;
import com.cong.wego.service.*;
import com.cong.wego.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

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
                roomVo.setActiveTime(item.getActiveTime());
                roomVo.setType(item.getType());
            }
            //4、查询私聊房间信息
            RoomFriend roomFriend = roomFriendService.getOne(new LambdaQueryWrapper<RoomFriend>().eq(RoomFriend::getRoomId, item.getId()));
            if (roomFriend != null) {
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
}




