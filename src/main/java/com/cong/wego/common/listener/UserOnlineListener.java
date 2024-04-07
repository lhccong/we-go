package com.cong.wego.common.listener;


import com.cong.wego.common.event.UserOnlineEvent;
import com.cong.wego.model.entity.User;
import com.cong.wego.service.UserService;
import com.cong.wego.websocket.adapter.WSAdapter;
import com.cong.wego.websocket.cache.UserCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户上线监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserOnlineListener {

    /**
     * 用户服务
     */
    private final UserService userService;
    /**
     * 用户缓存
     */
    private final UserCache userCache;
    /**
     * WS 适配器
     */
    private final WSAdapter wsAdapter;


    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getUpdateTime());
        //推送给所有在线用户，该用户登录成功（暂时不推送）
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDb(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setUpdateTime(user.getUpdateTime());
        userService.updateById(update);
    }

}
