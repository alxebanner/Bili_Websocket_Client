package com.uid939948.Service;

import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Result.Result;


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

    /**
     * 测试礼物
     *
     * @param name 用户名
     * @param num 礼物数量
     * @param giftName 礼物名称
     */
    void getOneGift(String name, String num, String giftName);

    /**
     * 测试舰长
     *
     * @param name 用户名
     * @param num 舰长数量
     * @param type 舰队类型
     */
    void getOneToast(String name, String num, String type);
}
