package com.cong.wego.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cong.wego.model.dto.user.UserQueryRequest;
import com.cong.wego.model.entity.User;
import com.cong.wego.model.vo.user.LoginUserVO;
import com.cong.wego.model.vo.user.TokenLoginUserVo;
import com.cong.wego.model.vo.user.UserVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * 用户服务
 * # @author <a href="https://github.com/lhccong">程序员聪</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    TokenLoginUserVo userLogin(String userAccount, String userPassword);

    /**
     * 用户通过 MP Open 登录
     * 用户登录（微信开放平台）
     *
     * @param wxOauth2UserInfo 从微信获取的用户信息
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOauth2UserInfo);

    /**
     * 获取当前登录用户
     *
     * @return {@link User}
     */
    User getLoginUser();

    /**
     * 获取当前登录用户根据token
     *
     * @param token 令 牌
     * @return {@link User}
     */
    User getLoginUser(String token);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @return {@link User}
     */
    User getLoginUserPermitNull();

    /**
     * 是否为管理员
     *
     * @return boolean
     */
    boolean isAdmin();

    /**
     * 是否为管理员
     *
     * @param user 用户
     * @return boolean
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return boolean
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 用户
     * @return {@link LoginUserVO}
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户
     * @return {@link UserVO}
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户列表
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求
     * @return {@link QueryWrapper}<{@link User}>
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}
