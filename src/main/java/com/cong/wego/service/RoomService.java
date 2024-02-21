package com.cong.wego.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cong.wego.model.dto.chat.RoomQueryRequest;
import com.cong.wego.model.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cong.wego.model.vo.room.RoomVo;

/**
* @author 聪
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-02-18 10:45:29
*/
public interface RoomService extends IService<Room> {

    /**
     * 按页面列出房间 VO
     *
     * @param roomQueryRequest 房间查询请求
     * @return {@link Page}<{@link RoomVo}>
     */
    Page<RoomVo> listRoomVoByPage(RoomQueryRequest roomQueryRequest);
}
