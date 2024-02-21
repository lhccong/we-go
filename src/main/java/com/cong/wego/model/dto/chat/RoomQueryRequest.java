package com.cong.wego.model.dto.chat;

import com.cong.wego.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间查询请求
 *
 * @author cong
 * @date 2024/02/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoomQueryRequest extends PageRequest {
    /**
     * 房间 ID
     */
    private Long roomId;
}
