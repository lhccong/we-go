package com.cong.wego.model.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 私信 DTO
 *
 * @author cong
 * @date 2024/02/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessageDTO implements Serializable {

    private Long fromUserId;

    private Long toUserId;

    private String content;

    private static final long serialVersionUID = 1L;
}
