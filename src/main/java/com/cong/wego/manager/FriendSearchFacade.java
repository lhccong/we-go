package com.cong.wego.manager;

import com.cong.wego.common.ErrorCode;
import com.cong.wego.datasource.DataSource;
import com.cong.wego.exception.BusinessException;
import com.cong.wego.model.vo.friend.FriendContentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 好友列表门面
 *
 * @author cong
 * @date 2024/03/04
 */
@Component
@Slf4j
public class FriendSearchFacade {

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public FriendContentVo searchAll(Integer type, List<Long> ids) {
        if (type == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未指定查询类型");
        } else {

            DataSource dataSource = dataSourceRegistry.getDataSourceByType(type);
            return dataSource.doSearch(ids);
        }
    }
}
