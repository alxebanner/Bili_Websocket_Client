package com.uid939948.Service;

import com.uid939948.Conf.CenterSetConf;


public interface ClientService {
    /**
     * 连接服务器
     *
     * @param roomId 房间号
     * @throws Exception
     */
    void startConnService(long roomId) throws Exception;

    boolean closeConnService();

    /**
     * 保存配置
     *
     * @param centerSetConf 中心配置
     * @param check         是否为json保存
     */
    void changeSet(CenterSetConf centerSetConf, boolean check);

    /**
     * 开启测试模式
     */
    void starTestService();

    /**
     * 关闭测试模式
     */
    void closeTestSet();

}
