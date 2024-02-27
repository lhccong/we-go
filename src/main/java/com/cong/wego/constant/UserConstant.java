package com.cong.wego.constant;

/**
 * 用户常量
 * # @author <a href="https://github.com/lhccong">程序员聪</a>
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    String DEFAULT_AVATAR = "https://img2.baidu.com/it/u=1661624596,544958493&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1709139600&t=8bd455a8d9fa53929c892bc3d0ba556d";

    String DEFAULT_NICKNAME = "用户";
    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion
}
