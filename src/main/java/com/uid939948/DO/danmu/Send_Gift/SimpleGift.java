package com.uid939948.DO.danmu.Send_Gift;

import com.uid939948.DO.MedalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 礼物弹幕
 * 简单实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGift {
    /**
     * 用户uid
     */
    private Long uid;

    /**
     * 礼物编号
     */
    private int giftId;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物数量
     */
    private Integer num;

    /**
     * 用户名
     */
    private String uname;

    /**
     * 舰队等级
     */
    private int guard_level;

    /**
     * 价格
     */
    private int price;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 动作
     */
    private String action;

    /**
     * 礼物氪金类型
     * gold 电池
     * silver 银瓜子
     *
     */
    private String coin_type;

    /**
     * 勋章
     */
    private MedalInfo medalInfo;

    private String img_basic;

    private String faceUrl;


}
