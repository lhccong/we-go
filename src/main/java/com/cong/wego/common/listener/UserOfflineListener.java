package com.cong.wego.common.listener;


import com.cong.wego.common.event.UserOfflineEvent;
import com.cong.wego.model.entity.User;
import com.cong.wego.service.UserService;
import com.cong.wego.websocket.adapter.WSAdapter;
import com.cong.wego.websocket.cache.UserCache;
import com.cong.wego.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户下线监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserOfflineListener {
    /**
     * 用户服务
     */
    private final UserService userService;

    private final UserCache userCache;

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent event) {
        User user = event.getUser();
        userCache.offline(user.getId(), new Date());
        //推送给所有在线用户，该用户下线(暂不推送)
    }

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDb(UserOfflineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setUpdateTime(new Date());
        userService.updateById(update);
    }

}
