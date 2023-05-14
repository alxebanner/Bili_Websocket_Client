package com.uid939948.DO.danmu.Send_Gift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftConfigData {
    private int id;
    private String name;
    private int price;
    private int type;
    private String coin_type;
    private int bag_gift;
    private int effect;
    private String corner_mark;
    private String corner_background;
    private int broadcast;
    private int draw;
    private int stay_time;
    private int animation_frame_num;
    private String desc;
    private String rule;
    private String rights;
    private int privilege_required;
//    private List<Count_map> count_map;

    /**
     * 图片地址
     */
    private String img_basic;
    private String img_dynamic;
    /**
     * 动态图
     */
    private String frame_animation;
    private String gif;
    private String webp;
    private String full_sc_web;
    private String full_sc_horizontal;
    private String full_sc_vertical;
    private String full_sc_horizontal_svga;
    private String full_sc_vertical_svga;
    private String bullet_head;
    private String bullet_tail;
    private int limit_interval;
    private int bind_ruid;
    private int bind_roomid;
    private int gift_type;
    private int combo_resources_id;
    private int max_send_limit;
    private int weight;
    private int goods_id;
    private int has_imaged_gift;
    private String left_corner_text;
    private String left_corner_background;
    private String gift_banner;
    private int diy_count_map;
    private int effect_id;
    private String first_tips;
    private List<Integer> gift_attrs;

}


