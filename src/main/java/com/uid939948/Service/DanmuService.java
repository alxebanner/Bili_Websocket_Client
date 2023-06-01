package com.uid939948.Service;

/**
 * 弹幕消息体内容定制
 */
public interface DanmuService {
    /**
     * 双击点赞定制内容
     *
     * @param message 收到消息体
     */
    void LikeFunction(String message);

    /**
     * 双击点赞数量
     *
     * @param message 收到消息体
     */
    void LikeNumFunction(String message);


    /**
     * 曾观看人数
     * WATCHED_CHANGE
     *
     * @param message 收到消息体
     */
    void watchNumFunction(String message);

    /**
     * 弹幕定制内容
     *
     * @param message 收到消息体
     */
    void danmuFunction(String message);

    /**
     * 礼物定制内容
     *
     * @param message 收到消息体
     */
    void giftFunction(String message);

    /**
     * 开通舰长消息推送一
     *
     * @param message 收到消息体
     */
    void guardBuyFunction(String message);

    /**
     * 开通舰长 消息推送二（比一更详细）
     *
     * @param message 收到消息体
     */
    void ToastFunction(String message);

    /**
     * super chat 消息推送
     *
     * @param message 收到消息体
     */
    void superChatFunction(String message);

    /**
     * 节奏风暴消息推送 SPECIAL_GIFT
     *
     * @param message 收到消息体
     */
    void specialGiftFunction(String message);

    /**
     * INTERACT_WORD 进房、关注、分享 消息推送
     *interact
     * @param message 收到消息体
     */
    void interactFunction(String message);

    /**
     * 天选推送
     *
     * @param message 收到消息体
     */
    void anchor_lot_start_Function(String message);



    /**
     * 天选结束   中奖消息推送
     *
     * @param message 收到消息体
     */
    void anchorLotAward_Function(String message);


    /**
     * 天选消息堆叠 显示
     * DANMU_AGGREGATION
     * @param message 收到消息体
     */
    void danmu_aggregation_Function(String message);


}
