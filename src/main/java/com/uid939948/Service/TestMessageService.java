package com.uid939948.Service;

import com.uid939948.Conf.CenterSetConf;

/**
 * 测试类弹幕服务
 */
public interface TestMessageService {
    /**
     * 文字 测试弹幕
     *
     * @param name        用户名
     * @param faceUrl     用户头像
     * @param message     消息体
     * @param guard_level 舰队等级 （1 总督 2 提督 3 舰长）
     */
    void danmuMessage(String name, String faceUrl, String message, int guard_level);

    /**
     * 系统表情 测试弹幕
     * （秒啊 有点东西）
     *
     * @param name        用户名
     * @param faceUrl     用户头像
     * @param message     消息体
     * @param guard_level 舰队等级 （1 总督 2 提督 3 舰长）
     * @param EmoticonUrl 表情地址
     */
    void bulge_displayEmoticonMessage(String name, String faceUrl, String message, int guard_level, String EmoticonUrl);

    /**
     * 正常的表情 测试弹幕
     * （秒啊 有点东西）
     *
     * @param name        用户名
     * @param faceUrl     用户头像
     * @param message     消息体
     * @param guard_level 舰队等级 （1 总督 2 提督 3 舰长）
     * @param EmoticonUrl 表情地址
     */
    void EmoticonMessage(String name, String faceUrl, String message, int guard_level, String EmoticonUrl);

    /**
     * 礼物 测试弹幕
     *
     * @param name        用户名
     * @param faceUrl     用户头像
     * @param message     消息体
     * @param guard_level 舰队等级 （1 总督 2 提督 3 舰长）  暂时无用
     * @param giftName    礼物名称
     */
    void giftMessage(String name, String faceUrl, String message, int guard_level, String giftName);

    /**
     * 上舰 测试弹幕
     *
     * @param name        用户名
     * @param num         用户头像
     * @param message     消息体
     * @param guard_level 舰队等级 （1 总督 2 提督 3 舰长） 暂时无用
     * @param type        舰队等级 （1 总督 2 提督 3 舰长）
     */
    void toastMessage(String name, String num, String message, int guard_level, String type);
}