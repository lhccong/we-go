package com.cong.wego.datasource;

import com.cong.wego.model.vo.friend.FriendContentVo;

import java.util.List;

/**
 * 数据源接口（新接入数据源必须实现 ）
 *
 * @author 86188
 * @date 2023/03/20
 */
public interface DataSource {
    /**
     * 做搜索
     *
     * @param ids id列表
     */
    FriendContentVo doSearch(List<Long> ids);
}
