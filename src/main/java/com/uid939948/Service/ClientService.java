package com.uid939948.Service;

import com.uid939948.Conf.CenterSetConf;


public interface ClientService {
    /**
     * 连接服务器
     *
     * @param roomId 房间号
     * @throws Exception 异常
     */

    void startConnService(long roomId) throws Exception;

    /**
     * 关闭服务器
     */
    void closeConnService();

    /**
     * 开启测试模式
     */
    void starTestService();

    /**
     * 关闭测试模式
     */
    void closeTestSet();
}
