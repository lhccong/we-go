package com.cong.wego.manager;

import com.cong.wego.datasource.DataSource;
import com.cong.wego.datasource.FriendDataSource;
import com.cong.wego.datasource.GroupDataSource;
import com.cong.wego.model.enums.chat.FriendSearchTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源注册表
 *
 * @author cong
 * @date 2024/03/04
 */
@Component
@RequiredArgsConstructor
public class DataSourceRegistry {

    private Map<Integer, DataSource> typeDataSourceMap;
    private final FriendDataSource friendDataSource;
    private final GroupDataSource groupDataSource;


    @PostConstruct
    public void doInit() {
        typeDataSourceMap = new HashMap() {{
            put(FriendSearchTypeEnum.GROUP.getType(), groupDataSource);
            put(FriendSearchTypeEnum.FRIEND.getType(), friendDataSource);
        }};
    }


    public DataSource getDataSourceByType(Integer type) {
        if (typeDataSourceMap==null){
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
