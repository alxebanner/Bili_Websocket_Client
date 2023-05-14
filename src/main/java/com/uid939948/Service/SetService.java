package com.uid939948.Service;

import com.uid939948.Conf.CenterSetConf;

public interface SetService {
    /**
     * 初始化项目
     */
    void init();

    void holdSet(CenterSetConf centerSetConf);

    /**
     * 保存配置
     * @param centerSetConf 用户配置
     */
    void changeSet(CenterSetConf centerSetConf);

}
