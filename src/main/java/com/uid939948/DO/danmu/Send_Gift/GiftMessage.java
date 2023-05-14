package com.uid939948.DO.danmu.Send_Gift;

import com.uid939948.DO.pojo.*;

/**
 * 礼物
 */
public class GiftMessage {
//    投喂人气票
//    {"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"batch:gift:combo_id:306801722:629601798:32345:1681001618.4921","batch_combo_send":{"action":"投喂","batch_combo_id":"batch:gift:combo_id:306801722:629601798:32345:1681001618.4921","batch_combo_num":1,"blind_gift":null,"gift_id":32345,"gift_name":"人气票","gift_num":1500,"send_master":null,"uid":306801722,"uname":"lulu今天也要加油哦"},"beatId":"","biz_source":"live","blind_gift":null,"broadcast_id":0,"coin_type":"gold","combo_resources_id":3,"combo_send":{"action":"投喂","combo_id":"gift:combo_id:306801722:629601798:32345:1681001618.4908","combo_num":1500,"gift_id":32345,"gift_name":"人气票","gift_num":1500,"send_master":null,"uid":306801722,"uname":"lulu今天也要加油哦"},"combo_stay_time":12,"combo_total_coin":150000,"crit_prob":0,"demarcation":1,"discount_price":100,"dmscore":128,"draw":0,"effect":0,"effect_block":0,"face":"https://i2.hdslb.com/bfs/face/61517b824db23e5172613ec006d93b3327ad4b44.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":0,"giftId":32345,"giftName":"人气票","giftType":0,"gold":0,"guard_level":2,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":2,"icon_id":0,"is_lighted":1,"medal_color":2951253,"medal_color_border":16771156,"medal_color_end":10329087,"medal_color_start":2951253,"medal_level":30,"medal_name":"被融化","special":"","target_id":629601798},"name_color":"#E17AFF","num":1500,"original_gift_name":"","price":100,"rcost":4204087,"receive_user_info":{"uid":629601798,"uname":"野比大融official"},"remain":0,"rnd":"1681001618131200001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":1,"super_gift_num":1500,"svga_block":0,"switch":true,"tag_image":"","tid":"1681001618131200001","timestamp":1681001618,"top_list":null,"total_coin":150000,"uid":306801722,"uname":"lulu今天也要加油哦"}}

    // 送乱斗卡
//                            2023-04-07 13:42:16.424 | Thread-4raceId | INFO  | 8016 | Thread-4 | com.uid939948.thread.core.ParseMessageThread.run(ParseMessageThread.java:409) : {"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"","batch_combo_send":null,"beatId":"0","biz_source":"Live","blind_gift":null,"broadcast_id":0,"coin_type":"silver","combo_resources_id":1,"combo_send":null,"combo_stay_time":5,"combo_total_coin":0,"crit_prob":0,"demarcation":1,"discount_price":0,"dmscore":56,"draw":0,"effect":0,"effect_block":1,"face":"https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":0,"giftId":30768,"giftName":"10乱斗值卡","giftType":5,"gold":0,"guard_level":0,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":0,"icon_id":0,"is_lighted":0,"medal_color":0,"medal_color_border":0,"medal_color_end":0,"medal_color_start":0,"medal_level":0,"medal_name":"","special":"","target_id":0},"name_color":"","num":5,"original_gift_name":"","price":0,"rcost":2120673,"receive_user_info":{"uid":1007320348,"uname":"koi绾绾"},"remain":0,"rnd":"1680846239110800001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":0,"super_gift_num":0,"svga_block":0,"switch":true,"tag_image":"","tid":"1680846239110800001","timestamp":1680846239,"top_list":null,"total_coin":0,"uid":939948,"uname":"春风十里不如一路有语"}}
//                        2023-04-07 13:43:59:收到道具:春风十里不如一路有语 投喂的:10乱斗值卡 x 5

    /**
     * 动作 投喂或者 送出
     * 小电视红包 为 送出   送礼物为 投喂
     */
    private String action;

    /**
     * 某主键ID
     * 目前在投喂 盲盒时才有
     */
    private String batch_combo_id;
    private Batch_combo_send batch_combo_send;

    /**
     * beatId ID
     * 目前发现为空或者 0
     */
    private String beatId;
    /**
     * 可能为 直播状态
     * biz_source":"live"
     */
    private String biz_source;
    /**
     * 空
     */
    private Blind_gift blind_gift;
    /**
     * 0
     */
    private int broadcast_id;
    /**
     * 礼物类型
     * "coin_type":"silver"  免费礼物
     * "coin_type":"gold" 收费礼物
     */
    private String coin_type;

    /**
     * 人气票为0 也可能为3
     * 乱斗值卡为1
     * 粉丝牌为1（不确定为收费还是免费的） coin_type":"gold","combo_resources_id":1
     * 鸿运小电视为2
     */
    private int combo_resources_id;

    /**
     * 组合礼物类型
     * 非组合礼物时为空
     * 组合礼物包括 宝盒 人气卡
     */
    private Combo_send combo_send;
    /**
     * 組合保存時間
     * 一般是8大12
     */
    private int combo_stay_time;

    /**
     * 组合累计硬币
     */
    private long combo_total_coin;
    /**
     * 0
     */
    private int crit_prob;
    /**
     * 1
     */
    private int demarcation;
    /**
     * 礼物价值
     */
    private long discount_price;
    private int dmscore;
    private int draw;
    private int effect;
    private int effect_block;
    private String face;
    private int face_effect_id;
    private int face_effect_type;
    private int float_sc_resource_id;
    private int giftId;
    private String giftName;
    private int giftType;
    private int gold;
    private int guard_level;
    private boolean is_first;
    private boolean is_join_receiver;
    private boolean is_naming;
    private int is_special_batch;
    private int magnification;
    private Medal_info medal_info;
    private String name_color;
    private int num;
    private String original_gift_name;
    private long price;
    private long rcost;
    private Receive_user_info receive_user_info;
    private int remain;
    private String rnd;
    private String send_master;
    private int silver;
    private int super1;
    private int super_batch_gift_num;
    private int super_gift_num;
    private int svga_block;
    private boolean switch1;
    private String tag_image;
    private String tid;
    private long timestamp;
    private String top_list;
    private long total_coin;
    private long uid;
    private String uname;
}
